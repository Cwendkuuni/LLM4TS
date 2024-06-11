package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() throws Exception {
    AbstractCompiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    Node expectedRoot = IR.script();
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertEquals(expectedRoot, actualRoot);
  }

  @Test
  public void testGetAstRootWithNullCompiler() throws Exception {
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    Assert.assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetAstRootWithNullSourceFile() throws Exception {
    AbstractCompiler compiler = new Compiler();
    JsAst jsAst = new JsAst(null);
    Assert.assertNull(jsAst.getAstRoot(compiler));
  }

  @Test(expected = IOException.class)
  public void testGetAstRootWithIOException() throws Exception {
    AbstractCompiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        throw new IOException("Test IOException");
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler);
  }

  @Test(expected = AssertionError.class)
  public void testGetAstRootWithHaltingErrors() throws Exception {
    AbstractCompiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        throw new AssertionError("Test Halting Error");
      }
    };
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler);
  }

  @Test
  public void testGetAstRootWithEmptyFile() throws Exception {
    AbstractCompiler compiler = new Compiler();
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);
    Node expectedRoot = IR.script();
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertEquals(expectedRoot, actualRoot);
  }
}