package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_functionWithConstructorAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setThisType("Type");
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionWithOverrideAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionInsideInvalidParent() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionInsideValidParent() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_functionInsideAssignParent() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_functionInsideVarAssignParent() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.VAR);
        Node grandparent = new Node(Token.ASSIGN);
        parent.setParent(grandparent);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_notParentNode() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);

        assertEquals(true, result);
    }
}