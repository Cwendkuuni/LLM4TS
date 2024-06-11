import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testProcess_withNoAssignments_shouldNotChangeCode() {
    String input = "function foo() {}";
    String expected = "function foo() {}";

    String actual = compile(input);

    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_withAssignmentToLiveVariable_shouldNotChangeCode() {
    String input = "function foo() { var x = 1; x = 2; }";
    String expected = "function foo() { var x = 1; x = 2; }";

    String actual = compile(input);

    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_withDeadAssignment_shouldRemoveAssignment() {
    String input = "function foo() { var x = 1; x = 2; x = 3; }";
    String expected = "function foo() { var x = 1;           x = 2; x = 3; }";

    String actual = compile(input);

    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_withIdentityAssignment_shouldRemoveAssignment() {
    String input = "function foo() { var x = 1; x = x; }";
    String expected = "function foo() { var x = 1;       }";

    String actual = compile(input);

    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_withAssignmentToEscapedVariable_shouldNotRemoveAssignment() {
    String input = "function foo() { var x = 1; function bar() { return x; } x = 2; }";
    String expected = "function foo() { var x = 1; function bar() { return x; } x = 2; }";

    String actual = compile(input);

    assertEquals(expected, actual);
  }

  private String compile(String input) {
    Compiler compiler = new Compiler();
    compiler.disableThreads();
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT_NEXT);
    compiler.initOptions(options);

    Node externs = compiler.parseTestCode("var console = {};");

    Node parseRoot = compiler.parseTestCode(input);
    compiler.getAstVerifier().injectRootNode(parseRoot);

    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);
    pass.process(externs, parseRoot);

    String code = compiler.toSource(parseRoot);
    return code.trim();
  }

}