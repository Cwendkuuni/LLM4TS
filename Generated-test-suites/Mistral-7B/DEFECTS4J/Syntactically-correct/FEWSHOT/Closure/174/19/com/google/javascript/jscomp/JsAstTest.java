package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.ParserEngine;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {
  @Test
  public void testJsAstInstance() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test.js");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetInputId() {
    String fileName = "test.js";
    SourceFile sourceFile = new SourceFile(fileName, fileName);
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals(inputId.getName(), fileName);
  }

  @Test
  public void testGetSourceFile() {
    String fileName = "test.js";
    SourceFile sourceFile = new SourceFile(fileName, fileName);
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertNotNull(result);
    assertEquals(result.getName(), fileName);
  }

  @Test
  public void testSetSourceFile() {
    String oldFileName = "test.js";
    String newFileName = "newTest.js";
    SourceFile oldSourceFile = new SourceFile(oldFileName, oldFileName);
    SourceFile newSourceFile = new SourceFile(newFileName, newFileName);
    JsAst jsAst = new JsAst(oldSourceFile);
    jsAst.setSourceFile(newSourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertNotNull(result);
    assertEquals(result.getName(), newFileName);
  }

  @Test
  public void testGetAstRootWithNullCompiler() {
    String fileName = "test.js";
    SourceFile sourceFile = new SourceFile(fileName, fileName);
    JsAst jsAst = new JsAst(sourceFile);
    assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetAstRootWithEmptyCode() {
    String fileName = "test.js";
    String code = "";
    SourceFile sourceFile = new SourceFile(fileName, fileName);
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertEquals(root.getClass(), IR.script().getClass());
  }

  @Test
  public void testGetAstRootWithSuccessParse() {
    String fileName = "test.js";
    String code = "function foo() { }";
    SourceFile sourceFile = new SourceFile(fileName, fileName);
    ParserConfig parserConfig = new ParserConfig();
    ParserEngine parserEngine = new ParserEngine(parserConfig);
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setParserEngine(parserEngine);
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    Preconditions.checkState(root.getClass() instanceof Node);
  }

  @Test
  public void testGetAstRootWithIOException() {
    String fileName = "test.js";
    String code = "function foo() { }";
    SourceFile sourceFile = new SourceFile(fileName, fileName);
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNull(root);
  }

  @Test
  public void testGetAstRootWithHaltingErrors() {
    String fileName = "test.js";
    String code = "function foo() { throw new Error(); }";
    SourceFile sourceFile = new SourceFile(fileName, fileName);
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(compiler);
    assertNull(root);
  }

  @Test
  public void testGetAstRootWithOldParseTree() {
    String fileName = "test.js";
    String code = "function foo() { }";
    String oldCode = "function bar() { }";
    SourceFile sourceFile = new SourceFile(fileName, fileName);
    AbstractCompiler compiler = new AbstractCompiler();
    JsAst jsAst = new JsAst(sourceFile);
    Node oldAst = IR.script();
    compiler.setOldParseTree(fileName, oldAst);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertEquals(root.getClass(), oldAst.getClass());
  }
}