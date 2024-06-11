package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Logging;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {
  private static final Logger logger = Logging.getLogger(JsAstTest.class.getName());
  private SourceFile sourceFile;
  private AbstractCompiler compiler;
  private JsAst jsAst;
  private ParserConfig parserConfig;

  @Before
  public void setUp() {
    parserConfig = new ParserConfig();
    compiler = new AbstractCompiler() {
      @Override
      public ParserConfig getParserConfig() {
        return parserConfig;
      }

      @Override
      public Node parse(String code, SourceName sourceName, int options,
          ErrorReporter errorReporter) throws IOException {
        return null;
      }

      @Override
      public void dumpAST(Node root, SourceName sourceName, int options) {
        // Do nothing
      }

      @Override
      public void report(JSError error) {
        // Do nothing
      }

      @Override
      public void report(JSError error, int line, int column) {
        // Do nothing
      }

      @Override
      public Node setOldParseTree(String name, Node node) {
        return null;
      }
    };
    sourceFile = new SourceFile("test.js", "test content");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetInputId() {
    assertEquals(sourceFile.getName(), jsAst.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("new_test.js", "new test content");
    jsAst.setSourceFile(newSourceFile);
    checkState(newSourceFile.getName().equals(jsAst.getSourceFile().getName()));
  }

  @Test
  public void testGetAstRootNull() {
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetAstRootNotNull() {
    Node root = IR.script();
    jsAst.root = root;
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testClearAst() {
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseSuccess() {
    Node expectedRoot = IR.script();
    jsAst.parse(compiler);
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertEquals(expectedRoot, actualRoot);
  }

  @Test
  public void testParseFailure() {
    jsAst.parse(compiler);
    Node expectedRoot = IR.script();
    assertEquals(expectedRoot, jsAst.getAstRoot(compiler));
    logger.severe("Expected parse error");
  }
}