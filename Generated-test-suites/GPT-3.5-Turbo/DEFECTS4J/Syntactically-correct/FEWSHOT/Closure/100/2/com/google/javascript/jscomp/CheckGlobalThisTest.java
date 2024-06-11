package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(null, null);
    }
    
    @Test
    public void testShouldTraverse_WithFunctionNode_ReturnFalse() {
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_WithFunctionNodeAndConstructorJsDoc_ReturnFalse() {
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_WithFunctionNodeAndThisJsDoc_ReturnFalse() {
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_WithFunctionNodeAndVarAssignment_ReturnFalse() {
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.VAR);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_WithAssignNodeOnRightSideOfAssignment_ReturnFalse() {
        NodeTraversal t = null;
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        parent.addChildToFront(n);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_WithAssignNodeOnLeftSideOfAssigment_ReturnTrue() {
        NodeTraversal t = null;
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        parent.addChildToBack(new Node(Token.NAME));
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_WithAssignNodeOnLeftSideOfNestedAssignment_ReturnTrue() {
        NodeTraversal t = null;
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        Node child = new Node(Token.NAME);
        parent.addChildToFront(child);
        child.addChildToFront(new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.STRING, "prototype")));
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_WithAssignNodeOnLeftSideOfNestedAssignmentAndQualifiedName_ReturnTrue() {
        NodeTraversal t = null;
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        Node child = new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.STRING, "prototype"));
        parent.addChildToFront(child);
        child.setQualifiedName("example.prototype.test");
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testVisit_WithThisNode_ShouldReportGlobalThis() {
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = null;
        
        checkGlobalThis.visit(t, n, parent);
        //assert that compiler.report(...) is called
    }
    
    @Test
    public void testVisit_WithThisNodeAndAssignLhsChildNull_ShouldNotReportGlobalThis() {
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = null;
        checkGlobalThis.setAssignLhsChild(null);
        
        checkGlobalThis.visit(t, n, parent);
        //assert that compiler.report(...) is not called
    }
    
    @Test
    public void testVisit_WithThisNodeAndAssignLhsChildNotNull_ShouldReportGlobalThis() {
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = null;
        checkGlobalThis.setAssignLhsChild(new Node(Token.NAME));
        
        checkGlobalThis.visit(t, n, parent);
        //assert that compiler.report(...) is called
    }
    
}