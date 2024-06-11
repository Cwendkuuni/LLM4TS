package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_FunctionNotConstructorOrHasThisTypeOrIsOverride_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node functionNode = new Node(Token.FUNCTION);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_FunctionIsConstructor_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        functionNode.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionHasThisType_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        functionNode.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionIsOverride_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        functionNode.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_ParentTypeIsNotBlockOrScriptOrNameOrAssign_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.DIV);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_ParentTypeIsBlock_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_ParentTypeIsScript_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_ParentTypeIsName_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_ParentTypeIsAssign_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent);

        assertEquals(true, result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReportThis_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        boolean result = checkGlobalThis.shouldReportThis(thisNode, parent);

        assertEquals(true, result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldNotReportThis_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkGlobalThis);
        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldReportThis(thisNode, parent);

        assertEquals(false, result);
    }
}