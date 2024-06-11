package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.test.BaseTest;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class JsAstTest extends BaseTest {

  @Test
  public void testJsAst_GetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", getClass().getResourceAsStream("/test.js"));
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compiler = createCompiler();
    Node root = ast.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testJsAst_ClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", getClass().getResourceAsStream("/test.js"));
    JsAst ast = new JsAst(sourceFile);
    ast.clearAst();
    assertNull(ast.getAstRoot(compiler));
  }

  @Test
  public void testJsAst_Parse_Success() {
    SourceFile sourceFile = new SourceFile("test.js", getClass().getResourceAsStream("/test.js"));
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compiler = createCompiler();
    Node root = ast.getAstRoot(compiler);
    assertTrue(root.isTopLevel());
  }

  @Test(expected = IOException.class)
  public void testJsAst_Parse_IOException() {
    SourceFile sourceFile = new SourceFile("test.js", new byte[0][0]);
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compiler = createCompiler();
    ast.getAstRoot(compiler);
  }

  @Test
  public void testJsAst_Parse_ParserError() {
    SourceFile sourceFile = new SourceFile("test.invalid.js", getClass().getResourceAsStream("/test.invalid.js"));
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler compiler = createCompiler();
    Node root = ast.getAstRoot(compiler);
    assertTrue(root.isScript());
  }

  @Test
  public void testJsAst_SetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test1.js", getClass().getResourceAsStream("/test1.js"));
    JsAst ast = new JsAst(sourceFile1);
    SourceFile sourceFile2 = new SourceFile("test2.js", getClass().getResourceAsstream("/test2.js"));
    ast.setSourceFile(sourceFile2);
    assertEquals(sourceFile2.getName(), ast.getInputId().getName());
  }

  @Test
  public void testJsAst_GetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", getClass().getResourceAsStream("/test.js"));
    JsAst ast = new JsAst(sourceFile);
    assertEquals(sourceFile.getName(), ast.getInputId().getName());
  }

  @Test
  public void testJsAst_GetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", getClass().getResourceAsStream("/test.js"));
    JsAst ast = new JsAst(sourceFile);
    assertEquals(sourceFile, ast.getSourceFile());
  }
}