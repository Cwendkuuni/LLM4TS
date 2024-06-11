package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserRunner;
import com.google.javascript.rhino.SourceName;

/**
 * Tests for {@link JsAst} class.
 */
public class JsAstTest {
  private static final String TEST_FILE_NAME = "test.js";
  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    parserConfig = mock(ParserConfig.class);
    compiler = mock(AbstractCompiler.class);
    sourceFile = new SourceFile(TEST_FILE_NAME, TEST_FILE_NAME);
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node expectedAst = IR.script();
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));
    when(compiler.getLogger()).thenReturn(mock(Logger.class));

    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));

    // Check that the old parse tree is set correctly
    Node oldAst = IR.block(IR.text("old parse tree"));
    when(compiler.getOldParseTree(TEST_FILE_NAME)).thenReturn(oldAst);
    Node actualAst = jsAst.getAstRoot(compiler);
    assertNotNull(actualAst);
    assertEquals(expectedAst, actualAst);
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
    assertNotNull(jsAst.getInputId());
    assertEquals(expectedInputId, jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    assertNotNull(jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newFileName", TEST_FILE_NAME);
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }
}