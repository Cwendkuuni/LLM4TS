import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parser.testtype.SourceFileType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.jscomp.parsing.testtype.ParserTest;
import com.google.javascript.jscomp.testtype.CompilationTest;
import com.google.javascript.rhino.IR;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private ParserTest parserTest;
  private CompilationTest compilationTest;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler(new CompilerOptions());
  }

  @After
  public void tearDown() {
    compiler.dispose();
  }

  // Test for getAstRoot method
  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("Test.js", "print('Hello World');", JS_SOURCE);
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  // Test for clearAst method
  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("Test.js", "print('Hello World');", JS_SOURCE);
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler); // parse the file
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  // Test for getInputId method
  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("Test.js", "print('Hello World');", JS_SOURCE);
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals("Test.js", inputId.getName());
  }

  // Test for getSourceFile method
  @Test
  public void testGetSourceFile() {
    SourceFile testSourceFile = new SourceFile("Test.js", "print('Hello World');", JS_SOURCE);
    JsAst jsAst = new JsAst(testSourceFile);
    SourceFile resultSourceFile = jsAst.getSourceFile();
    assertEquals(testSourceFile, resultSourceFile);
  }

  // Test for setSourceFile method
  @Test
  public void testSetSourceFile() {
    SourceFile testSourceFile = new SourceFile("Test.js", "print('Hello World');", JS_SOURCE);
    JsAst jsAst = new JsAst(new SourceFile("Test2.js", "print('Hello World');", JS_SOURCE));
    jsAst.setSourceFile(testSourceFile);
    SourceFile resultSourceFile = jsAst.getSourceFile();
    assertEquals(testSourceFile, resultSourceFile);
  }

  // Test for parse method with valid code
  @Test
  public void testParseValidCode() {
    SourceFile sourceFile = new SourceFile("Test.js", "print('Hello World');", JS_SOURCE);
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertEquals(IR.script(), jsAst.getAstRoot(compiler).getFirstChild());
  }

  // Test for parse method with invalid code
  @Test(expected = IOException.class)
  public void testParseInvalidCode() {
    SourceFile sourceFile = new SourceFile("Test.js", "print('Hello World'};", JS_SOURCE);
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
  }

  // Test for parse method with compiler errors
  @Test
  public void testParseCompilerErrors() {
    SourceFile sourceFile = new SourceFile("Test.js", "print('1' + 2);", JS_SOURCE);
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertTrue(compiler.hasHaltingErrors());
  }

  // Test for parse method with parse errors
  @Test
  public void testParseParseErrors() {
    SourceFile sourceFile = new SourceFile("Test.js", "print('Hello World');print('Hello World');", JS_SOURCE);
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertTrue(jsAst.getAstRoot(compiler).getFirstChild().getFirstChild().isError());
  }
}