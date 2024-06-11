
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_ShouldReturnTrue_ForNonGlobalContext() {
        // Create nodes for testing
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnFalse_ForConstructorFunction() {
        // Create nodes for testing
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.putProp(Node.JSDOC_INFO_PROP, jsDoc);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnFalse_ForFunctionWithThisTypeAnnotation() {
        // Create nodes for testing
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.putProp(Node.JSDOC_INFO_PROP, jsDoc);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnFalse_ForFunctionWithOverrideAnnotation() {
        // Create nodes for testing
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.putProp(Node.JSDOC_INFO_PROP, jsDoc);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnFalse_ForFunctionWithNoLogicalPlaceForThisAnnotation() {
        // Create nodes for testing
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.SCRIPT);
        parent.setParent(grandParent);
        JSDocInfo jsDoc = new JSDocInfo();
        n.putProp(Node.JSDOC_INFO_PROP, jsDoc);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testVisit_ShouldReportError_ForUnsafeUseOfThisKeyword() {
        // Create nodes for testing
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        Node grandParent = new Node(Token.ASSIGN);
        parent.setParent(grandParent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        checkGlobalThis.visit(t, n, parent);

        // Assert that an error was reported
        // TODO: Add assertion for the error
    }

    @Test
    public void testVisit_ShouldNotReportError_ForSafeUseOfThisKeyword() {
        // Create nodes for testing
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        Node grandParent = new Node(Token.NAME);
        parent.setParent(grandParent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        checkGlobalThis.visit(t, n, parent);

        // Assert that no error was reported
        // TODO: Add assertion for no error
    }
}
