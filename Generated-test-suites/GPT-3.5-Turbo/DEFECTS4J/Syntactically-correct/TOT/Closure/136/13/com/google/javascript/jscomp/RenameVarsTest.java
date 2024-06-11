import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import com.google.javascript.jscomp.RenameVars;

public class RenameVarsTest {

  private RenameVars renameVars;

  @Before
  public void setup() {
    renameVars = new RenameVars(null, "prefix_", false, false, false, null, null, null);
  }

  @Test
  public void testProcess_withValidInputs_shouldRenameGlobalsAndLocals() {
    String input = "var a = 1; function foo() { var b = 2; }";
    String expectedOutput = "var prefix_a = 1; function foo() { var prefix_b = 2; }";

    Node externs = null;
    Node root = parse(input);

    renameVars.process(externs, root);

    String actualOutput = root.toString();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcess_withReservedGlobalNames_shouldNotRenameGlobals() {
    String input = "var a = 1; function foo() { var b = 2; }";
    String expectedOutput = "var a = 1; function foo() { var prefix_b = 2; }";

    Node externs = null;
    Node root = parse(input);

    renameVars.getReservedNames().add("a");
    renameVars.process(externs, root);

    String actualOutput = root.toString();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcess_withReservedLocalNames_shouldNotRenameLocals() {
    String input = "var a = 1; function foo() { var b = 2; }";
    String expectedOutput = "var prefix_a = 1; function foo() { var b = 2; }";

    Node externs = null;
    Node root = parse(input);

    renameVars.getReservedNames().add("b");
    renameVars.process(externs, root);

    String actualOutput = root.toString();
    assertEquals(expectedOutput, actualOutput);
  }

  private Node parse(String source) {
    return new com.google.javascript.jscomp.Parser.parseTestCode(
      Ease.parseJsCode(source)
    );
  }
}