package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Logger;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserRunner;

public class JsAstTest {
  private SourceFile sourceFile;
  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private ErrorReporter errorReporter;
  private Logger logger;

  @Before
  public void setup() {
    sourceFile = mock(SourceFile.class);
    compiler = mock(AbstractCompiler.class);
    parserConfig = mock(ParserConfig.class);
    errorReporter = mock(ErrorReporter.class);
    logger = mock(Logger.class);
  }

  @Test
  public void testParseSuccess() {
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("function test() {}".getBytes());

    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(errorReporter);
    when(compiler.getLogger()).thenReturn(logger);

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseFailureWithIOException() {
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenThrow(new IOException("File not found"));

    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(errorReporter);
    when(compiler.getLogger()).thenReturn(logger);

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    // The parse method should have set a dummy block as the root in case of error
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertNotNull(root.getFirstChild());
  }

  @Test
  public void testParseFailureWithError() {
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("function test() { SyntaxError('Error'); }".getBytes());

    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(errorReporter);
    when(compiler.getLogger()).thenReturn(logger);

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    // The parse method should have set a dummy block as the root in case of error
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertNotNull(root.getFirstChild());

    // The compiler should have reported an error
    Preconditions.checkState(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testClearAst() {
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCachedSource()).thenReturn("function test() {}");

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    jsAst.clearAst();

    // The cached source should have been cleared
    assertNull(sourceFile.getCachedSource());
  }
}