
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionWithoutLogicalPlaceForThisAnnotation_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null);
        assertEquals(false, result);
    }

    @Test
    public void testVisit_ThisOnLeftSideOfAssignment_ShouldReportError() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        checkGlobalThis.assignLhsChild = new Node(Token.THIS);
        Node thisNode = new Node(Token.THIS);
        checkGlobalThis.visit(null, thisNode, null);
        assertEquals(DiagnosticType.JSC_USED_GLOBAL_THIS, checkGlobalThis.compiler.reportedError.getType());
    }

    @Test
    public void testVisit_ThisWithPropertyAccess_ShouldReportError() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node propAccess = new Node(Token.GETPROP);
        propAccess.addChildToFront(new Node(Token.THIS));
        checkGlobalThis.visit(null, propAccess, null);
        assertEquals(DiagnosticType.JSC_USED_GLOBAL_THIS, checkGlobalThis.compiler.reportedError.getType());
    }

    @Test
    public void testShouldReportThis_ThisWithAssignLhsChild_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        checkGlobalThis.assignLhsChild = new Node(Token.GETPROP);
        boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS));
        assertEquals(true, result);
    }

    @Test
    public void testShouldReportThis_ThisWithPropertyAccess_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node propAccess = new Node(Token.GETPROP);
        propAccess.addChildToFront(new Node(Token.THIS));
        boolean result = checkGlobalThis.shouldReportThis(propAccess);
        assertEquals(true, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_HasJSDocInfo_ReturnJSDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSDocInfo(new JSDocInfo());
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(new JSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsNameOrAssignWithJSDocInfo_ReturnJSDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());
        Node functionNode = new Node(Token.FUNCTION, parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(new JSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsNameWithoutJSDocInfo_ReturnNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node parent = new Node(Token.NAME);
        Node functionNode = new Node(Token.FUNCTION, parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(null, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssign_ReturnJSDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node parent = new Node(Token.ASSIGN);
        parent.setJSDocInfo(new JSDocInfo());
        Node functionNode = new Node(Token.FUNCTION, parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(new JSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssignWithoutJSDocInfo_ReturnNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node parent = new Node(Token.ASSIGN);
        Node functionNode = new Node(Token.FUNCTION, parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(null, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsVarWithJSDocInfo_ReturnJSDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node grandparent = new Node(Token.VAR);
        grandparent.setJSDocInfo(new JSDocInfo());
        Node parent = new Node(Token.NAME, grandparent);
        Node functionNode = new Node(Token.FUNCTION, parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(new JSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsVarWithoutJSDocInfo_ReturnNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node grandparent = new Node(Token.VAR);
        Node parent = new Node(Token.NAME, grandparent);
        Node functionNode = new Node(Token.FUNCTION, parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(null, result);
    }
}
