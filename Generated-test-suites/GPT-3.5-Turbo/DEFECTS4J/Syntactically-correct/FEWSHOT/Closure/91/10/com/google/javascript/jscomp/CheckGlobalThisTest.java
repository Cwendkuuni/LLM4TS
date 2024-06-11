package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionNotConstructor_shouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_ShouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder(true).build());
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfoBuilder jsDocInfoBuilder = new JSDocInfoBuilder();
        jsDocInfoBuilder.recordThisType();
        n.putProp(Node.JSDOC_INFO_PROP, jsDocInfoBuilder.build());
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_BlockFunction_ShouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_ScriptFunction_ShouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_AssignFunction_ShouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_StringFunction_ShouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_NumberFunction_ShouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionFunction_ShouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(new Node(Token.GETPROP));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_ThisNodeShouldReportThis_shouldReport() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(new Node(Token.GETPROP));
        checkGlobalThis.visit(t, n, parent);
        // Make assertion here
    }

    @Test
    public void testVisit_AssignLhsChild_shouldSetAssignLhsChildToNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), checkGlobalThis);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionHasJsDoc_shouldReturnJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(new JSDocInfoBuilder().build(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentOfTypeNameHasJsDoc_shouldReturnJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(new JSDocInfoBuilder().build(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_GrandParentOfTypeVarHasJsDoc_shouldReturnJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.setParent(new Node(Token.VAR));
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(new JSDocInfoBuilder().build(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDocFound_shouldReturnNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }

    @Test
    public void testShouldReportThis_AssignLhsChildNotNull_shouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP));
        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ParentNotNull_shouldReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP));
        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ParentIsNull_shouldReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS), null);
        assertFalse(result);
    }
}