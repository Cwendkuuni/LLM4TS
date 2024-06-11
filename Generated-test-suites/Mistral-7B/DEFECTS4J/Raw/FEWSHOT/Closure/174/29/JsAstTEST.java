package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompilerOptions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

/**
 * Tests for JsAst class
 */
public class JsAstTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = new Compiler(new AbstractCompilerOptions());
    SourceFile sourceFile = new SourceFile("test.js", "test");
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    AbstractCompiler compiler = new Compiler(new AbstractCompilerOptions());
    SourceFile sourceFile = new SourceFile("test.js", "test");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler); // parse the file
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    AbstractCompiler compiler = new Compiler(new AbstractCompilerOptions());
    SourceFile sourceFile = new SourceFile("test.js", "test");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals("test.js", inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    AbstractCompiler compiler = new Compiler(new AbstractCompilerOptions());
    SourceFile sourceFile = new SourceFile("test.js", "test");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile result = jsAst.getSourceFile();
    assertEquals("test.js", result.getName());
  }

  @Test
  public void testSetSourceFile() {
    AbstractCompiler compiler = new Compiler(new AbstractCompilerOptions());
    SourceFile sourceFile1 = new SourceFile("test1.js", "test1");
    JsAst jsAst = new JsAst(sourceFile1);
    SourceFile sourceFile2 = new SourceFile("test2.js", "test2");
    jsAst.setSourceFile(sourceFile2);
    assertEquals("test2.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testParse_success() {
    AbstractCompiler compiler = new Compiler(new AbstractCompilerOptions());
    SourceFile sourceFile = new SourceFile("test.js", "test");
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testParse_failure() {
    AbstractCompiler compiler = new Compiler(new AbstractCompilerOptions());
    SourceFile sourceFile = new SourceFile("test.js", "test");
    JsAst jsAst = new JsAst(sourceFile);
    expectedEx.expect(RuntimeException.class);
    jsAst.getAstRoot(compiler);
  }

  @Test
  public void testParse_IOException() {
    AbstractCompiler compiler = new Compiler(new AbstractCompilerOptions());
    SourceFile sourceFile = new SourceFile("test.js", "test");
    JsAst jsAst = new JsAst(sourceFile);
    expectedEx.expect(IOException.class);
    jsAst.parse(compiler);
  }

  @Test
  public void testPrepareAst() {
    AbstractCompiler compiler = new Compiler(new AbstractCompilerOptions());
    SourceFile sourceFile = new SourceFile("test.js", "test");
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    jsAst.parse(compiler);
    assertNotNull(astRoot.getFirstChild());
  }

}