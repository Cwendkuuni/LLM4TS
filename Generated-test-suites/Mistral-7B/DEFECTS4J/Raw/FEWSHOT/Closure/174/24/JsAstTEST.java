package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.AbstractErrorReporter;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Logger;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserRunner;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private AbstractErrorReporter errorReporter;
  private Logger logger;
  private ParserConfig parserConfig;
  private SourceFile sourceFile;
  private ParserRunner parserRunner;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    errorReporter = mock(AbstractErrorReporter.class);
    logger = Logger.getLogger("JsAstTest");
    parserConfig = new ParserConfig();
    sourceFile = new SourceFile("test.js");
    parserRunner = new ParserRunner();
  }

  @Test
  public void testParseWithNullSourceFile() {
    JsAst jsAst = new JsAst(null);
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithEmptySourceFile() {
    when(sourceFile.getCode()).thenReturn("");

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithValidSourceFile() {
    when(sourceFile.getCode()).thenReturn("function foo() {}");

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertNotNull(root.getChildren());
  }

  @Test
  public void testClearAst() {
    when(sourceFile.getCode()).thenReturn("function foo() {}");
    Node root = IR.script();
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    jsAst.setAstRoot(root);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    JsAst jsAst = new JsAst(sourceFile);
    InputId expectedId = new InputId("test.js");
    InputId actualId = jsAst.getInputId();
    Preconditions.checkState(expectedId.equals(actualId));
  }

  @Test
  public void testSetSourceFile() {
    JsAst jsAst = new JsAst(new SourceFile("test.js"));
    SourceFile newSourceFile = new SourceFile("newTest.js");
    jsAst.setSourceFile(newSourceFile);
    SourceFile actualSourceFile = jsAst.getSourceFile();
    Preconditions.checkState(actualSourceFile.equals(newSourceFile));
  }
}