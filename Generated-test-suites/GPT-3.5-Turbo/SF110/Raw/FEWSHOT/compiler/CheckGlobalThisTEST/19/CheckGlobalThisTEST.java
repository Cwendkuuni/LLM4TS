package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_functionWithConstructorJsDoc_false() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo());
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        n.getJSDocInfo().setMemberTypes("");
        n.getJSDocInfo().setClassType(true);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, null));
    }
    
    @Test
    public void testShouldTraverse_functionWithThisJsDoc_false() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo());
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        n.getJSDocInfo().setMemberTypes("");
        n.getJSDocInfo().setClassType(false);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, null));
    }
    
    @Test
    public void testShouldTraverse_functionWithOverrideJsDoc_false() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo());
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        n.getJSDocInfo().setMemberTypes("");
        n.getJSDocInfo().setClassType(false);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, null));
    }
    
    @Test
    public void testShouldTraverse_functionWithInterfaceJsDoc_false() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo());
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setInterface(true);
        n.getJSDocInfo().setMemberTypes("");
        n.getJSDocInfo().setClassType(false);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, null));
    }
    
    @Test
    public void testShouldTraverse_functionNotBlockScriptnameAssign_false() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo());
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToFront(n);
        Node grandparent = new Node(Token.VAR, parent);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, grandparent));
    }
    
    @Test
    public void testShouldTraverse_functionBlockScriptnameAssign_true() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo());
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToFront(n);
        Node grandparent = new Node(Token.BLOCK, parent);
        assertTrue(CheckGlobalThis.shouldTraverse(t, n, grandparent));
    }
    
    @Test
    public void testShouldTraverse_functionNested_true() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo());
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(new Node(Token.NAME, "a"));
        lhs.addChildToFront(new Node(Token.NAME, "prototype"));
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(lhs);
        parent.addChildToFront(n);
        Node grandparent = new Node(Token.BLOCK, parent);
        assertTrue(CheckGlobalThis.shouldTraverse(t, n, grandparent));
    }
    
    @Test
    public void testShouldTraverse_functionNestedPropertyAccess_false() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(new Node(Token.NAME, "a"));
        lhs.addChildToFront(new Node(Token.STRING, "prototype"));
        Node parent = new Node(Token.GETPROP, lhs);
        parent.addChildToFront(n);
        Node grandparent = new Node(Token.BLOCK, parent);
        assertFalse(CheckGlobalThis.shouldTraverse(t, n, grandparent));
    }

    @Test
    public void testShouldReportThis_leftSideOfAssign_true() {
        Node n = new Node(Token.THIS);
        CheckGlobalThis assignCheck = new CheckGlobalThis(null);
        assignCheck.assignLhsChild = new Node(Token.THIS);
        assertTrue(assignCheck.shouldReportThis(n));
    }
    
    @Test
    public void testShouldReportThis_propertyAccess_true() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP, n);
        assertTrue(CheckGlobalThis.shouldReportThis(n));
    }
    
    @Test
    public void testShouldReportThis_notPropertyAccess_false() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME, n);
        assertFalse(CheckGlobalThis.shouldReportThis(n));
    }
}