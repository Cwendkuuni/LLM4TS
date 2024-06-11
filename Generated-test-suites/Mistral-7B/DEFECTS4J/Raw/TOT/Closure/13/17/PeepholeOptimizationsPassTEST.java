import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.Type.WARNING;
import static com.google.javascript.jscomp.CompilerOptions.Type.ERROR;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.jscomp.testing.TestCompiler;

import org.junit.Test;

public class PeepholeOptimizationsPassTest {

  @Test
  public void testPeepholeOptimizationsPass() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setClassnames(true);
    options.setWarningLevel(WARNING, "Deprecation");
    options.setWarningLevel(WARNING, "Experimental");
    options.setWarningLevel(WARNING, "JsDoc");

    String jsSource = "function foo(x) {"
        + "if (x < 10) {"
        + "return 1;"
        + "} else {"
        + "return x * x;"
        + "}";

    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);

    Node root = compiler.parseTree(jsSource);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler,
        new ConstantFolding(), new DeadCodeElimination(), new IfFolding());
    pass.process(null, root);

    // Test for constant folding
    assertEquals("function foo(x) {"
        + "if (x < 10) {"
        + "return 1;"
        + "} else {"
        + "return 100;"
        + "}", compiler.getSource(root));

    // Test for dead code elimination
    jsSource = "function foo() {"
        + "if (false) {"
        + "return 1;"
        + "} else {"
        + "return 1;"
        + "}";

    root = compiler.parseTree(jsSource);
    compiler.setOptions(options);
    pass = new PeepholeOptimizationsPass(compiler, new DeadCodeElimination());
    pass.process(null, root);

    assertEquals("function foo() { return 1; }", compiler.getSource(root));

    // Test for if folding
    jsSource = "function foo(x) {"
        + "if (x < 10) {"
        + "return 1;"
        + "} else if (x > 20) {"
        + "return 3;"
        + "} else {"
        + "return 2;"
        + "}";

    root = compiler.parseTree(jsSource);
    compiler.setOptions(options);
    pass = new PeepholeOptimizationsPass(compiler, new IfFolding());
    pass.process(null, root);

    assertEquals("function foo(x) {"
        + "return x < 10 ? 1 : x > 20 ? 3 : 2;"
        + "}", compiler.getSource(root));
  }

  // Add error testing here if necessary

  @Test(expected = AssertionError.class)
  public void testExceptionOnError() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setClassnames(true);
    options.setWarningLevel(ERROR, "AllErrors");

    String jsSource = "function foo() {"
        + "return 1;"
        + "1 / 0;";

    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);

    Node root = compiler.parseTree(jsSource);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler,
        new DeadCodeElimination(), new IfFolding(), new DivByZero());
    pass.process(null, root);

    fail("Expected DivByZero error but none was thrown.");
  }
}