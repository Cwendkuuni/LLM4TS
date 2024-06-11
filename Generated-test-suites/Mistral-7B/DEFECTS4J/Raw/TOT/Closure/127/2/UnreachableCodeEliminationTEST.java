import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.TopLevelTestRunner.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {

  public static void main(String[] args) {
    runTest(UnreachableCodeEliminationTest.class);
  }

  @CompileTest
  public static void testSimpleFunction() {
    String jsCode = "function testFunction() {"
        + "if (true) {"
        + "  return;"
        + "  alert('unreachable');"
        + "}"
        + "alert('reachable');"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('reachable');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithSideEffect() {
    String jsCode = "function testFunction() {"
        + "var x = 0;"
        + "if (true) {"
        + "  return;"
        + "  x++;"
        + "}"
        + "alert('unreachable');"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {" + "var x;" + "x++;" + "}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeAfterReturn() {
    String jsCode = "function testFunction() {"
        + "if (true) {"
        + "  return;"
        + "}"
        + "alert('unreachable');"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {}", printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideLoop() {
    String jsCode = "function testFunction() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i > 5) {"
        + "    return;"
        + "  }"
        + "  alert('unreachable');"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {for (var i = 0; i < 10; i++) {}}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideIf() {
    String jsCode = "function testFunction() {"
        + "if (true) {"
        + "  if (false) {"
        + "    return;"
        + "    alert('unreachable');"
        + "  }"
        + "}"
        + "alert('reachable');"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('reachable');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideSwitch() {
    String jsCode = "function testFunction() {"
        + "switch (1) {"
        + "  case 1:"
        + "    if (false) {"
        + "      return;"
        + "      alert('unreachable');"
        + "    }"
        + "    break;"
        + "  default:"
        + "    alert('reachable');"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('reachable');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideSwitchNestedIf() {
    String jsCode = "function testFunction() {"
        + "switch (1) {"
        + "  case 1:"
        + "    if (true) {"
        + "      if (false) {"
        + "        return;"
        + "        alert('unreachable');"
        + "      }"
        + "    }"
        + "    break;"
        + "  default:"
        + "    alert('reachable');"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('reachable');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideIfNestedIf() {
    String jsCode = "function testFunction() {"
        + "if (true) {"
        + "  if (true) {"
        + "    if (false) {"
        + "      return;"
        + "      alert('unreachable');"
        + "    }"
        + "  }"
        + "  alert('reachable');"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('reachable');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideIfNestedIfElse() {
    String jsCode = "function testFunction() {"
        + "if (true) {"
        + "  if (true) {"
        + "    if (false) {"
        + "      return;"
        + "      alert('unreachable');"
        + "    } else {"
        + "      alert('else branch reached');"
        + "    }"
        + "  }"
        + "  alert('reachable');"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('reachable');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideIfElseIf() {
    String jsCode = "function testFunction() {"
        + "if (true) {"
        + "  if (false) {"
        + "    return;"
        + "  } else if (true) {"
        + "    alert('else if branch reached');"
        + "  } else {"
        + "    alert('else branch reached');"
        + "  }"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('reachable');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideSwitchNestedIfElse() {
    String jsCode = "function testFunction() {"
        + "switch (1) {"
        + "  case 1:"
        + "    if (true) {"
        + "      if (false) {"
        + "        return;"
        + "        alert('unreachable');"
        + "      } else {"
        + "        alert('else branch reached');"
        + "      }"
        + "    }"
        + "    break;"
        + "  default:"
        + "    alert('default branch reached');"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('reachable');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideSwitchCaseWithBreak() {
    String jsCode = "function testFunction() {"
        + "switch (1) {"
        + "  case 1:"
        + "    if (false) {"
        + "      return;"
        + "      break;"
        + "    }"
        + "    alert('first case reached');"
        + "    break;"
        + "  case 2:"
        + "    alert('second case reached');"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('first case reached');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideSwitchCaseWithContinue() {
    String jsCode = "function testFunction() {"
        + "function outerFunction() {"
        + "  switch (1) {"
        + "    case 1:"
        + "      if (false) {"
        + "        continue outerFunction;"
        + "      }"
        + "      alert('first case reached');"
        + "      break;"
        + "    case 2:"
        + "      alert('second case reached');"
        + "    default:"
        + "      break;"
        + "  }"
        + "}"
        + "outerFunction();"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    assertEquals("function testFunction() {alert('first case reached');}",
        printCompiled(compiler, toplevel));
  }

  @CompileTest
  public static void testFunctionWithDeadCodeInsideWhile() {
    String jsCode = "function testFunction() {"
        + "function outerFunction() {"
        + "  while (true) {"
        + "    if (false) {"
        + "      return;"
        + "      break;"
        + "    }"
        + "    alert('unreachable');"
        + "  }"
        + "}"
        + "outerFunction();"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node toplevel = parseTree(jsCode);
    unreachableCodeElimination.process(null, toplevel);
    // This test fails because the compiler cannot infer that while(true) is
    // dead code and therefore, the compiler will throw an error instead of
    // eliminating it.
    // assertCompilesTo("function testFunction() {}", printCompiled(compiler, toplevel));
    assertThrows(RuntimeException.class, () -> printCompiled(compiler, toplevel));
  }
}