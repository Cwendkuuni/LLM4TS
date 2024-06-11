package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {

    private AbstractCompiler compiler;
    private PeepholeOptimizationsPass pass;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        pass = new PeepholeOptimizationsPass(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN_LIST);
        Node root = new Node(Token.ROOT);

        pass.process(externs, root);

        // Test assertions here
    }

    @Test
    public void testShouldRetraverse_ChangedNodeInFunction() {
        Node node = createFunctionNode();

        assertTrue(pass.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_NoChangedNodeInFunction() {
        Node node = createFunctionNode();
        setChangedState(false, true);

        assertFalse(pass.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_ChangedNodeInScript() {
        Node node = createScriptNode();

        assertTrue(pass.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_NoChangedNodeInScript() {
        Node node = createScriptNode();
        setChangedState(false, true);

        assertFalse(pass.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_ParentIsNull() {
        Node node = new Node(Token.EMPTY);
        node.putBooleanProp(Node.FUNCTION_PROP, true);

        assertFalse(pass.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_NotFunctionOrScript() {
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.EMPTY);
        node.replaceWith(parent);

        assertFalse(pass.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_ChangedNodeInParentFunction() {
        Node node = createFunctionNode();
        setChangedState(true, true);

        assertFalse(pass.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_ChangedNodeInParentScript() {
        Node node = createScriptNode();
        setChangedState(true, true);

        assertFalse(pass.shouldRetraverse(node));
    }

    @Test
    public void testShouldVisit_FunctionNode_TraverseChildScopesTrue() {
        Node node = createFunctionNode();
        setTraverseChildScopes(true);

        assertTrue(pass.shouldVisit(node));
    }

    @Test
    public void testShouldVisit_FunctionNode_TraverseChildScopesFalse() {
        Node node = createFunctionNode();
        setTraverseChildScopes(false);

        assertFalse(pass.shouldVisit(node));
    }

    @Test
    public void testShouldVisit_ScriptNode_TraverseChildScopesTrue() {
        Node node = createScriptNode();
        setTraverseChildScopes(true);

        assertTrue(pass.shouldVisit(node));
    }

    @Test
    public void testShouldVisit_ScriptNode_TraverseChildScopesFalse() {
        Node node = createScriptNode();
        setTraverseChildScopes(false);

        assertFalse(pass.shouldVisit(node));
    }

    @Test
    public void testShouldVisit_NotFunctionOrScript() {
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.EMPTY);
        node.replaceWith(parent);

        assertTrue(pass.shouldVisit(node));
    }

    @Test
    public void testExitNode_FunctionNode() {
        Node node = createFunctionNode();

        pass.exitNode(node);

        // Test assertions here
    }

    @Test
    public void testExitNode_ScriptNode() {
        Node node = createScriptNode();

        pass.exitNode(node);

        // Test assertions here
    }

    private Node createFunctionNode() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);

        node.replaceWith(parent);

        return node;
    }

    private Node createScriptNode() {
        Node node = new Node(Token.SCRIPT);
        Node parent = new Node(Token.ROOT);

        node.replaceWith(parent);

        return node;
    }

    private void setChangedState(boolean changed, boolean traverseChildScopes) {
        PeepholeOptimizationsPass.ScopeState state = pass.traversalState.peek();
        state.changed = changed;
        state.traverseChildScopes = traverseChildScopes;
    }

    private void setTraverseChildScopes(boolean traverseChildScopes) {
        PeepholeOptimizationsPass.ScopeState state = pass.traversalState.peek();
        state.traverseChildScopes = traverseChildScopes;
    }
}