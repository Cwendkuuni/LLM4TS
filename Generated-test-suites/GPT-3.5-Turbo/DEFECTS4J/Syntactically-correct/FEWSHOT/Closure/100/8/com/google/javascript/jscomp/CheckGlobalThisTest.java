package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;
    private CheckLevel level;
    private NodeTraversal.Callback callback;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        callback = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_WithFunctionConstructor_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().recordConstructor().build());
        boolean result = callback.shouldTraverse(t, n, null);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_WithFunctionThis_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().recordThis().build());
        boolean result = callback.shouldTraverse(t, n, null);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_WithAssignment_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node parent = new Node(Token.ASSIGN);
        Node n = new Node(Token.NAME);
        parent.addChildToFront(n);
        boolean result = callback.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_WithAssignmentToPrototype_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(new Node(Token.NAME, "prototype"));
        parent.addChildToFront(lhs);
        Node n = new Node(Token.NAME);
        lhs.addChildToBack(n);
        boolean result = callback.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_WithAssignmentToNestedPrototype_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(new Node(Token.NAME, "prototype"));
        parent.addChildToFront(lhs);
        Node n = new Node(Token.NAME, "subproperty");
        lhs.addChildToBack(n);
        boolean result = callback.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_WithFunction_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node parent = new Node(Token.NAME);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToFront(n);
        boolean result = callback.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testVisit_WithThisOnAssignLhsChild_CallsReport() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis callback = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        Node assignLhsChild = new Node(Token.NAME);
        assignLhsChild.putBooleanProp(Node.LEADING_THIS, true);
        parent.addChildToFront(assignLhsChild);
        callback.visit(t, n, parent);
        // Asserts for report call
    }

    @Test
    public void testVisit_WithThisNotOnAssignLhsChild_DoesNotCallReport() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis callback = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        Node assignLhsChild = new Node(Token.NAME);
        parent.addChildToFront(assignLhsChild);
        callback.visit(t, n, parent);
        // Asserts for no report call
    }

}