package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserRunner;
import java.io.IOException;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private ParserConfig parserConfig;
  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private ParserRunner parserRunner;
  private Logger logger;

  @Before
  public void setup() {
    parserConfig = mock(ParserConfig.class);
    compiler = mock(AbstractCompiler.class);
    sourceFile = mock(SourceFile.class);
    parserRunner = mock(ParserRunner.class);
    logger = mock(Logger.class);
  }

  @Test
  public void testGetAstRoot() {
    Node root = IR.script();
    when(parserRunner.parse(sourceFile, "", parserConfig, compiler.getErrorReporter(), logger))
        .thenReturn(new ParserRunner.ParseResult(root, null));

    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testClearAst() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    String sourceFileName = "test.js";
    when(sourceFile.getName()).thenReturn(sourceFileName);

    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals(sourceFileName, inputId.getResource());
  }

  @Test
  public void testGetSourceFile() {
    String sourceFileName = "test.js";
    when(sourceFile.getName()).thenReturn(sourceFileName);

    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);
    assertEquals(sourceFileName, sourceFile.getName());
  }

  @Test
  public void testSetSourceFile() {
    String testSourceFileName = "test.js";
    String anotherSourceFileName = "another.js";

    when(sourceFile.getName()).thenReturn(testSourceFileName);

    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = mock(SourceFile.class);
    when(newSourceFile.getName()).thenReturn(anotherSourceFileName);

    jsAst.setSourceFile(newSourceFile);

    assertEquals(anotherSourceFileName, jsAst.getSourceFile().getName());
  }

  @Test
  public void testParseWithNullCompiler() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(null);
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithIOException() {
    when(parserRunner.parse(sourceFile, "", parserConfig, compiler.getErrorReporter(), logger))
        .thenThrow(new IOException("Test IOException"));

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithNullParserConfig() {
    ParserConfig nullParserConfig = null;
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Preconditions.checkState(jsAst.getAstRoot(compiler) == null);
    when(parserConfig.getSourceOutputFile()).thenReturn(new File("output.js"));
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithNullErrorReporter() {
    when(compiler.getErrorReporter()).thenReturn(null);
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithNullLogger() {
    when(logger_).thenReturn(null);
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseWithSuccess() {
    Node root = IR.script();
    when(parserRunner.parse(sourceFile, "", parserConfig, compiler.getErrorReporter(), logger))
        .thenReturn(new ParserRunner.ParseResult(root, null));

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals(root, astRoot);
  }

  @Test
  public void testParseWithHaltingErrors() {
    when(parserRunner.parse(sourceFile, "", parserConfig, compiler.getErrorReporter(), logger))
        .thenReturn(new ParserRunner.ParseResult(null, new HaltingException("Test HaltingException")));

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }
}