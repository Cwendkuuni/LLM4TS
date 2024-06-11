import static org.junit.Assert.*;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testProcess() {
    // Test typical use case
    String input = "var x = {a: 1, b: 2, c: 3};";
    String output = "var x_a_ = 1, x_b_ = 2, x_c_ = 3;";
    assertEquals(output, process(input));

    // Test edge case with no assignments
    input = "var x = {};";
    output = "";
    assertEquals(output, process(input));

    // Test error scenario with self-referential assignment
    input = "var x = {a: 1, b: x.a};";
    try {
      process(input);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertEquals("Self-referential assignment is not allowed", e.getMessage());
    }
  }

  private String process(String input) {
    // mock dependencies
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      public String get() {
        return String.valueOf(System.currentTimeMillis());
      }
    };

    // instantiate object under test
    InlineObjectLiterals obj = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // process input
    String externs = "";
    Node root = compiler.parseTestCode(input);
    obj.process(externs, root);

    // return processed output
    return compiler.toSource(root);
  }

}