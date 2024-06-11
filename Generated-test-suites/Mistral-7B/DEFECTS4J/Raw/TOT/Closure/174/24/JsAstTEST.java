import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parsing.ParserRunner.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.parser.ErrorReporter;

/**
 * Tests for the JsAst class.
 */
public class JsAstTest {
  private SourceFile sourceFile;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testParsingValidJavaScript() throws IOException {
    String validJavaScriptCode = "function test() {\n" +
        "  return 1 + 1;\n" +
        "}";

    sourceFile = new SourceFile("test.js", validJavaScriptCode);
    JsAst jsAst = new JsAst(sourceFile);

    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertEquals(IR.FunctionDeclaration("test", null, IR.block(IR.returnStatement(IR.intLiteral(2))), null), astRoot);
  }

  @Test
  public void testParsingInvalidJavaScript() throws IOException {
    String invalidJavaScriptCode = "function test() {\n" +
        "  return 1 + 1;\n" +
        "} \\"; // missing semicolon

    sourceFile = new SourceFile("test.js", invalidJavaScriptCode);
    JsAst jsAst = new JsAst(sourceFile);

    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    // The parse tree should be a dummy block due to the parse error.
    assertEquals(IR.script(), astRoot);

    // Verify that an error was reported.
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(JSError.PARSE_ERROR, compiler.getDiagnostics().get(0).getKind());
  }

  @After
  public void tearDown() {
    if (compiler != null) {
      compiler.dispose();
    }
  }
}