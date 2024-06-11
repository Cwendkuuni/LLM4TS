package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParsingTestBase;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class JsAstTest extends ParsingTestBase {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() throws Exception {
    compiler = mock(AbstractCompiler.class);
    sourceFile = new SourceFile("test.js", "test content", getClass());
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRootNull() {
    Preconditions.checkState(jsAst.getAstRoot(compiler) == null);
  }

  @Test
  public void testGetAstRootNotParsed() {
    when(compiler.getParserConfig()).thenReturn(new ParserConfig());
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertThat(root).isNotNull();
  }

  @Test
  public void testGetAstRootParsed() {
    when(compiler.getParserConfig()).thenReturn(new ParserConfig());
    Node expectedRoot = parser().parseProgram(sourceFile.getCode());
    jsAst.parse(compiler);
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertThat(actualRoot).isEqualTo(expectedRoot);
  }

  @Test
  public void testClearAst() {
    Node expectedRoot = parser().parseProgram(sourceFile.getCode());
    jsAst.parse(compiler);
    jsAst.clearAst();
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertThat(actualRoot).isNull();
  }

  @Test
  public void testGetInputId() {
    InputId expectedId = new InputId("test.js");
    InputId actualId = jsAst.getInputId();
    assertThat(actualId).isEqualTo(expectedId);
  }

  @Test
  public void testGetSourceFile() {
    SourceFile expectedSourceFile = sourceFile;
    SourceFile actualSourceFile = jsAst.getSourceFile();
    assertThat(actualSourceFile).isEqualTo(expectedSourceFile);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newFile.js", "new content", getClass());
    jsAst.setSourceFile(newSourceFile);
    SourceFile actualSourceFile = jsAst.getSourceFile();
    assertThat(actualSourceFile).isEqualTo(newSourceFile);
  }

  @Test
  public void testParseIOException() {
    when(compiler.getParserConfig()).thenReturn(new ParserConfig());
    when(compiler.getDefaultErrorReporter()).thenReturn(new TestErrorReporter());
    ArgumentCaptor<JSError> errorCaptor = ArgumentCaptor.forClass(JSError.class);
    compiler.report(errorCaptor.capture());
    jsAst.parse(compiler);
    JSError actualError = errorCaptor.getValue();
    JSError expectedError = JSError.make(AbstractCompiler.READ_ERROR, sourceFile.getName());
    assertThat(actualError).isEqualTo(expectedError);
  }

  @Test
  public void testParseNullAst() {
    when(compiler.getParserConfig()).thenReturn(new ParserConfig());
    when(compiler.getDefaultErrorReporter()).thenReturn(new TestErrorReporter());
    jsAst.parse(compiler);
    Node expectedRoot = IR.script();
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertThat(actualRoot).isEqualTo(expectedRoot);
  }
}

class TestErrorReporter extends AbstractCompiler.ErrorReporter {
  @Override
  public void report(JSError error) {
    // Do nothing
  }
}