package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.parser.ErrorReporter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class JsAstTest {
  private static final String FILE_NAME = "testFile.js";
  private AbstractCompiler compiler;
  private Node root;
  private ParserConfig parserConfig;
  private ParserRunner parserRunner;
  private ErrorReporter errorReporter;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setup() {
    parserConfig = mock(ParserConfig.class);
    parserRunner = mock(ParserRunner.class);
    errorReporter = mock(ErrorReporter.class);
    sourceFile = mock(SourceFile.class);
    when(sourceFile.getName()).thenReturn(FILE_NAME);
    when(sourceFile.getCode()).thenReturn("test code".getBytes());
    when(sourceFile.clearCachedSource()).thenReturn(true);

    compiler = mock(AbstractCompiler.class);
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(errorReporter);
    when(compiler.getLogger()).thenReturn(Logger.getLogger(""));

    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    assertNotNull(inputId);
    assertEquals(FILE_NAME, inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = jsAst.getSourceFile();
    assertNotNull(sourceFile);
    assertEquals(FILE_NAME, sourceFile.getName());
  }

  @Test
  public void testGetAstRoot() {
    jsAst.parse(compiler);
    Node expectedAst = IR.script();
    ArgumentCaptor<Node> astCaptor = ArgumentCaptor.forClass(Node.class);
    when(compiler.prepareAst(eq(root))).thenReturn(expectedAst);
    Node ast = jsAst.getAstRoot(compiler);
    assertNotNull(ast);
    assertEquals(expectedAst, ast);
  }

  @Test
  public void testClearAst() {
    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    jsAst.clearAst();
    Node oldAst = when(compiler.getOldParseTree(FILE_NAME)).thenReturn(IR.script()).getMock();
    assertNull(oldAst);
    Node ast = jsAst.getAstRoot(compiler);
    assertNull(ast);
  }

  @Test
  public void testParse_success() {
    jsAst = new JsAst(sourceFile);
    when(parserRunner.parse(eq(sourceFile), anyByteArray(), eq(parserConfig), eq(errorReporter), any(Logger.class)))
        .thenReturn(new ParserRunner.ParseResult(IR.script(), null));

    jsAst.parse(compiler);
    Node expectedAst = IR.script();
    ArgumentCaptor<Node> astCaptor = ArgumentCaptor.forClass(Node.class);
    when(compiler.prepareAst(eq(root))).thenReturn(expectedAst);
    Node ast = jsAst.getAstRoot(compiler);
    assertNotNull(ast);
    assertEquals(expectedAst, ast);
  }

  @Test
  public void testParse_failure() {
    jsAst = new JsAst(sourceFile);
    when(parserRunner.parse(eq(sourceFile), anyByteArray(), eq(parserConfig), eq(errorReporter), any(Logger.class)))
        .thenThrow(new IOException());

    jsAst.parse(compiler);
    Node expectedAst = IR.script();
    ArgumentCaptor<Node> astCaptor = ArgumentCaptor.forClass(Node.class);
    when(compiler.prepareAst(eq(root))).thenReturn(expectedAst);
    Node ast = jsAst.getAstRoot(compiler);
    assertNotNull(ast);
    assertEquals(expectedAst, ast);
  }
}