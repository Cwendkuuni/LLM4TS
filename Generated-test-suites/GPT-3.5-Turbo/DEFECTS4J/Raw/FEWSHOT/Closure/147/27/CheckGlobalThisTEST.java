package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_FunctionIsConstructorWithThisAnnotation_ReturnsFalse() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        jsDoc.recordConstructor();
        n.setJSDocInfo(jsDoc);
        
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionIsInterface_ReturnsFalse() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        jsDoc.recordInterface();
        n.setJSDocInfo(jsDoc);
        
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasThisTypeAnnotation_ReturnsFalse() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasOverrideAnnotation_ReturnsFalse() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        jsDoc.recordOverride();
        n.setJSDocInfo(jsDoc);
        
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ParentIsNull_ReturnsFalse() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_AssignLhsChildIsNotNull_ReturnsTrue() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, new Node(Token.THIS));
        Node rhs = new Node(Token.NUMBER);
        parent.addChildToFront(lhs);
        parent.addChildToBack(rhs);
        checker.assignLhsChild = lhs;
        
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_ParentIsNotAssign_ReturnsTrue() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_ParentIsGet_ParentIsNotAssignmentToPrototypeProperty_ReturnsTrue() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(new Node(Token.NAME, new Node(Token.THIS)));
        parent.addChildToBack(new Node(Token.STRING, "property"));
        
        boolean result = checker.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReportThis_ReturnsTrue() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(new Node(Token.NAME, new Node(Token.THIS)));
        parent.addChildToBack(new Node(Token.STRING, "property"));
        
        boolean result = checker.shouldReportThis(n, parent);
        
        assertTrue(result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReportThis_ReturnsFalse() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME, new Node(Token.THIS)));
        parent.addChildToBack(new Node(Token.NUMBER));
        
        boolean result = checker.shouldReportThis(n, parent);
        
        assertFalse(result);
    }

    @Test
    public void testVisit_NodeIsAssignLhsChild_AssignLhsChildIsNull() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, new Node(Token.THIS));
        Node rhs = new Node(Token.NUMBER);
        parent.addChildToFront(lhs);
        parent.addChildToBack(rhs);
        checker.assignLhsChild = lhs;
        
        checker.visit(t, n, parent);
        
        assertNull(checker.assignLhsChild);
    }

    @Test
    public void testVisit_NodeIsAssignLhsChild_AssignLhsChildIsNotNull() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, new Node(Token.THIS));
        Node rhs = new Node(Token.NUMBER);
        parent.addChildToFront(lhs);
        parent.addChildToBack(rhs);
        checker.assignLhsChild = rhs;
        
        checker.visit(t, n, parent);
        
        assertNotNull(checker.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_NodeHasJSDocInfo_ReturnsJSDocInfo() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        
        JSDocInfo result = checker.getFunctionJsDocInfo(n);
        
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsNameWithJSDocInfo_ReturnsJSDocInfo() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);
        
        JSDocInfo result = checker.getFunctionJsDocInfo(n);
        
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssignWithNameWithJSDocInfo_ReturnsJSDocInfo() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        Node grandparent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        grandparent.setJSDocInfo(jsDoc);
        parent.setParent(grandparent);
        n.setParent(parent);
        
        JSDocInfo result = checker.getFunctionJsDocInfo(n);
        
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssignWithNoName_ReturnsNull() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        n.setParent(parent);
        
        JSDocInfo result = checker.getFunctionJsDocInfo(n);
        
        assertNull(result);
    }
}