package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScopedAliasesTest {
  
  @Test
  public void testEnterScope() {
    // test case when parent of the current node is null
    // should return false
    Node node = null;
    NodeTraversal t = null;
    assertFalse(new ScopedAliases.Traversal().shouldTraverse(t, node, null));
    
    // test case for goog.scope function
    Node n = new Node(Token.CALL, new Node(Token.NAME, "goog.scope"), new Node(Token.FUNCTION, new Node(Token.NAME, "anonymousFnNode")));
    Node parent = new Node(Token.CALL, new Node(Token.NAME, "parent"), n);
    assertTrue(new ScopedAliases.Traversal().shouldTraverse(t, n, parent));
    
    // test case for any other function
    Node n1 = new Node(Token.CALL, new Node(Token.NAME, "function1"), new Node(Token.FUNCTION, new Node(Token.NAME, "anonymousFnNode1")));
    Node parent1 = new Node(Token.CALL, new Node(Token.NAME, "parent1"), n1);
    assertFalse(new ScopedAliases.Traversal().shouldTraverse(t, n1, parent1));
  }
  
  @Test
  public void testExitScope() {
    // test case when scopeDepth is 2
    NodeTraversal t = new NodeTraversal(new Compiler(), new Nodes());
    Node n = new Node(Token.CALL, new Node(Token.NAME, "goog.scope"), new Node(Token.FUNCTION, new Node(Token.NAME, "anonymousFnNode")));
    Node parent = new Node(Token.CALL, new Node(Token.NAME, "parent"), n);
    assertFalse(new ScopedAliases.Traversal().shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testVisit() {
    // test case for aliasVar != null
    NodeCompilationUnit n = new NodeCompilationUnit(null, null, null);
    NodeTraversal t = null;
    Node n1 = new Node(Token.NAME, Token.MUL);
    Node parent = new Node(Token.NAME, "testVisit");
    new ScopedAliases.Traversal().visit(t, n1, parent);
    
    // TODO: Add more test cases for different branches of the method
  }
}