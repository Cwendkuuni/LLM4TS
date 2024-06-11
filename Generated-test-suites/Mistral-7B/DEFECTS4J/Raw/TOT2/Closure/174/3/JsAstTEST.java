package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsAstTest {

  @Test
  public void testJsAst_GetAstRoot() {
    // Given
    SourceFile sourceFile = new SourceFile("test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);

    // When
    Node astRoot = jsAst.getAstRoot(compiler);

    // Then
    assertNotNull(astRoot);
  }

  @Test
  public void testJsAst_Parse_EmptySourceFile() {
    // Given
    SourceFile sourceFile = new SourceFile("test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);

    // When
    jsAst.parse(compiler);

    // Then
    assertNotNull(jsAst.root);
    assertTrue(jsAst.root.isScript());
  }

  @Test
  public void testJsAst_Parse_InvalidSourceFile() {
    // Given
    String testSource = "function foo() { return 1; } /*comment*/ var x = 2;";
    SourceFile sourceFile = new SourceFile("test.js", testSource);
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);

    // When & Then
    try {
      jsAst.parse(compiler);
      fail();
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testJsAst_Parse_IOException() {
    // Given
    SourceFile sourceFile = new SourceFile("test.js");
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void report(JSError error) {
        // Do nothing
      }
    };
    JsAst jsAst = new JsAst(sourceFile);

    // When & Then
    try {
      jsAst.parse(compiler);
      fail();
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testJsAst_ClearAst() {
    // Given
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = IR.script();
    jsAst.root = root;

    // When
    jsAst.clearAst();

    // Then
    assertNull(jsAst.root);
  }

  @Test
  public void testJsAst_SetSourceFile() {
    // Given
    SourceFile sourceFile1 = new SourceFile("test1.js");
    JsAst jsAst = new JsAst(sourceFile1);
    SourceFile sourceFile2 = new SourceFile("test2.js");

    // When
    jsAst.setSourceFile(sourceFile2);

    // Then
    assertEquals(sourceFile2, jsAst.sourceFile);
  }

  // Additional test cases can be added here
}