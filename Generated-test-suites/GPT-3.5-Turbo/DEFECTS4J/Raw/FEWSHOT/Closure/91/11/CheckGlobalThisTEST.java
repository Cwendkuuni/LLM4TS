package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_shouldReturnFalseForConstructors() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsAnnotatedWithThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsAnnotatedWithOverride() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsInsideNonGlobalContexts() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        Node grandParent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        grandParent.addChildToFront(parent);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnTrueForOtherFunctions() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }
    
    @Test
    public void testVisit_shouldReportWarningForThisOnLeftSideOfAssign() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        DiagnosticType expected = DiagnosticType.warning("JSC_USED_GLOBAL_THIS",
            "dangerous use of the global 'this' object");
        // add assert for the error message
    }
    
    @Test
    public void testVisit_shouldNotReportWarningForThisOnRightSideOfAssign() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(n);
        DiagnosticType expected = null;
        // add assert for the error message
    }
    
    @Test
    public void testVisit_shouldReportWarningForThisWithPropertyAccess() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);
        DiagnosticType expected = DiagnosticType.warning("JSC_USED_GLOBAL_THIS",
            "dangerous use of the global 'this' object");
        // add assert for the error message
    }
    
    @Test
    public void testVisit_shouldNotReportWarningForOtherNodes() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.ADD);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(n);
        DiagnosticType expected = null;
        // add assert for the error message
    }
}