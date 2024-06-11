package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JsCompTestCase;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.io.IOException;

public class JsAstTest extends JsCompTestCase {

  private static final String TEST_FILE = "testJsAst.js";
  private static final String TEST_CODE = "// Test JsAst class";

  @org.junit.Before
  public void setUp() throws Exception {
    initCompiler();
  }

  @org.junit.After
  public void tearDown() throws Exception {
    clearCompilerState();
  }

  /**
   * Tests the constructor of JsAst class.
   * @throws IOException
   */
  @org.junit.Test
  public void testJsAstConstructor() throws IOException {
    SourceFile sourceFile = new SourceFile(TEST_FILE, TEST_CODE);
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getInputId());
    assertNotNull(jsAst.getSourceFile());
  }

  /**
   * Tests the getAstRoot method when there is a valid AST.
   * @throws IOException
   */
  @org.junit.Test
  public void testGetAstRootWithValidAST() throws IOException {
    SourceFile sourceFile = new SourceFile(TEST_FILE, TEST_CODE);
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = getCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  /**
   * Tests the getAstRoot method when there is no AST.
   * @throws IOException
   */
  @org.junit.Test
  public void testGetAstRootWithNoAST() throws IOException {
    SourceFile sourceFile = new SourceFile(TEST_FILE, TEST_CODE);
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = getCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  /**
   * Tests the parse method when there is a valid parse result.
   * @throws IOException
   */
  @org.junit.Test
  public void testParseWithValidParseResult() throws IOException {
    SourceFile sourceFile = new SourceFile(TEST_FILE, TEST_CODE);
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = getCompiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertTrue(astRoot.getNodeType() == Node.SCRIPT_NODE);
  }

  /**
   * Tests the parse method when there is a parse error.
   * @throws IOException
   */
  @org.junit.Test
  public void testParseWithParseError() throws IOException {
    SourceFile sourceFile = new SourceFile(TEST_FILE, TEST_CODE + "\n" + "invalid code");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = getCompiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertTrue(astRoot.getNodeType() == Node.BLOCK_NODE);
  }

  /**
   * Tests the clearAst method.
   * @throws IOException
   */
  @org.junit.Test
  public void testClearAst() throws IOException {
    SourceFile sourceFile = new SourceFile(TEST_FILE, TEST_CODE);
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = getCompiler();
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node clearedAstRoot = jsAst.getAstRoot(compiler);
    assertNull(clearedAstRoot);
  }

  /**
   * Tests the getInputId method.
   * @throws IOException
   */
  @org.junit.Test
  public void testGetInputId() throws IOException {
    SourceFile sourceFile = new SourceFile(TEST_FILE, TEST_CODE);
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals(new InputId(TEST_FILE), inputId);
  }

  /**
   * Tests the getSourceFile method.
   * @throws IOException
   */
  @org.junit.Test
  public void testGetSourceFile() throws IOException {
    SourceFile sourceFile = new SourceFile(TEST_FILE, TEST_CODE);
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile getSourceFile = jsAst.getSourceFile();
    assertEquals(sourceFile, getSourceFile);
  }

  /**
   * Tests the setSourceFile method.
   * @throws IOException
   */
  @org.junit.Test
  public void testSetSourceFile() throws IOException {
    SourceFile sourceFile = new SourceFile(TEST_FILE, TEST_CODE);
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newTestFile.js", "newTestCode");
    jsAst.setSourceFile(newSourceFile);
    SourceFile getSourceFile = jsAst.getSourceFile();
    assertEquals(newSourceFile, getSourceFile);
  }
}