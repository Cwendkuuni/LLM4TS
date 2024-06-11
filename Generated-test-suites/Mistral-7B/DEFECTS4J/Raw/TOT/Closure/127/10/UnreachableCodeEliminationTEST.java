import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.AbstractShallowCallbackType.TRAVERSE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {
  @org.junit.Test
  public void testUnreachableCodeElimination() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String code = "function test() {\n" +
        "  if (true) {\n" +
        "    return; \n" +
        "  } \n" +
        "  alert('unreachable'); \n" +
        "}";

    Node root = Parser.parseJs(code, null, null);

    // Add the unreachable code elimination pass
    UnreachableCodeElimination eliminationPass = new UnreachableCodeElimination(compiler, false);
    eliminationPass.process(null, root);

    // Verify the unreachable code was removed
    assertTrue(root.getChild(1).isMissing());
    assertFalse(root.getChild(1).getParent().getChild(1).isMissing());

    // Test with removeNoOpStatements = true
    code = "function test() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  } \n" +
        "  x++; \n" +
        "}";

    root = Parser.parseJs(code, null, null);

    eliminationPass = new UnreachableCodeElimination(compiler, true);
    eliminationPass.process(null, root);

    // Verify the unreachable code was removed
    assertFalse(root.getChild(3).isMissing());
    assertTrue(root.getChild(4).isMissing());

    // Test with unreachable code in a loop
    code = "function test() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (true) {\n" +
        "      break;\n" +
        "    } \n" +
        "    alert('unreachable'); \n" +
        "  } \n" +
        "}";

    root = Parser.parseJs(code, null, null);

    eliminationPass.process(null, root);

    // Verify the unreachable code was removed
    assertFalse(root.getChild(2).getChild(1).isMissing());
    assertTrue(root.getChild(2).getChild(2).isMissing());

    // Test with unreachable code in a conditional statement
    code = "function test() {\n" +
        "  if (true) {\n" +
        "    if (false) {\n" +
        "      alert('unreachable'); \n" +
        "    } \n" +
        "  } \n" +
        "}";

    root = Parser.parseJs(code, null, null);

    eliminationPass.process(null, root);

    // Verify the unreachable code was removed
    assertTrue(root.getChild(1).isMissing());

    // Test with unreachable code in a try-catch block
    code = "function test() {\n" +
        "  try {\n" +
        "    throw new Error('unreachable'); \n" +
        "  } catch (e) {\n" +
        "    return; \n" +
        "  } \n" +
        "}";

    root = Parser.parseJs(code, null, null);

    eliminationPass.process(null, root);

    // Verify the unreachable code was removed
    assertTrue(root.getChild(1).isMissing());

    // Test with unreachable code in a switch statement
    code = "function test() {\n" +
        "  switch (1) {\n" +
        "    case 1:\n" +
        "      if (false) {\n" +
        "        alert('unreachable'); \n" +
        "      }\n" +
        "      break;\n" +
        "  } \n" +
        "}";

    root = Parser.parseJs(code, null, null);

    eliminationPass.process(null, root);

    // Verify the unreachable code was removed
    assertTrue(root.getChild(1).getChild(0).isMissing());

    // Test with unreachable code in a for-in statement
    code = "function test() {\n" +
        "  for (var k in {}) {\n" +
        "    if (true) {\n" +
        "      return; \n" +
        "    } \n" +
        "  } \n" +
        "}";

    root = Parser.parseJs(code, null, null);

    eliminationPass.process(null, root);

    // Verify the unreachable code was removed
    assertTrue(root.getChild(1).getChild(0).isMissing());
  }

  @org.junit.Test
  public void testDeadCodeAfterReturns() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String code = "function test() {\n" +
        "  if (true) {\n" +
        "    return; \n" +
        "    alert('unreachable'); \n" +
        "  } \n" +
        "}";

    Node root = Parser.parseJs(code, null, null);

    // Add the unreachable code elimination pass
    UnreachableCodeElimination eliminationPass = new UnreachableCodeElimination(compiler, false);
    eliminationPass.process(null, root);

    // Verify the unreachable code was not removed
    assertFalse(root.getChild(1).isMissing());

    // Test with removeNoOpStatements = true
    code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return;\n" +
        "  x++; \n" +
        "}";

    root = Parser.parseJs(code, null, null);

    eliminationPass = new UnreachableCodeElimination(compiler, true);
    eliminationPass.process(null, root);

    // Verify the unreachable code was not removed
    assertFalse(root.getChild(1).isMissing());
  }

  @org.junit.Test
  public void testRemoveNoOpStatements() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return;\n" +
        "  x++; \n" +
        "}";

    Node root = Parser.parseJs(code, null, null);

    // Add the unreachable code elimination pass with removeNoOpStatements = false
    UnreachableCodeElimination eliminationPass = new UnreachableCodeElimination(compiler, false);
    eliminationPass.process(null, root);

    // Verify the no-op statement was not removed
    assertFalse(root.getChild(1).getChild(0).isMissing());

    // Add the unreachable code elimination pass with removeNoOpStatements = true
    eliminationPass = new UnreachableCodeElimination(compiler, true);
    eliminationPass.process(null, root);

    // Verify the no-op statement was removed
    assertTrue(root.getChild(1).isMissing());
  }

  @org.junit.Test
  public void testUnconditionalBranching() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String code = "function test() {\n" +
        "  if (true) {\n" +
        "    break test2;\n" +
        "  }\n" +
        "test2:\n" +
        "  if (true) {\n" +
        "    break test2;\n" +
        "  }\n" +
        "}";

    Node root = Parser.parseJs(code, null, null);

    // Add the unreachable code elimination pass
    UnreachableCodeElimination eliminationPass = new UnreachableCodeElimination(compiler, false);
    eliminationPass.process(null, root);

    // Verify the unreachable code was removed
    assertTrue(root.getChild(1).isMissing());
    assertTrue(root.getChild(2).isMissing());
  }

  @org.junit.Test
  public void testUnconditionalBreakWithLabel() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String code = "function test() {\n" +
        "  label1:\n" +
        "  if (true) {\n" +
        "    break label1;\n" +
        "  }\n" +
        "}";

    Node root = Parser.parseJs(code, null, null);

    // Add the unreachable code elimination pass
    UnreachableCodeElimination eliminationPass = new UnreachableCodeElimination(compiler, false);
    eliminationPass.process(null, root);

    // Verify the unreachable code was not removed
    assertFalse(root.getChild(1).isMissing());
  }

  @org.junit.Test
  public void testUnconditionalContinueWithLabel() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String code = "function test() {\n" +
        "  label1:\n" +
        "  if (true) {\n" +
        "    continue label1;\n" +
        "  }\n" +
        "}";

    Node root = Parser.parseJs(code, null, null);

    // Add the unreachable code elimination pass
    UnreachableCodeElimination eliminationPass = new UnreachableCodeElimination(compiler, false);
    eliminationPass.process(null, root);

    // Verify the unreachable code was not removed
    assertFalse(root.getChild(1).isMissing());
  }

  @org.junit.Test
  public void testUnconditionalReturnWithLabel() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String code = "function test() {\n" +
        "  label1:\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "}";

    Node root = Parser.parseJs(code, null, null);

    // Add the unreachable code elimination pass
    UnreachableCodeElimination eliminationPass = new UnreachableCodeElimination(compiler, false);
    eliminationPass.process(null, root);

    // Verify the unreachable code was not removed
    assertFalse(root.getChild(1).isMissing());
  }
}