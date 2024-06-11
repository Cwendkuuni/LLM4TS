import org.junit.Test;
import static org.junit.Assert.*;

public class InlineObjectLiteralsTest {

  @Test
  public void testProcess() {
    // Test typical use case
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      public String get() {
        return "1";
      }
    };
    InlineObjectLiterals inliner = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    inliner.process(null, null);

    // Create a sample class
    InlineObjectLiterals testObj = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    assertNotNull(testObj);

    // Test edge case
    // Run process method with empty externs and root nodes
    try {
      testObj.process(null, null);
      assertTrue(true);
    } catch (Exception e) {
      fail("Exception thrown.");
    }
  }

}