package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserRunner;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    parserConfig = mock(ParserConfig.class);
    sourceFile = mock(SourceFile.class);
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetInputId() {
    InputId expectedInputId = new InputId("test.js");
    when(sourceFile.getName()).thenReturn("test.js");
    assertThat(jsAst.getInputId()).isEqualTo(expectedInputId);
  }

  @Test
  public void testGetSourceFile() {
    assertThat(jsAst.getSourceFile()).isSameInstanceAs(sourceFile);
  }

  @Test
  public void testClearAst() {
    Node oldAst = mock(Node.class);
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("test code");

    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);

    jsAst.clearAst();
    Node clearedRoot = jsAst.getAstRoot(compiler);

    assertThat(clearedRoot).isNull();
    when(sourceFile.clearCachedSource()).thenCallRealMethod();
    sourceFile.clearCachedSource();
    Preconditions.checkState(sourceFile.isEmpty());
  }

  @Test
  public void testGetAstRoot() {
    Node root = mock(Node.class);
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(parserConfig.getSourceName()).thenReturn(new SourceName("test.js"));
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("test code");

    jsAst.parse(compiler);
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig, compiler.getDefaultErrorReporter(), logger_)).thenReturn(new ParserRunner.ParseResult(root, null));

    Node expectedAstRoot = root;
    Node actualAstRoot = jsAst.getAstRoot(compiler);

    assertThat(actualAstRoot).isSameInstanceAs(expectedAstRoot);
  }

  @Test
  public void testParseWithIOException() {
    InputId inputId = new InputId("test.js");
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("test code");

    jsAst.parse(compiler);
    Preconditions.checkArgument(jsAst.getAstRoot(compiler) == null);
  }

  @Test
  public void testParseWithNullRoot() {
    InputId inputId = new InputId("test.js");
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("test code");

    jsAst.parse(compiler);
    Node expectedRoot = IR.script();
    Node actualRoot = jsAst.getAstRoot(compiler);

    assertThat(actualRoot).isSameInstanceAs(expectedRoot);
  }
}