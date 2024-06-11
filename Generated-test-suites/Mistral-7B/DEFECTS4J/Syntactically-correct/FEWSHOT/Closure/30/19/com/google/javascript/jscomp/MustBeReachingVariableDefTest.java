package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MustBeReachingVariableDefTest {
    @Test
    public void testGetDef() {
        Scope scope = new Scope();
        AbstractCompiler compiler = new Compiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

        // Test with empty scope
        assertNull(analysis.getDef("x", null));

        // Test with simple definition
        Node definitionNode = NodeUtil.createAssignment(Node.newName("x"), Node.newNumber(1));
        Node useNode = Node.newName("x");
        scope.declareVar("x", Node.newName("x"), definitionNode, null);
        analysis.compute(definitionNode);
        analysis.compute(useNode);
        assertEquals(definitionNode, analysis.getDef("x", useNode));

        // Test with multiple definitions
        Node definitionNode2 = Node.newName("x");
        Node useNode2 = Node.newName("x");
        scope.declareVar("x", Node.newName("x"), definitionNode, null);
        scope.declareVar("x", Node.newName("x"), definitionNode2, null);
        analysis.compute(definitionNode);
        analysis.compute(definitionNode2);
        analysis.compute(useNode2);
        assertNull(analysis.getDef("x", useNode2));

        // Test with escaped variable
        Var escapedVar = scope.getVar("x");
        scope.declareVar("x", Node.newName("x"), definitionNode, null);
        escaped.add(escapedVar);
        analysis.compute(definitionNode);
        analysis.compute(useNode);
        assertNull(analysis.getDef("x", useNode));

        // Test with use of outer scope variable
        Scope outerScope = new Scope();
        outerScope.declareVar("y", Node.newName("y"), Node.newNumber(2), null);
        scope = new Scope(outerScope);
        scope.declareVar("x", Node.newName("x"), definitionNode, null);
        analysis.compute(definitionNode);
        analysis.compute(useNode);
        assertTrue(analysis.dependsOnOuterScopeVars("y", useNode));
    }
}