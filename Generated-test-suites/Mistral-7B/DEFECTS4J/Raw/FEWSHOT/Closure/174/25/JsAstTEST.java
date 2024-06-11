package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
  private static final String TEST_FILE_NAME = "testFile.js";
  private static final String TEST_FILE_CODE = "// Test file code.";

  private AbstractCompiler compiler;
  private ParserRunner parserRunner;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    parserRunner = mock(ParserRunner.class);
    sourceFile = new SourceFile(TEST_FILE_NAME, TEST_FILE_CODE);
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node root = IR.script();
    when(parserRunner.parse(sourceFile, TEST_FILE_CODE, null, compiler.getParserConfig(),
        mock(Logger.class), null)).thenReturn(new ParserRunner.ParseResult(root, null));

    Node expectedAstRoot = root;
    Node actualAstRoot = jsAst.getAstRoot(compiler);
    assertThat(actualAstRoot).isEqualTo(expectedAstRoot);
  }

  @Test
  public void testClearAst() {
    Node root = IR.script();
    jsAst.parse(compiler);
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    InputId expectedInputId = new InputId(TEST_FILE_NAME);
    assertThat(jsAst.getInputId()).isEqualTo(expectedInputId);
  }

  @Test
  public void testGetSourceFile() {
    assertThat(jsAst.getSourceFile()).isSameInstanceAs(sourceFile);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTestFile.js", TEST_FILE_CODE);
    jsAst.setSourceFile(newSourceFile);
    assertThat(jsAst.getSourceFile()).isSameInstanceAs(newSourceFile);
  }

  @Test
  public void testParse_Success() {
    Node root = IR.script();
    when(parserRunner.parse(sourceFile, TEST_FILE_CODE, null, compiler.getParserConfig(),
        mock(Logger.class), null)).thenReturn(new ParserRunner.ParseResult(root, null));

    jsAst.parse(compiler);
    Node actualAstRoot = jsAst.getAstRoot(compiler);
    Node expectedAstRoot = root;
    assertThat(actualAstRoot).isEqualTo(expectedAstRoot);
  }

  @Test
  public void testParse_Failure() {
    jsAst.parse(compiler);
    Node actualAstRoot = jsAst.getAstRoot(compiler);
    Node expectedAstRoot = IR.script();
    assertThat(actualAstRoot).isEqualTo(expectedAstRoot);
  }
}