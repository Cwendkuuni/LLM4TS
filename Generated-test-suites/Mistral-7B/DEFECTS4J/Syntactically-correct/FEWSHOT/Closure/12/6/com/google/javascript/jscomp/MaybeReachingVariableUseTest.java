package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RunWith(JUnit4.class)
public class MaybeReachingVariableUseTest {
    private static final Scope TEST_SCOPE = new Scope();
    private ControlFlowGraph<Node> testCfG;
    private MaybeReachingVariableUse maybeReachingVariableUse;

    @Before
    public void setUp() {
        // Create a simple control flow graph for testing.
        Node root = Node.newBlock(Token.BLOCK, null);
        Node aDef = Node.newVar("a", Token.VAR, root, null, null);
        Node bDef = Node.newVar("b", Token.VAR, root, null, null);
        Node cDef = Node.newVar("c", Token.VAR, root, null, null);
        Node aUse = Node.newName("a", Token.NAME, null, aDef, null);
        Node bUse = Node.newName("b", Token.NAME, null, bDef, null);
        Node cUse = Node.newName("c", Token.NAME, null, cDef, null);
        Node ifStatement = Node.newIf(Token.IF, null, aUse, Node.newName("x", Token.NAME, null, null, null), null);
        Node assignStatement = Node.newAssign(Token.ASSIGN, null, bUse, Node.newName("a", Token.NAME, null, aDef, null), null);
        Node assignStatement2 = Node.newAssign(Token.ASSIGN, null, cUse, Node.newName("b", Token.NAME, null, bDef, null), null);
        Node assignStatement3 = Node.newAssign(Token.ASSIGN, null, aUse, Node.newInt(Token.INT_LITERAL, 5), null);
        Node returnStatement = Node.newReturn(Token.RETURN, null, null, null);
        testCfG = new ControlFlowGraph<>(root, List.of(
            new DiGraphEdge<>(aDef, ifStatement),
            new DiGraphEdge<>(aDef, assignStatement),
            new DiGraphEdge<>(bDef, assignStatement),
            new DiGraphEdge<>(bDef, assignStatement2),
            new DiGraphEdge<>(ifStatement, assignStatement),
            new DiGraphEdge<>(assignStatement, assignStatement2),
            new DiGraphEdge<>(assignStatement2, returnStatement)
        ));

        maybeReachingVariableUse = new MaybeReachingVariableUse(testCfG, TEST_SCOPE, null);
    }

    @Test
    public void testComputeMayUse() {
        ReachingUses entryLattice = maybeReachingVariableUse.createEntryLattice();
        ReachingUses aReachingUses = maybeReachingVariableUse.flowThrough(aDef, entryLattice);
        ReachingUses bReachingUses = maybeReachingVariableUse.flowThrough(bDef, entryLattice);
        ReachingUses cReachingUses = maybeReachingVariableUse.flowThrough(cDef, entryLattice);

        // Check that the variables are not using themselves.
        assertFalse("Variable 'a' should not use itself.", aReachingUses.mayUseMap.containsKey(jsScope.getVar("a")));
        assertFalse("Variable 'b' should not use itself.", bReachingUses.mayUseMap.containsKey(jsScope.getVar("b")));
        assertFalse("Variable 'c' should not use itself.", cReachingUses.mayUseMap.containsKey(jsScope.getVar("c")));

        // Check that 'a' is not using 'b' or 'c'.
        assertNull("Variable 'a' should not use 'b'.", aReachingUses.mayUseMap.get(jsScope.getVar("b")));
        assertNull("Variable 'a' should not use 'c'.", aReachingUses.mayUseMap.get(jsScope.getVar("c")));

        // Check that 'b' is not using 'a' or 'c'.
        assertNull("Variable 'b' should not use 'a'.", bReachingUses.mayUseMap.get(jsScope.getVar("a")));
        Collection<Node> bUses = getUses("b", assignStatement2);
        assertEquals("Variable 'b' should use itself.", 1, bUses.size());
        assertEquals("Variable 'b' should use itself as the last definition.", assignStatement2, bUses.iterator().next());

        // Check that 'c' is not using 'a' or 'b'.
        assertNull("Variable 'c' should not use 'a'.", cReachingUses.mayUseMap.get(jsScope.getVar("a")));
        assertNull("Variable 'c' should not use 'b'.", cReachingUses.mayUseMap.get(jsScope.getVar("b")));

        // Check that 'a' is using itself and 'b'.
        Collection<Node> aUses = getUses("a", aDef);
        assertEquals("Variable 'a' should use itself as the entry value.", 1, aUses.size());
        assertEquals("Variable 'a' should use itself as the entry value.", aDef, aUses.iterator().next());
        aUses = getUses("a", assignStatement);
        assertEquals("Variable 'a' should use 'b' as the last definition before the use.", 1, aUses.size());
        assertEquals("Variable 'a' should use 'b' as the last definition before the use.", assignStatement, aUses.iterator().next());

        // Check that 'b' is using itself and 'a'.
        bUses = getUses("b", bDef);
        assertEquals("Variable 'b' should use itself as the entry value.", 1, bUses.size());
        assertEquals("Variable 'b' should use itself as the entry value.", bDef, bUses.iterator().next());
        bUses = getUses("b", assignStatement2);
        assertEquals("Variable 'b' should use 'a' as the last definition before the use.", 1, bUses.size());
        assertEquals("Variable 'b' should use 'a' as the last definition before the use.", assignStatement, bUses.iterator().next());
    }

    @Test
    public void testEscapedVariables() {
        String escapedVariableName = "escapeMe";
        Node escapedVariableDef = Node.newVar(escapedVariableName, Token.VAR, null, null, null);
        testCfG = new ControlFlowGraph<>(Node.newBlock(Token.BLOCK, null), List.of());
        maybeReachingVariableUse = new MaybeReachingVariableUse(testCfG, TEST_SCOPE, null);
        Set<Var> escapedVariables = Sets.newHashSet();
        maybeReachingVariableUse.computeEscaped(TEST_SCOPE, escapedVariables, null);
        assertTrue("Variable '" + escapedVariableName + "' should be considered escaped.", escapedVariables.contains(TEST_SCOPE.getVar(escapedVariableName)));

        ReachingUses entryLattice = maybeReachingVariableUse.createEntryLattice();
        ReachingUses escapedVariableReachingUses = maybeReachingVariableUse.flowThrough(escapedVariableDef, entryLattice);

        // Check that the escaped variable is not using itself or other variables.
        assertNull("Escaped variable 'escapeMe' should not use itself.", escapedVariableReachingUses.mayUseMap.get(TEST_SCOPE.getVar(escapedVariableName)));
        assertNull("Escaped variable 'escapeMe' should not use other variables.", escapedVariableReachingUses.mayUseMap.get(jsScope.getVar("a")));
        assertNull("Escaped variable 'escapeMe' should not use other variables.", escapedVariableReachingUsuses.mayUseMap.get(jsScope.getVar("b")));
    }

    private Collection<Node> getUses(String variableName, Node definitionNode) {
        return maybeReachingVariableUse.getUses(variableName, definitionNode);
    }
}