package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        
        assertFalse(checker.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        
        assertFalse(checker.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithPrototypeAssignment_ShouldReturnTrue() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = createAssignNode(createGetPropNode("this", "prototype"), null);
        
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        
        assertTrue(checker.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithSubpropertyAssignment_ShouldReturnTrue() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = createAssignNode(createGetPropNode("this.prototype", "property"), null);
        
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        
        assertTrue(checker.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_AssignmentNodeWithLeftSideThis_ShouldSetAssignLhsChild() {
        NodeTraversal t = null;
        Node n = createGetPropNode("this", "property");
        Node parent = createAssignNode(n, null);
        
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        
        checker.shouldTraverse(t, n, parent);
        
        assertTrue(checker.assignLhsChild == n);
    }
    
    @Test
    public void testShouldTraverse_AssignmentNodeWithGetPropOnLeftSide_ShouldReturnFalse() {
        NodeTraversal t = null;
        Node lhs = createGetPropNode("this", "property");
        Node n = createGetPropNode(lhs, "subproperty");
        Node parent = createAssignNode(n, null);
        
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        
        assertFalse(checker.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_AssignmentNode_ShouldReturnTrue() {
        NodeTraversal t = null;
        Node lhs = createNameNode("a");
        Node n = createGetPropNode(lhs, "property");
        Node parent = createAssignNode(lhs, n);
        
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        
        assertTrue(checker.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testVisit_ThisNodeWithAssignLhsChild_ShouldReportError() {
        NodeTraversal t = null;
        Node n = createThisNode();
        Node parent = null;
        
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        checker.assignLhsChild = createGetPropNode("this", "property");
        boolean errorReported = false;
        
        try {
            checker.visit(t, n, parent);
        } catch (Exception e) {
            errorReported = true;
        }
        
        assertTrue(errorReported);
    }
    
    @Test
    public void testVisit_ThisNodeWithoutAssignLhsChild_ShouldNotReportError() {
        NodeTraversal t = null;
        Node n = createThisNode();
        Node parent = null;
        
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        checker.assignLhsChild = null;
        boolean errorReported = false;
        
        try {
            checker.visit(t, n, parent);
        } catch (Exception e) {
            errorReported = true;
        }
        
        assertFalse(errorReported);
    }
    
    private Node createNameNode(String name) {
        return new Node(Token.NAME, name);
    }
    
    private Node createGetPropNode(String firstChild, String lastChild) {
        Node node = new Node(Token.GETPROP);
        node.addChildToFront(createNameNode(firstChild));
        node.addChildToBack(createNameNode(lastChild));
        return node;
    }
    
    private Node createThisNode() {
        return new Node(Token.THIS);
    }
    
    private Node createAssignNode(Node lhs, Node rhs) {
        Node node = new Node(Token.ASSIGN);
        node.addChildToFront(lhs);
        node.addChildToBack(rhs);
        return node;
    }
}