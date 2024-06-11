package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.AbstractCompiler;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.parser.ErrorReporterImpl;
import com.google.javascript.rhino.parser.ParserConfigImpl;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Tests for {@link JsAst}.
 */
public class JsAstTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private ErrorReporter errorReporter;
  @Mock
  private ParserConfig parserConfig;
  @Mock
  private SourceFile sourceFile;

  @InjectMocks
  private JsAst jsAst;

  @Before
  public void setup() {
    when(compiler.getDefaultErrorReporter()).thenReturn(errorReporter);
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(parserConfig.toParserConfig()).thenReturn(new ParserConfigImpl());
  }

  @Test
  public void testGetAstRoot() {
    Node ast = IR.script();
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("test code".getBytes());

    Preconditions.checkState(jsAst.getInputId().equals(new InputId("test.js")));
    Preconditions.checkState(jsAst.getSourceFile().equals(sourceFile));

   JsAst jsAstSpy = mock(JsAst.class);
    when(jsAstSpy.getAstRoot(compiler)).thenReturn(ast);

    Node expected = jsAstSpy.getAstRoot(compiler);
    Node actual = jsAst.getAstRoot(compiler);

    checkState(expected == actual);
  }

  @Test
  public void testClearAst() {
    Node ast = IR.script();
    jsAst.parse(compiler);

    jsAst.clearAst();

    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    when(sourceFile.getName()).thenReturn("test.js");
    Preconditions.checkState(jsAst.getInputId().equals(new InputId("test.js")));
  }

  @Test
  public void testGetSourceFile() {
    when(sourceFile.getName()).thenReturn("test.js");
    Preconditions.checkState(jsAst.getSourceFile().equals(sourceFile));
  }

  @Test
  public void testSetSourceFile() {
    String fileName = "test.js";
    SourceFile sourceFileMock = mock(SourceFile.class);
    when(sourceFileMock.getName()).thenReturn(fileName);

    jsAst.setSourceFile(sourceFileMock);

    Preconditions.checkState(jsAst.getInputId().equals(new InputId(fileName)));
    Preconditions.checkState(jsAst.getSourceFile().equals(sourceFileMock));
    checkState(fileName.equals(jsAst.fileName));
  }

  @Test
  public void testParse() throws IOException {
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("test code".getBytes());

    ParserRunner parserRunner = mock(ParserRunner.class);
    ParserRunner.ParseResult result = new ParserRunner.ParseResult();
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig, errorReporter, logger_))
        .thenReturn(result);

    Node expected = result.ast;
    Node actual = jsAst.parse(compiler);

    checkState(expected == actual);
  }
}