package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_notGlobalContext_returnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertTrue(checkGlobalThis.shouldTraverse(null, null, null));
    }

    @Test
    public void testShouldTraverse_constructorFunction_returnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        functionNode.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionWithThis_returnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.addThisType();
        functionNode.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_functionWithOverride_returnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        functionNode.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_validFunction_returnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.setType(Token.SCRIPT);
        Node nameNode = new Node(Token.NAME);
        Node assignNode = new Node(Token.ASSIGN);
        Node stringNode = new Node(Token.STRING);
        Node numberNode = new Node(Token.NUMBER);

        boolean result1 = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, scriptNode);
        boolean result2 = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, nameNode);
        boolean result3 = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, assignNode);
        boolean result4 = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, stringNode);
        boolean result5 = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, numberNode);

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertTrue(result5);
    }

    @Test
    public void testVisit_thisNode_reportError() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);
        Node thisNode = new Node(Token.THIS);

        checkGlobalThis.visit(nodeTraversal, thisNode, null);

        // Assert that an error is reported
    }

    @Test
    public void testVisit_assignLhsChild_resetAssignLhsChild() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);
        Node assignLhsChild = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = assignLhsChild;

        checkGlobalThis.visit(nodeTraversal, assignLhsChild, null);

        assertNull(checkGlobalThis.assignLhsChild);
    }
}