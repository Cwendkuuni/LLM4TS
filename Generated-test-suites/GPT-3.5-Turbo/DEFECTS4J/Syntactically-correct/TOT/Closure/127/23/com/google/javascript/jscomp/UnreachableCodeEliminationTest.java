import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  private UnreachableCodeElimination uce;

  @Before
  public void setUp() {
    // Initialize the UnreachableCodeElimination object
    uce = new UnreachableCodeElimination();
  }

  @Test
  public void testProcess_withNoOpStatements() {
    // Test case where there are no side effect-free statements
    String code = "function foo() {\n" +
                  "  var x = 5;\n" +
                  "  return x;\n" +
                  "}\n";
    String expected = "function foo() {\n" +
                      "  return 5;\n" +
                      "}\n";
    String actual = uce.process(code);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_withDeadCodeAfterReturn() {
    // Test case where there is dead code after a return statement
    String code = "function foo() {\n" +
                  "  if (true) {\n" +
                  "    return;\n" +
                  "    alert('unreachable');\n" +
                  "  }\n" +
                  "}\n";
    String expected = "function foo() {\n" +
                      "  if (true) {\n" +
                      "    return;\n" +
                      "  }\n" +
                      "}\n";
    String actual = uce.process(code);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_withNoOpStatementsAndDeadCodeAfterReturn() {
    // Test case where there are no side effect-free statements and dead code after a return statement
    String code = "function foo() {\n" +
                  "  var x = 5;\n" +
                  "  if (true) {\n" +
                  "    return;\n" +
                  "    alert('unreachable');\n" +
                  "  }\n" +
                  "  return x;\n" +
                  "}\n";
    String expected = "function foo() {\n" +
                      "  if (true) {\n" +
                      "    return;\n" +
                      "  }\n" +
                      "  return 5;\n" +
                      "}\n";
    String actual = uce.process(code);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_withUselessLiveCode() {
    // Test case where there are useless live code such as breaks/continues/returns and statements with no side effects
    String code = "function foo() {\n" +
                  "  var x = 5;\n" +
                  "  if (true) {\n" +
                  "    break;\n" +
                  "    continue;\n" +
                  "    return;\n" +
                  "    x = 10;\n" +
                  "  }\n" +
                  "}\n";
    String expected = "function foo() {\n" +
                      "  if (true) {\n" +
                      "    break;\n" +
                      "  }\n" +
                      "}\n";
    String actual = uce.process(code);
    assertEquals(expected, actual);
  }
}