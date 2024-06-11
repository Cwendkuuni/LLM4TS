import static org.junit.Assert.*;
import static com.google.javascript.rhino.Node.*;
import static com.google.javascript.jscomp.parser.testtype.SourceFileType.sourceFileString;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.FileOrUrlSource;
import com.google.javascript.rhino.ParserConfig;
import com.google.javascript.rhino.scripting.ScriptOptions;
import com.google.javascript.rhino.testing.TestErrorReporter;
import com.google.javascript.rhino.testing.TestLogger;

public class JsAstTest {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() throws Exception {
    compiler = new AbstractCompiler(new TestErrorReporter(), new TestLogger());
    sourceFile = new SourceFile(sourceFileString("test.js", "test content"));
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node actual = jsAst.getAstRoot(compiler);
    assertNotNull(actual);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootNullCompiler() {
    jsAst.getAstRoot(null);
  }

  @Test
  public void testClearAst() {
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node actual = jsAst.getAstRoot(compiler);
    assertNull(actual);
  }

  @Test
  public void testGetInputId() {
    String expected = sourceFile.getName();
    assertEquals(expected, jsAst.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile(sourceFileString("new_test.js", "new test content"));
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileDifferentNames() {
    SourceFile newSourceFile = new SourceFile(sourceFileString("test.js", "new test content"));
    jsAst.setSourceFile(newSourceFile);
  }

  @Test
  public void testParseSuccess() {
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseIOException() {
    compiler.setParserConfig(new ParserConfig());
    compiler.getParserConfig().setAllowIOErrors(false);
    jsAst.parse(compiler);
    assertTrue(compiler.hasHaltingErrors());
  }

  @Test
  public void testParseNullAst() {
    compiler.setParserConfig(new ParserConfig());
    compiler.getParserConfig().setAllowIOErrors(true);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testSetStaticSourceFile() {
    Node actual = script();
    actual.setStaticSourceFile(sourceFile);
    String expected = sourceFile.getName();
    assertEquals(expected, actual.getSourceFile().getName());
  }
}