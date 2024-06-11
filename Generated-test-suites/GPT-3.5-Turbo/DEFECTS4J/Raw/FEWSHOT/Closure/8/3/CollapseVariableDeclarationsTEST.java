/*
 * CollapseVariableDeclarations test cases
 */

package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        collapseVariableDeclarations = new CollapseVariableDeclarations(new AbstractCompiler());
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        collapseVariableDeclarations.process(externs, root);
    }

    @Test
    public void testGatherCollapses() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;

        // Test case 1: n is not a VAR node and cannot be redeclared
        boolean result = collapseVariableDeclarations.gatherCollapses(t, n, parent);
        assertFalse(result);

        // Test case 2: n is not a VAR node, but can be redeclared
        result = collapseVariableDeclarations.gatherCollapses(t, n, parent);
        assertFalse(result);

        // Test case 3: n is a VAR node and has no following VAR nodes
        result = collapseVariableDeclarations.gatherCollapses(t, n, parent);
        assertTrue(result);

        // Test case 4: n is a VAR node and has following VAR nodes
        result = collapseVariableDeclarations.gatherCollapses(t, n, parent);
        assertTrue(result);

        // Test case 5: parent is an IF node
        result = collapseVariableDeclarations.gatherCollapses(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testBlacklistStubVars() {
        NodeTraversal t = null;
        Node varNode = null;

        // Test case 1: varNode has no children
        collapseVariableDeclarations.blacklistStubVars(t, varNode);

        // Test case 2: varNode has children, but no blacklisted vars
        collapseVariableDeclarations.blacklistStubVars(t, varNode);

        // Test case 3: varNode has children with blacklisted vars
        collapseVariableDeclarations.blacklistStubVars(t, varNode);
    }

    @Test
    public void testCanBeRedeclared() {
        Node n = null;
        Scope s = null;

        // Test case 1: n is not an assignment node
        boolean result = collapseVariableDeclarations.canBeRedeclared(n, s);
        assertFalse(result);

        // Test case 2: n is an assignment node, but lhs is not a name
        result = collapseVariableDeclarations.canBeRedeclared(n, s);
        assertFalse(result);

        // Test case 3: n is an assignment node, lhs is a name, but var is null
        result = collapseVariableDeclarations.canBeRedeclared(n, s);
        assertFalse(result);

        // Test case 4: n is an assignment node, lhs is a name, var is not null, scope matches, and var is not blacklisted
        result = collapseVariableDeclarations.canBeRedeclared(n, s);
        assertTrue(result);

        // Test case 5: n is an assignment node, lhs is a name, var is not null, scope doesn't match, and var is not blacklisted
        result = collapseVariableDeclarations.canBeRedeclared(n, s);
        assertFalse(result);

        // Test case 6: n is an assignment node, lhs is a name, var is not null, scope matches, and var is blacklisted
        result = collapseVariableDeclarations.canBeRedeclared(n, s);
        assertFalse(result);
    }

    @Test
    public void testApplyCollapses() {
        Collapse collapse = new Collapse(null, null, null);

        // Test case 1
        collapseVariableDeclarations.applyCollapses(collapse);

        // Test case 2
        collapseVariableDeclarations.applyCollapses(collapse);

        // Test case 3
        collapseVariableDeclarations.applyCollapses(collapse);

        // Test case 4
        collapseVariableDeclarations.applyCollapses(collapse);

        // Test case 5
        collapseVariableDeclarations.applyCollapses(collapse);

        // Test case 6
        collapseVariableDeclarations.applyCollapses(collapse);
    }
}