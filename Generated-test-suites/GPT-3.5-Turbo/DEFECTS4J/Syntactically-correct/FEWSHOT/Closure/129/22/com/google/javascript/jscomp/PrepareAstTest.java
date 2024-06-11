package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PrepareAstTest {
  
  private PrepareAst prepareAst;
  
  @Before
  public void setUp() {
    prepareAst = new PrepareAst(new AbstractCompiler());
  }

  @Test
  public void testProcess() {
    // Test with only externs
    Node externs = new Node(Token.EXTERN_FILE);
    Node root = null;
    prepareAst.process(externs, root);

    // Test with only root
    externs = null;
    root = new Node(Token.ROOT);
    prepareAst.process(externs, root);

    // Test with both externs and root
    externs = new Node(Token.EXTERN_FILE);
    root = new Node(Token.ROOT);
    prepareAst.process(externs, root);
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node node = new Node(Token.ROOT);
    prepareAst.normalizeNodeTypes(node);
  }

  @Test
  public void testNormalizeBlocks() {
    Node node = new Node(Token.IF);
    prepareAst.normalizeBlocks(node);
  }

  @Test
  public void testNormalizeBlocks_withSwitch() {
    Node node = new Node(Token.SWITCH);
    prepareAst.normalizeBlocks(node);
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);
    prepareAst.new PrepareAnnotations().normalizeObjectLiteralAnnotations(objlit);
  }

  @Test
  public void testAnnotateCalls() {
    Node call = new Node(Token.CALL);
    prepareAst.new PrepareAnnotations().annotateCalls(call);
  }

  @Test
  public void testAnnotateDispatchers() {
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(function);
    prepareAst.new PrepareAnnotations().annotateDispatchers(function, parent);
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);
    Node key = new Node(Token.STRING);
    Node value = new Node(Token.STRING);
    prepareAst.new PrepareAnnotations().normalizeObjectLiteralKeyAnnotations(objlit, key, value);
  }
  
}