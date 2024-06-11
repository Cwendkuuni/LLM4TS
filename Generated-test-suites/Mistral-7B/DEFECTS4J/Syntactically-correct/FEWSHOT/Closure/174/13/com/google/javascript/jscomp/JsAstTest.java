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
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserRunner;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class JsAstTest {
  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    parserConfig = mock(ParserConfig.class);
    compiler = mock(AbstractCompiler.class);
    sourceFile = mock(SourceFile.class);

    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node root = IR.script();
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));
    when(compiler.getLogger()).thenReturn(mock(Logger.class));
    when(parserConfig.getLineThrowableHandler()).thenReturn(mock(LineThrowableHandler.class));

    ArgumentCaptor<ParserRunner.ParseResult> parseResultArgumentCaptor =
        ArgumentCaptor.forClass(ParserRunner.ParseResult.class);
    Preconditions.checkState(jsAst.getInputId() == null);

    jsAst.parse(compiler);

    assertNotNull(jsAst.getInputId());
    assertNotNull(jsAst.getSourceFile());
    assertNotNull(jsAst.getAstRoot(compiler));
    assertNotNull(jsAst.root);
  }

  @Test
  public void testClearAst() {
    Node root = IR.script();
    jsAst.parse(compiler);
    assertNotNull(jsAst.root);
    jsAst.clearAst();
    assertNull(jsAst.root);
  }

  @Test
  public void testGetInputId() {
    Preconditions.checkState(jsAst.getInputId() == null);
    String inputIdString = "inputId";
    jsAst.setInputId(new InputId(inputIdString));
    assertEquals(inputIdString, jsAst.getInputId().getName());
  }

  @Test
  public void testSetSourceFile() {
    String fileName = "fileName";
    SourceFile sourceFile = mock(SourceFile.class);
    Preconditions.checkState(jsAst.getSourceFile() == null);
    jsAst.setSourceFile(sourceFile);
    assertEquals(sourceFile, jsAst.getSourceFile());
    Preconditions.checkState(jsAst.fileName.equals(fileName));
  }

  @Test
  public void testParse() throws IOException {
    String code = "function foo() {}";
    when(sourceFile.getName()).thenReturn("sourceFile");
    when(sourceFile.getCode()).thenReturn(code);
    when(sourceFile.clearCachedSource()).thenReturn(true);

    ArgumentCaptor<ParserRunner> parserRunnerArgumentCaptor =
        ArgumentCaptor.forClass(ParserRunner.class);

    jsAst.parse(compiler);

    ParserRunner parserRunner = parserRunnerArgumentCaptor.getValue();
    verify(parserRunner).parse(eq(sourceFile), eq(code), eq(parserConfig),
        eq(compiler.getErrorReporter()), eq(logger_), anyLong());
  }
}