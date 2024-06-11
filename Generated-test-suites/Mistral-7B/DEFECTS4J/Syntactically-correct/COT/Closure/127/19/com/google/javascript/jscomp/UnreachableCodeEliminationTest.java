import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeCoverage.cover;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

  private static final Logger logger = Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testUnreachableCodeElimination() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function f() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "  alert('unreachable');\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(f()) {}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testUnreachableCodeEliminationWithNoOpStatements() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function f() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  } else {\n" +
        "    true;\n" +
        "  }\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(f()) {}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationWithBreakStatement() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function f() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (i === 5) {\n" +
        "      break;\n" +
        "    }\n" +
        "  }\n" +
        "  alert('unreachable');\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(f()) {\n" +
        "  for (var i = 0; i < 5; i++) {} // Break statement removed.\n" +
        "}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationWithContinueStatement() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function f() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (i === 5) {\n" +
        "      continue;\n" +
        "    }\n" +
        "    alert(i);\n" +
        "  }\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(f()) {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (i !== 5) {\n" +
        "      alert(i);\n" +
        "    }\n" +
        "  }\n" +
        "}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationWithReturnStatement() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function f() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "  alert('unreachable');\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(f()) {}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationWithEmptyBlock() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function f() {\n" +
        "  if (true) {\n" +
        "  }\n" +
        "  alert('unreachable');\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(f()) {}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationWithFunction() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function g() {\n" +
        "  function f() {\n" +
        "    if (true) {\n" +
        "      return;\n" +
        "    }\n" +
        "    alert('unreachable');\n" +
        "  }\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(g()) {\n" +
        "  function f() {} // Return statement removed.\n" +
        "}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationWithFunctionReturnStatement() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function g() {\n" +
        "  function f() {\n" +
        "    if (true) {\n" +
        "      return 1;\n" +
        "    }\n" +
        "    alert('unreachable');\n" +
        "  }\n" +
        "  return f();\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(g()) {\n" +
        "  function f() {\n" +
        "    return 1;\n" + // Return statement kept.\n
        "  }\n" +
        "  return f();\n" +
        "}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationWithConditionalBranching() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function f() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "  if (false) {\n" +
        "    alert('unreachable');\n" +
        "  }\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(f()) {}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationWithUnconditionalBranching() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function f() {\n" +
        "  break;\n" +
        "  alert('unreachable');\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(f()) {\n" +
        "  // Break statement removed.\n" +
        "}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationWithComplexConditionalBranching() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    AbstractCompiler compiler = new TestCompiler(options);

    SourceFile input = new SourceFile("test.js", "function f() {\n" +
        "  var x = 0;\n" +
        "  if (x === 0) {\n" +
        "    if (true) {\n" +
        "      return;\n" +
        "    }\n" +
        "  } else if (x === 1) {\n" +
        "    if (false) {\n" +
        "      alert('unreachable');\n" +
        "    }\n" +
        "    if (true) {\n" +
        "      return;\n" +
        "    }\n" +
        "  } else if (x === 2) {\n" +
        "    if (false) {\n" +
        "      alert('unreachable');\n" +
        "    }\n" +
        "  } else {\n" +
        "    if (false) {\n" +
        "      alert('unreachable');\n" +
        "    }\n" +
        "  }\n" +
        "}");

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = input.parseTree();
    unreachableCodeElimination.process(null, root);

    Node actual = root.getFirstChild();
    Node expected = NodeUtil.createNode("function(f()) {\n" +
        "  var x = 0;\n" +
        "  if (x === 0) {\n" +
        "    return;\n" + // Return statement kept in the first if branch.\n
        "  } else if (x === 1) {\n" +
        "  } else if (x === 2) {\n" +
        "  } else {\n" +
        "  }\n" +
        "}");

    assertEquals(expected.toString(), actual.toString());

    cover(input);
  }
}