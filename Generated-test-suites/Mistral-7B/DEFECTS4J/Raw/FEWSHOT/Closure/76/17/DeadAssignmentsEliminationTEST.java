package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CompilationUnit;
import org.apache.commons.cli.CompilerInitializationException;
import org.apache.commons.cli.DefaultCompiler;
import org.apache.commons.cli.DefaultEnvironment;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.parser.ParseResult;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private static CompilationUnit compilationUnit;
  private static DefaultCompiler defaultCompiler;

  @BeforeClass
  public static void setup() throws CompilerInitializationException {
    DefaultEnvironment env = new DefaultEnvironment();
    Options options = new Options();
    compilationUnit = new CompilationUnit("", null, options);
    defaultCompiler = new DefaultCompiler(compilationUnit, env);
  }

  @Test
  public void testDeadAssignmentsElimination() {
    String sourceCode = ""
        + "class MyClass {\n"
        + "  var x;\n"
        + "  function foo() {\n"
        + "    x = 1;\n"
        + "    x = 2;\n"
        + "  }\n"
        + "  function bar() {\n"
        + "    x = 1;\n"
        + "  }\n"
        + "}\n";

    ParseResult parseResult = defaultCompiler.parse(sourceCode.toCharArray(), null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilationUnit.getCompiler());

    deadAssignmentsElimination.process(compilationUnit.getExterns(),
        compilationUnit.getRootNode());

    assertTrue(parseResult.getSource().toString().contains("x = 1;"));
    assertFalse(parseResult.getSource().toString().contains("x = 2;"));
  }

  @Test
  public void testDeadAssignmentsEliminationInsideConditionalBranch() {
    String sourceCode = ""
        + "class MyClass {\n"
        + "  var x;\n"
        + "  function foo() {\n"
        + "    if (true) {\n"
        + "      x = 1;\n"
        + "      x = 2;\n"
        + "    } else {\n"
        + "      x = 3;\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    ParseResult parseResult = defaultCompiler.parse(sourceCode.toCharArray(), null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilationUnit.getCompiler());

    deadAssignmentsElimination.process(compilationUnit.getExterns(),
        compilationUnit.getRootNode());

    assertTrue(parseResult.getSource().toString().contains("x = 1;"));
    assertFalse(parseResult.getSource().toString().contains("x = 2;"));
  }

  @Test
  public void testDeadAssignmentsEliminationInsideLoop() {
    String sourceCode = ""
        + "class MyClass {\n"
        + "  var x;\n"
        + "  function foo() {\n"
        + "    for (var i = 0; i < 10; i++) {\n"
        + "      x = 1;\n"
        + "      x = 2;\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    ParseResult parseResult = defaultCompiler.parse(sourceCode.toCharArray(), null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilationUnit.getCompiler());

    deadAssignmentsElimination.process(compilationUnit.getExterns(),
        compilationUnit.getRootNode());

    assertTrue(parseResult.getSource().toString().contains("x = 1;"));
    assertFalse(parseResult.getSource().toString().contains("x = 2;"));
  }

  @Test
  public void testDeadAssignmentsEliminationInsideSwitch() {
    String sourceCode = ""
        + "class MyClass {\n"
        + "  var x;\n"
        + "  function foo() {\n"
        + "    switch (true) {\n"
        + "      case 1:\n"
        + "        x = 1;\n"
        + "        x = 2;\n"
        + "        break;\n"
        + "      case 2:\n"
        + "        x = 3;\n"
        + "        break;\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    ParseResult parseResult = defaultCompiler.parse(sourceCode.toCharArray(), null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilationUnit.getCompiler());

    deadAssignmentsElimination.process(compilationUnit.getExterns(),
        compilationUnit.getRootNode());

    assertTrue(parseResult.getSource().toString().contains("x = 1;"));
    assertFalse(parseResult.getSource().toString().contains("x = 2;"));
  }

  @Test
  public void testDeadAssignmentsEliminationInsideFunctionCall() {
    String sourceCode = ""
        + "class MyClass {\n"
        + "  var x;\n"
        + "  function foo() {\n"
        + "    function bar() {\n"
        + "      x = 1;\n"
        + "      x = 2;\n"
        + "    }\n"
        + "    bar();\n"
        + "  }\n"
        + "}\n";

    ParseResult parseResult = defaultCompiler.parse(sourceCode.toCharArray(), null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilationUnit.getCompiler());

    deadAssignmentsElimination.process(compilationUnit.getExterns(),
        compilationUnit.getRootNode());

    assertTrue(parseResult.getSource().toString().contains("x = 1;"));
    assertFalse(parseResult.getSource().toString().contains("x = 2;"));
  }

  @Test
  public void testDeadAssignmentsEliminationInsideReturnStatement() {
    String sourceCode = ""
        + "class MyClass {\n"
        + "  var x;\n"
        + "  function foo() {\n"
        + "    if (true) {\n"
        + "      x = 1;\n"
        + "      x = 2;\n"
        + "      return x;\n"
        + "    } else {\n"
        + "      return 5;\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    ParseResult parseResult = defaultCompiler.parse(sourceCode.toCharArray(), null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilationUnit.getCompiler());

    deadAssignmentsElimination.process(compilationUnit.getExterns(),
        compilationUnit.getRootNode());

    assertTrue(parseResult.getSource().toString().contains("x = 1;"));
    assertFalse(parseResult.getSource().toString().contains("x = 2;"));
  }

  @Test
  public void testDeadAssignmentsEliminationInsideVariableDeclaration() {
    String sourceCode = ""
        + "class MyClass {\n"
        + "  var x, y;\n"
        + "  function foo() {\n"
        + "    x = 1;\n"
        + "    x = 2;\n"
        + "    y = 3;\n"
        + "  }\n"
        + "}\n";

    ParseResult parseResult = defaultCompiler.parse(sourceCode.toCharArray(), null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilationUnit.getCompiler());

    deadAssignmentsElimination.process(compilationUnit.getExterns(),
        compilationUnit.getRootNode());

    assertTrue(parseResult.getSource().toString().contains("x = 1;"));
    assertFalse(parseResult.getSource().toString().contains("x = 2;"));
    assertTrue(parseResult.getSource().toString().contains("y = 3;"));
  }

  @Test
  public void testDeadAssignmentsEliminationInsideFunctionDeclaration() {
    String sourceCode = ""
        + "class MyClass {\n"
        + "  function foo(x, y) {\n"
        + "    var z;\n"
        + "    z = 1;\n"
        + "    z = 2;\n"
        + "  }\n"
        + "}\n";

    ParseResult parseResult = defaultCompiler.parse(sourceCode.toCharArray(), null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilationUnit.getCompiler());

    deadAssignmentsElimination.process(compilationUnit.getExterns(),
        compilationUnit.getRootNode());

    assertTrue(parseResult.getSource().toString().contains("z = 1;"));
    assertFalse(parseResult.getSource().toString().contains("z = 2;"));
  }
}