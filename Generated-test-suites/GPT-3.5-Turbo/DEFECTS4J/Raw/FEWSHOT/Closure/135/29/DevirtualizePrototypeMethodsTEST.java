package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

    @Test
    public void testIsCall() {
        Node node = new Node(Token.CALL);
        Node parent = new Node(Token.EXPR_RESULT, node);
        node.addChildToBack(new Node(Token.NAME, "foo"));
        assertTrue(DevirtualizePrototypeMethods.isCall(new UseSite(node, parent)));

        node = new Node(Token.CALL);
        parent = new Node(Token.EXPR_RESULT, node);
        node.addChildToBack(new Node(Token.STRING, "foo"));
        assertFalse(DevirtualizePrototypeMethods.isCall(new UseSite(node, parent)));

        node = new Node(Token.CALL);
        parent = new Node(Token.ASSIGN, new Node(Token.NAME, "foo"), node);
        node.addChildToBack(new Node(Token.NAME, "bar"));
        assertFalse(DevirtualizePrototypeMethods.isCall(new UseSite(node, parent)));
    }

    @Test
    public void testIsPrototypeMethodDefinition() {
        Node node = new Node(Token.NAME, "prototype");
        Node nameNode = new Node(Token.NAME, "foo");
        Node getPropNode = new Node(Token.GETPROP, nameNode, node);
        Node assignNode = new Node(Token.ASSIGN, getPropNode, new Node(Token.FUNCTION));
        Node parent = new Node(Token.EXPR_RESULT, assignNode);
        assertTrue(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(nameNode));

        node = new Node(Token.NAME, "prototype");
        nameNode = new Node(Token.NAME, "foo");
        getPropNode = new Node(Token.GETPROP, nameNode, node);
        Node getStringNode = new Node(Token.STRING, "bar");
        Node rightNode = new Node(Token.FUNCTION);
        rightNode.putProp(Node.ORIGINALNAME_PROP, "bar");
        nameNode = new Node(Token.GETPROP, nameNode, getStringNode);
        assignNode = new Node(Token.ASSIGN, nameNode, rightNode);
        parent = new Node(Token.EXPR_RESULT, assignNode);
        assertTrue(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(nameNode));

        node = new Node(Token.NAME, "prototype");
        nameNode = new Node(Token.NAME, "foo");
        getPropNode = new Node(Token.NAME, "bar");
        assignNode = new Node(Token.ASSIGN, getPropNode, new Node(Token.FUNCTION));
        parent = new Node(Token.EXPR_RESULT, assignNode);
        assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(nameNode));

        node = new Node(Token.NAME, "prototype");
        nameNode = new Node(Token.NAME, "foo");
        getPropNode = new Node(Token.GETPROP, nameNode, node);
        assignNode = new Node(Token.ASSIGN, getPropNode, new Node(Token.STRING));
        parent = new Node(Token.EXPR_RESULT, assignNode);
        assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(nameNode));
    }

    @Test
    public void testGetRewrittenMethodName() {
        String originalMethodName = "foo";
        String expected = "JSCompiler_StaticMethods_foo";
        assertEquals(expected, DevirtualizePrototypeMethods.getRewrittenMethodName(originalMethodName));
    }

    @Test
    public void testIsEligibleDefinition() {
        // TODO: Add test cases for DevirtualizePrototypeMethods.isEligibleDefinition()
    }

    @Test
    public void testRewriteCallSites() {
        // TODO: Add test cases for DevirtualizePrototypeMethods.rewriteCallSites()
    }

    @Test
    public void testRewriteDefinition() {
        // TODO: Add test cases for DevirtualizePrototypeMethods.rewriteDefinition()
    }

    @Test
    public void testFixFunctionType() {
        // TODO: Add test cases for DevirtualizePrototypeMethods.fixFunctionType()
    }

    @Test
    public void testReplaceReferencesToThis() {
        // TODO: Add test cases for DevirtualizePrototypeMethods.replaceReferencesToThis()
    }

}