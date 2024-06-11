package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);

        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.BLOCK);
        assertFalse(checkGlobalThis.shouldTraverse(t, n1, parent1));

        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.ASSIGN);
        parent2.addChildToFront(n2);
        assertFalse(checkGlobalThis.shouldTraverse(t, n2, parent2));

        Node n3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.BLOCK);
        parent3.addChildToFront(new Node(Token.BLOCK));
        parent3.getFirstChild().addChildToFront(n3);
        assertTrue(checkGlobalThis.shouldTraverse(t, n3, parent3));

        Node n4 = new Node(Token.FUNCTION);
        Node parent4 = new Node(Token.STRING);
        assertTrue(checkGlobalThis.shouldTraverse(t, n4, parent4));

        Node n5 = new Node(Token.FUNCTION);
        Node parent5 = new Node(Token.NUMBER);
        assertTrue(checkGlobalThis.shouldTraverse(t, n5, parent5));
    }

    @Test
    public void testVisit() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);

        Node n1 = new Node(Token.THIS);
        Node parent1 = new Node(Token.GETPROP);
        assertTrue(checkGlobalThis.shouldReportThis(n1, parent1));

        Node n2 = new Node(Token.THIS);
        Node parent2 = new Node(Token.GETELEM);
        assertFalse(checkGlobalThis.shouldReportThis(n2, parent2));

        Node n3 = new Node(Token.THIS);
        Node parent3 = new Node(Token.ASSIGN);
        checkGlobalThis.visit(t, n3, parent3);
        assertNull(checkGlobalThis.assignLhsChild);

        Node n4 = new Node(Token.THIS);
        Node parent4 = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = parent4;
        checkGlobalThis.visit(t, n4, parent4);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);

        Node n1 = new Node(Token.FUNCTION);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n1));

        Node n2 = new Node(Token.NAME);
        n2.setJSDocInfo(new JSDocInfo(true));
        Node parent2 = new Node(Token.SCRIPT);
        parent2.addChildToFront(n2);
        assertEquals(true, checkGlobalThis.getFunctionJsDocInfo(n2).isConstructor());

        Node n3 = new Node(Token.NAME);
        Node parent3 = new Node(Token.ASSIGN);
        parent3.addChildToFront(n3);
        assertEquals(null, checkGlobalThis.getFunctionJsDocInfo(n3));

        Node n4 = new Node(Token.NAME);
        Node parent4 = new Node(Token.VAR);
        Node gramps4 = new Node(Token.SCRIPT);
        gramps4.addChildToFront(parent4);
        parent4.addChildToFront(n4);
        parent4.setJSDocInfo(new JSDocInfo(true));
        assertEquals(true, checkGlobalThis.getFunctionJsDocInfo(n4).isConstructor());

        Node n5 = new Node(Token.NAME);
        Node parent5 = new Node(Token.ASSIGN);
        parent5.setJSDocInfo(new JSDocInfo(true));
        parent5.addChildToFront(n5);
        assertEquals(true, checkGlobalThis.getFunctionJsDocInfo(n5).isConstructor());
    }
}