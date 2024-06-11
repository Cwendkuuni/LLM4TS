package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        // test when n.getType() is not Token.FUNCTION
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = new Node(Token.ADD);
        Node parent = new Node(Token.ADD);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));

        // test when n.getType() is Token.FUNCTION and jsDoc is constructor
        CheckGlobalThis checkGlobalThis1 = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t1 = new NodeTraversal(checkGlobalThis1);
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.ADD);
        JSDocInfo jsDocInfo1 = new JSDocInfo();
        jsDocInfo1.setConstructor(true);
        n1.setJSDocInfo(jsDocInfo1);
        assertFalse(checkGlobalThis1.shouldTraverse(t1, n1, parent1));

        // test when n.getType() is Token.FUNCTION and jsDoc hasThisType
        CheckGlobalThis checkGlobalThis2 = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t2 = new NodeTraversal(checkGlobalThis2);
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.ADD);
        JSDocInfo jsDocInfo2 = new JSDocInfo();
        jsDocInfo2.recordThisType();
        n2.setJSDocInfo(jsDocInfo2);
        assertFalse(checkGlobalThis2.shouldTraverse(t2, n2, parent2));

        // test when n.getType() is Token.FUNCTION and parent.getType() is not Token.ASSIGN
        CheckGlobalThis checkGlobalThis3 = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t3 = new NodeTraversal(checkGlobalThis3);
        Node n3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.FUNCTION);
        assertTrue(checkGlobalThis3.shouldTraverse(t3, n3, parent3));

        // test when parent.getType is Token.ASSIGN and n is not lhs
        CheckGlobalThis checkGlobalThis4 = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t4 = new NodeTraversal(checkGlobalThis4);
        Node n4 = new Node(Token.NAME);
        Node parent4 = new Node(Token.ASSIGN);
        parent4.addChildToFront(new Node(Token.NAME));
        parent4.addChildToBack(new Node(Token.NAME));
        assertFalse(checkGlobalThis4.shouldTraverse(t4, n4, parent4));

        // test when parent.getType is Token.ASSIGN and lhs.getLastChild().getString().equals("prototype")
        CheckGlobalThis checkGlobalThis5 = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t5 = new NodeTraversal(checkGlobalThis5);
        Node n5 = new Node(Token.NAME);
        Node parent5 = new Node(Token.ASSIGN);
        Node getprop5 = new Node(Token.GETPROP);
        getprop5.addChildToFront(new Node(Token.NAME));
        getprop5.addChildToBack(new Node(Token.NAME));
        getprop5.getLastChild().addChildToBack(new Node("\"prototype\""));
        parent5.addChildToFront(getprop5);
        parent5.addChildToBack(new Node(Token.NAME));
        assertFalse(checkGlobalThis5.shouldTraverse(t5, n5, parent5));

        // test when parent.getType is Token.ASSIGN and leftName is not null and leftName contains ".prototype."
        CheckGlobalThis checkGlobalThis6 = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t6 = new NodeTraversal(checkGlobalThis6);
        Node n6 = new Node(Token.NAME);
        Node parent6 = new Node(Token.ASSIGN);
        Node getprop6 = new Node(Token.GETPROP);
        getprop6.addChildToFront(new Node("\"name\""));
        getprop6.addChildToBack(new Node("\"prototype\""));
        parent6.addChildToFront(getprop6);
        parent6.addChildToBack(new Node(Token.NAME));
        assertFalse(checkGlobalThis6.shouldTraverse(t6, n6, parent6));
    }

    @Test
    public void testVisit() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        // assert something
    }
}