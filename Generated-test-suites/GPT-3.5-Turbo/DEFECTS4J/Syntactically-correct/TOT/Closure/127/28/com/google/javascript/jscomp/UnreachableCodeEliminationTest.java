import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testProcess_removesCodeAfterReturn_statementWithNoOp() {
    String[] input = {
      "function foo() {",
      "  return;",
      "  alert('unreachable');",
      "}"
    };
    String[] expectedOutput = {
      "function foo(){return}"
    };

    testCodeElimination(input, expectedOutput);
  }

  @Test
  public void testProcess_removesNoOpStatements() {
    String[] input = {
      "function foo() {",
      "  a = 5;",
      "  b();",
      "  c = 10;",
      "}"
    };
    String[] expectedOutput = {
      "function foo(){a=5;b();c=10}"
    };

    testCodeElimination(input, expectedOutput);
  }

  @Test
  public void testProcess_removesUnconditionalBranching() {
    String[] input = {
      "function foo() {",
      "  if (x) {",
      "    break;",
      "    alert('unreachable');",
      "  }",
      "}"
    };
    String[] expectedOutput = {
      "function foo() {",
      " if (x) {",
      " }",
      "}"
    };

    testCodeElimination(input, expectedOutput);
  }

  @Test
  public void testProcess_removesNestedUnconditionalBranching() {
    String[] input = {
      "function foo() {",
      "  if (x) {",
      "    continue;",
      "    alert('unreachable');",
      "  } else {",
      "    while (y) {",
      "      return;",
      "      alert('unreachable');",
      "    }",
      "  }",
      "}"
    };
    String[] expectedOutput = {
      "function foo() {",
      "  if (x) {",
      "  } else {",
      "    while (y) {",
      "    }",
      "  }",
      "}"
    };

    testCodeElimination(input, expectedOutput);
  }

  private void testCodeElimination(String[] input, String[] expectedOutput) {
    String inputCode = String.join("\n", input);
    String expectedOutputCode = String.join("\n", expectedOutput);

    // Create the compiler and the code eliminator
    Compiler compiler = createCompiler();
    UnreachableCodeElimination codeEliminator = new UnreachableCodeElimination(compiler, false);

    // Create the AST from the input code
    Node root = compiler.parseTestCode(inputCode);
    compiler.setRoot(root);

    // Run the code elimination pass
    codeEliminator.process(null, root);

    // Verify that the resulting code matches the expected output
    String actualOutputCode = compiler.toSource();
    assertEquals(expectedOutputCode, actualOutputCode);
  }

  private Compiler createCompiler() {
    CompilerOptions options = new CompilerOptions();
    CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
    return new Compiler(System.err, options);
  }
}