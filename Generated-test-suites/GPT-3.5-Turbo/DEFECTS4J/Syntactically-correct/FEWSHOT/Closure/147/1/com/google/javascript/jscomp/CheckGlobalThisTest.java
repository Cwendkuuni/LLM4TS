package com.google.javascript.jscomp;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  
    @Test
    public void testShouldTraverse_functionConstructorOrThisAnnotation_returnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        NodeTraversal t = new NodeTraversal(null);
      
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        boolean result = checker.shouldTraverse(t, node, parent);
      
        assertFalse(result);
    }
  
    @Test
    public void testShouldTraverse_functionNonGlobalContext_returnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        NodeTraversal t = new NodeTraversal(null);
      
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        boolean result = checker.shouldTraverse(t, node, parent);
      
        assertFalse(result);
    }
  
    @Test
    public void testShouldTraverse_assignmentLeftSide_returnsTrue() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        NodeTraversal t = new NodeTraversal(null);
      
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        boolean result = checker.shouldTraverse(t, node, parent);
      
        assertTrue(result);
    }
  
    @Test
    public void testShouldTraverse_assignmentToPrototypeProperty_returnsFalse() {
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        NodeTraversal t = new NodeTraversal(null);
      
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        boolean result = checker.shouldTraverse(t, node, parent);
      
        assertFalse(result);
    }
  
    @Test
    public void testShouldTraverse_assignmentToNestedPrototypeProperty_returnsFalse() {
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.GETPROP);
        NodeTraversal t = new NodeTraversal(null);
      
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        boolean result = checker.shouldTraverse(t, node, parent);
      
        assertFalse(result);
    }
  
    @Test
    public void testShouldReportThis_leftSideOfAssignment_returnsTrue() {
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
      
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        boolean result = checker.shouldReportThis(node, parent);
      
        assertTrue(result);
    }
  
    @Test
    public void testShouldReportThis_propertyAccess_returnsTrue() {
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
      
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        boolean result = checker.shouldReportThis(node, parent);
      
        assertTrue(result);
    }
  
    @Test
    public void testShouldReportThis_notLeftSideOfAssignmentOrPropertyAccess_returnsFalse() {
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
      
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        boolean result = checker.shouldReportThis(node, parent);
      
        assertFalse(result);
    }
}