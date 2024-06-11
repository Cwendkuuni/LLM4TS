package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(externs, root);
    
    // assertEquals(expected, actual);
    // assertTrue(condition);
    // assertFalse(condition);
    // assertNull(obj);
    // assertNotNull(obj);
    // assertSame(expected, actual);
    // assertNotSame(expected, actual);
    // assertArrayEquals(expectedArray, resultArray);
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node n = new Node(Token.SCRIPT);
    
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    prepareAst.normalizeNodeTypes(n);
    
    // Add assertions
  }

  @Test
  public void testNormalizeBlocks() {
    Node n = new Node(Token.SCRIPT);
    
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    prepareAst.normalizeBlocks(n);
    
    // Add assertions
  }

  @Test
  public void testPrepareAnnotations() {
    NodeTraversal t = new NodeTraversal(new Compiler(), null);
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.SCRIPT);
    
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.visit(t, n, parent);
    
    // Add assertions
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);
    
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);
    
    // Add assertions
  }

  @Test
  public void testAnnotateCalls() {
    Node n = new Node(Token.CALL);
    
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateCalls(n);
    
    // Add assertions
  }

  @Test
  public void testAnnotateDispatchers() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateDispatchers(n, parent);
    
    // Add assertions
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);
    Node key = new Node(Token.STRING);
    Node value = new Node(Token.FUNCTION);
    
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralKeyAnnotations(objlit, key, value);
    
    // Add assertions
  }
}