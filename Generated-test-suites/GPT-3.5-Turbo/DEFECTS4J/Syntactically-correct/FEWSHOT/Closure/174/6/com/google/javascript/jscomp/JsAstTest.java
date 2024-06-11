package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SourceFile sourceFile = mock(SourceFile.class);
    InputId inputId = mock(InputId.class);

    when(sourceFile.getName()).thenReturn("test.js");
    when(inputId.getIdName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("var x = 5;");
    when(sourceFile.getInputId()).thenReturn(inputId);
    when(compiler.getParserConfig()).thenReturn(mock(Parser.Config.class));
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler);

    assertEquals("var x = 5;", jsAst.getAstRoot(compiler).toString());
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = mock(SourceFile.class);
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();

    assertEquals(null, jsAst.getAstRoot(null));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = mock(SourceFile.class);
    InputId inputId = mock(InputId.class);

    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getInputId()).thenReturn(inputId);

    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(inputId, jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = mock(SourceFile.class);

    JsAst jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile = mock(SourceFile.class);
    SourceFile newSourceFile = mock(SourceFile.class);

    when(sourceFile.getName()).thenReturn("test.js");
    when(newSourceFile.getName()).thenReturn("test.js");

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.setSourceFile(newSourceFile);

    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testParse() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SourceFile sourceFile = mock(SourceFile.class);
    InputId inputId = mock(InputId.class);
    Logger logger = mock(Logger.class);
    ParserRunner.ParseResult parseResult = mock(ParserRunner.ParseResult.class);
    Node ast = mock(Node.class);

    when(sourceFile.getName()).thenReturn("test.js");
    when(inputId.getIdName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenReturn("var x = 5;");
    when(sourceFile.getInputId()).thenReturn(inputId);
    when(compiler.getParserConfig()).thenReturn(mock(Parser.Config.class));
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));
    when(logger.getName()).thenReturn(JsAst.class.getName());
    when(parseResult.ast).thenReturn(ast);
    when(parseResult.oldAst).thenReturn(ast);

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    assertEquals(ast, jsAst.getAstRoot(compiler));
  }
}