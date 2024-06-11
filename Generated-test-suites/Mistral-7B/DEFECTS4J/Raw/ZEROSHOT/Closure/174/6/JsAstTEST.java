import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.AbstractCompilerForTesting;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for JsAst class.
 */
public class JsAstTest {
  private SourceFile sourceFile;
  private AbstractCompiler compiler;
  private JsAst jsAst;

  @Before
  public void setUp() {
    sourceFile = new SourceFile("test.js", "test content");
    compiler = new AbstractCompilerForTesting();
    jsAst = new JsAst(sourceFile);
  }

  @After
  public void tearDown() {
    jsAst = null;
    sourceFile = null;
    compiler = null;
  }

  @Test
  public void testGetAstRoot() {
    Node expectedRoot = IR.script();
    when(compiler.getParserConfig()).thenReturn(new ParserConfig());
    jsAst.parse(compiler);
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertNotNull(actualRoot);
    assertNotNull(actualRoot.getStaticSourceName());
    assertNotNull(actualRoot.getStaticSourceFile());
    assertNotNull(actualRoot.getInputId());
    assertNull(actualRoot.getLineNumber());
    assertNull(actualRoot.getLineOffset());
    assertNull(actualRoot.getEndLineNumber());
    assertNull(actualRoot.getEndLineOffset());
    assertNull(actualRoot.getSourceName());
    assertNull(actualRoot.getName());
    assertNull(actualRoot.getSourcePosition());
    assertNotNull(actualRoot.getChildren());
    assertNull(actualRoot.getFirstChild());
    assertNull(actualRoot.getLastChild());
    assertNull(actualRoot.getParent());
    assertNull(actualRoot.getValue());
    // Test with a non-null root
    Node root = IR.block(IR.literal(1));
    jsAst.root = root;
    actualRoot = jsAst.getAstRoot(compiler);
    assertNotNull(actualRoot);
    assertNull(actualRoot.getStaticSourceName());
    assertNotNull(actualRoot.getStaticSourceFile());
    assertNotNull(actualRoot.getInputId());
    assertNull(actualRoot.getLineNumber());
    assertNull(actualRoot.getLineOffset());
    assertNull(actualRoot.getEndLineNumber());
    assertNull(actualRoot.getEndLineOffset());
    assertNull(actualRoot.getSourceName());
    assertNull(actualRoot.getName());
    assertNull(actualRoot.getSourcePosition());
    assertNotNull(actualRoot.getChildren());
    assertNull(actualRoot.getFirstChild());
    assertNull(actualRoot.getLastChild());
    assertNull(actualRoot.getParent());
    assertNull(actualRoot.getValue());
    assertSame(root, actualRoot);
  }

  @Test
  public void testClearAst() {
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertNull(actualRoot);
  }

  @Test
  public void testGetInputId() {
    InputId expectedInputId = new InputId("test.js");
    jsAst.inputId = expectedInputId;
    InputId actualInputId = jsAst.getInputId();
    assertNotNull(actualInputId);
    assertSame(expectedInputId, actualInputId);
  }

  @Test
  public void testGetSourceFile() {
    SourceFile expectedSourceFile = sourceFile;
    jsAst.sourceFile = expectedSourceFile;
    SourceFile actualSourceFile = jsAst.getSourceFile();
    assertNotNull(actualSourceFile);
    assertSame(expectedSourceFile, actualSourceFile);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test content");
    jsAst.setSourceFile(newSourceFile);
    SourceFile actualSourceFile = jsAst.getSourceFile();
    assertNotNull(actualSourceFile);
    assertSame(newSourceFile, actualSourceFile);
  }

  @Test
  public void testParseWithNullCompiler() {
    jsAst.parse(null);
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertNull(actualRoot);
  }

  @Test(expected = NullPointerException.class)
  public void testParseWithNullSourceFile() {
    jsAst = new JsAst(null);
    jsAst.parse(compiler);
  }

}