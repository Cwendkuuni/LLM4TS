import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class JsAstTest {

  @Test
  public void testJsAst_ValidSourceFile() {
    String validSourceCode = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    SourceFile sourceFile = new SourceFile("testSourceFile.js", validSourceCode);
    AbstractCompiler compiler = new TestCompiler(new CompilerOptions());

    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);

    // Check that the AST was generated correctly
    assertNotNull(astRoot);
    assertTrue(astRoot instanceof Node);

    // Perform some checks on the generated AST
    // ... (add any additional checks here if needed)
  }

  @Test
  public void testJsAst_InvalidSourceFile() {
    String invalidSourceCode = "}"; // Missing 'function' keyword

    SourceFile sourceFile = new SourceFile("testSourceFile.js", invalidSourceCode);
    AbstractCompiler compiler = new TestCompiler(new CompilerOptions());

    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);

    // Check that the AST was not generated, and an error was reported
    assertNull(astRoot);
    assertTrue(compiler.hasHaltingErrors());

    // Check that the error message is informative
    // ... (add any additional checks here if needed)
  }
}