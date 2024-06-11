package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new TestSourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new TestSourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNotNull(jsAst.getSourceFile());
  }

  @Test(expected = AssertionError.class)
  public void testParseError() {
    SourceFile sourceFile = new TestSourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    jsAst.parse(compiler);
  }

  @Test
  public void testIOException() {
    SourceFile sourceFile = new TestSourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.report(
        JSError.make(AbstractCompiler.READ_ERROR, sourceFile.getName()));
    assertTrue(jsAst.getAstRoot(compiler) == null);
  }

  @Test
  public void testParseValidCode() {
    SourceFile sourceFile = new TestSourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertTrue(astRoot instanceof IR.Script);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new TestSourceFile("test1.js");
    JsAst jsAst = new JsAst(sourceFile1);
    SourceFile sourceFile2 = new TestSourceFile("test2.js");
    jsAst.setSourceFile(sourceFile2);
    assertTrue(sourceFile1.getName().equals(jsAst.getInputId().getName()));
    assertTrue(sourceFile2.getName().equals(jsAst.getSourceFile().getName()));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new TestSourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertTrue(inputId.getName().equals(sourceFile.getName()));
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new TestSourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertTrue(sourceFile.getName().equals(result.getName()));
  }
}