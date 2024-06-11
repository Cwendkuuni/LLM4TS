package org.apache.commons.cli;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.Scope;

import java.util.Collection;
import java.util.List;

public class MaybeReachingVariableUseTest {

    @Test
    public void testSimpleLocalVariableUsage() {
        Scope jsScope = new Scope();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

        Node varAssignmentNode = new Node(Token.VAR, "a", new Node[]{}, new Node[]{}, null);
        Node useNode = new Node(Token.NAME, "a", null, null, null);

        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        maybeReachingVariableUse.process(varAssignmentNode);
        maybeReachingVariableUse.process(useNode);

        Collection<Node> uses = maybeReachingVariableUse.getUses("a", varAssignmentNode);
        assertThat(uses).containsExactly(useNode);
    }

    @Test
    public void testLocalVariableUsageInCondition() {
        Scope jsScope = new Scope();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

        Node assignmentNode = new Node(Token.ASSIGN, null, new Node[]{}, new Node[]{}, null);
        Node conditionNode = new Node(Token.IF, null, new Node[]{assignmentNode}, new Node[]{new Node(Token.NAME, "a", null, null, null)}, null);
        Node useNode = new Node(Token.NAME, "a", null, null, null);

        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        maybeReachingVariableUse.process(assignmentNode);
        maybeReachingVariableUse.process(conditionNode);
        maybeReachingVariableUse.process(useNode);

        Collection<Node> uses = maybeReachingVariableUse.getUses("a", assignmentNode);
        assertThat(uses).containsExactly(conditionNode, useNode);
    }

    @Test
    public void testLocalVariableUsageInLoop() {
        Scope jsScope = new Scope();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

        Node loopStartNode = new Node(Token.FOR, null, new Node[]{new Node(Token.NAME, "i", null, null, null)}, new Node[]{new Node(Token.NAME, "i", null, null, null)}, null);
        Node assignmentNode = new Node(Token.ASSIGN, null, new Node[]{new Node(Token.NAME, "i", null, null, null)}, new Node[]{new Node(Token.NAME, "i", null, null, null)}, null);
        Node useNode = new Node(Token.NAME, "i", null, null, null);

        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        maybeReachingVariableUse.process(loopStartNode);
        maybeReachingVariableUse.process(assignmentNode);
        maybeReachingVariableUse.process(useNode);

        Collection<Node> uses = maybeReachingVariableUse.getUses("i", loopStartNode);
        assertThat(uses).containsExactly(assignmentNode, useNode);
    }

    @Test
    public void testEscapedVariableUsage() {
        Scope jsScope = new Scope();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

        Node globalVariableAssignmentNode = new Node(Token.ASSIGN, null, new Node[]{new Node(Token.NAME, "global", null, null, null)}, new Node[]{new Node(Token.NAME, "global", null, null, null)}, null);
        Node localVariableAssignmentNode = new Node(Token.ASSIGN, null, new Node[]{new Node(Token.NAME, "escaped", null, null, null)}, new Node[]{new Node(Token.NAME, "escaped", null, null, null)}, null);
        Node useNode = new Node(Token.NAME, "escaped", null, null, null);

        jsScope.declareVar("global", Scope.VarType.VAR, null, null, null, null);
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        maybeReachingVariableUse.process(globalVariableAssignmentNode);
        maybeReachingVariableUse.process(localVariableAssignmentNode);
        maybeReachingVariableUse.process(useNode);

        Collection<Node> uses = maybeReachingVariableUse.getUses("escaped", localVariableAssignmentNode);
        assertNull(uses);
    }
}