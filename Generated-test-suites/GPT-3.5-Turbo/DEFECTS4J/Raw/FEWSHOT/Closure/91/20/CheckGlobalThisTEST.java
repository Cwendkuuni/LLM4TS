package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_withFunctionTypeAndConstructor() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        parent.addChildToFront(n);
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        parent.addChildToFront(n);
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndOverrideAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        parent.addChildToFront(n);
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndInterfaceAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setInterface(true);
        n.setJSDocInfo(jsDoc);
        parent.addChildToFront(n);
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withNotGlobalContext() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(n);
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withAssignmentLhsChildNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        parent.addChildToFront(n);
        CheckGlobalThis.assignLhsChild = n;
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withPropertyAccessAndAssignmentToPrototype() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        n.addChildToFront(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.STRING, "prototype"));
        parent.addChildToFront(n);
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withPropertyAccessAndRightSideAssignmentToPrototype() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        n.addChildToFront(new Node(Token.NAME));
        Node prototype = new Node(Token.STRING, "prototype");
        n.addChildToBack(prototype);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(new Node(Token.NAME));
        lhs.addChildToBack(prototype.cloneTree());
        parent.addChildToFront(lhs);
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withThisInLeftSideOfAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withThisAsPropertyAccess() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testVisit_withThisAndShouldReportThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        CheckGlobalThis.assignLhsChild = n;
        CheckGlobalThis.checkLevel = CheckLevel.WARNING;
        CheckGlobalThis.compiler = new AbstractCompiler();
        CheckGlobalThis.compiler.report(new Diagnostic(T, "dangerous use of the global 'this' object", null, CheckGlobalThis.GLOBAL_THIS, level));
        CheckGlobalThis.visit(t, n, parent);
        assertEquals(null, CheckGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_withThisAndNotShouldReportThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        CheckGlobalThis.checkLevel = CheckLevel.WARNING;
        CheckGlobalThis.compiler = new AbstractCompiler();
        CheckGlobalThis.visit(t, n, parent);
        assertEquals(null, CheckGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_withFunctionNodeAndJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withNameNodeAndJsDocInfo() {
        Node n = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withNameNodeAndVarParent() {
        Node n = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.VAR);
        Node grandparent = new Node(Token.ASSIGN);
        parent.addChildToFront(grandparent);
        grandparent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withNameNodeAndAssignParent() {
        Node n = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(null, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withNoJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(null, result);
    }
}