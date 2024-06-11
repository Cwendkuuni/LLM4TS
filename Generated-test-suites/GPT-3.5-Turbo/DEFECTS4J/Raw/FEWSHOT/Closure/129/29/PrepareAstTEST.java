package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;


public class PrepareAstTest {
  
  @Test
  public void testNormalizeBlocks() {
    Node n = new Node(Token.BLOCK);
    n.addChildToFront(new Node(Token.IF));
    n.getFirstChild().addChildToFront(new Node(Token.BLOCK));
    
    PrepareAst pa = new PrepareAst(new AbstractCompiler());
    pa.normalizeBlocks(n);
    
    assertEquals(2, n.getFirstChild().getChildren().size());
    assertTrue(n.getFirstChild().getFirstChild().isBlock());
  }
  
  @Test
  public void testNormalizeNodeTypes() {
    Node n = new Node(Token.BLOCK);
    n.addChildToFront(new Node(Token.EXPR_VOID));
    n.getFirstChild().addChildToFront(new Node(Token.NUMBER));
    
    PrepareAst pa = new PrepareAst(new AbstractCompiler());
    pa.normalizeNodeTypes(n);
    
    assertEquals(Token.EXPR_RESULT, n.getFirstChild().getToken());
  }
  
  @Test
  public void testPrepareAnnotations_normalizeObjectLiteralAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);
    objlit.addChildToFront(new Node(Token.KEYVALUE));
    objlit.getFirstChild().addChildToFront(new Node(Token.STRING));
    
    PrepareAst.PrepareAnnotations pa = new PrepareAst.PrepareAnnotations();
    pa.normalizeObjectLiteralAnnotations(objlit);
    
    assertEquals(1, objlit.getFirstChild().getFirstChild().getChildren().size());
    assertNull(objlit.getFirstChild().getFirstChild().getJSDocInfo());
    assertEquals(Token.STRING, objlit.getFirstChild().getFirstChild().getToken());
  }
  
  @Test
  public void testPrepareAnnotations_annotateCalls() {
    Node n = new Node(Token.CALL);
    n.addChildToFront(new Node(Token.NAME, new String("eval")));
    n.getFirstChild().putBooleanProp(Node.DIRECT_EVAL, true);
    
    PrepareAst.PrepareAnnotations pa = new PrepareAst.PrepareAnnotations();
    pa.annotateCalls(n);
    
    assertTrue(n.getBooleanProp(Node.DIRECT_EVAL));
    assertFalse(n.getBooleanProp(Node.FREE_CALL));
  }
  
  @Test
  public void testPrepareAnnotations_annotateDispatchers() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.ASSIGN));
    
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(new Node(Token.ASSIGN));
    parent.getFirstChild().addChildToFront(new Node(Token.FUNCTION, new Node(Token.NAME, new String("test"))));
    parent.getFirstChild().replaceChild(n, parent.getFirstChild().getFirstChild());
    
    PrepareAst.PrepareAnnotations pa = new PrepareAst.PrepareAnnotations();
    pa.annotateDispatchers(n, parent);
    
    assertTrue(n.getBooleanProp(Node.IS_DISPATCHER));
  }
  
  @Test
  public void testPrepareAnnotations_visit() {
    Node n = new Node(Token.FUNCTION);
    
    NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null, null);
    PrepareAst.PrepareAnnotations pa = new PrepareAst.PrepareAnnotations();
    pa.visit(t, n, null);
    
    assertFalse(n.getBooleanProp(Node.IS_DISPATCHER));
  }
  
}