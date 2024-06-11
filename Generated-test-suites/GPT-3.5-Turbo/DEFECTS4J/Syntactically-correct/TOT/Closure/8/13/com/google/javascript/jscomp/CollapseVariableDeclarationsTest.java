import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  private CollapseVariableDeclarations collapseVariableDeclarations;

  @Before
  public void setUp() {
    collapseVariableDeclarations = new CollapseVariableDeclarations();
  }

  @Test
  public void testCollapse_multipleVariableDeclarations_success() {
    String code = "var a;\n" +
        "var b = 1;\n" +
        "var c = 2;";

    String expected = "var a, b = 1, c = 2;";

    String result = collapseVariableDeclarations.collapseVariableDeclarations(code);

    assertEquals(expected, result);
  }

  @Test
  public void testCollapse_multipleVariableDeclarations_emptyCode_success() {
    String code = "";

    String expected = "";

    String result = collapseVariableDeclarations.collapseVariableDeclarations(code);

    assertEquals(expected, result);
  }

  @Test
  public void testCollapse_multipleVariableDeclarations_noVariableDeclarations_success() {
    String code = "var a = 1;\n" +
        "console.log(a);";

    String expected = code;

    String result = collapseVariableDeclarations.collapseVariableDeclarations(code);

    assertEquals(expected, result);
  }

  @Test
  public void testCollapse_assignmentsAndVariableDeclarations_success() {
    String code = "a = true;\n" +
        "b = true;\n" +
        "var c = true;\n" +
        "var d = 'test';";

    String expected = "var d = 'test'; var c = b = a = true;";

    String result = collapseVariableDeclarations.collapseVariableDeclarations(code);

    assertEquals(expected, result);
  }

  @Test
  public void testCollapse_assignmentsAndVariableDeclarations_noAssignments_success() {
    String code = "var a = 1;\n" +
        "var b = 2;\n" +
        "var c = 3;\n" +
        "var d = 4;";

    String expected = "var a = 1, b = 2, c = 3, d = 4;";

    String result = collapseVariableDeclarations.collapseVariableDeclarations(code);

    assertEquals(expected, result);
  }

  @Test
  public void testCollapse_assignmentsAndVariableDeclarations_emptyCode_success() {
    String code = "";

    String expected = "";

    String result = collapseVariableDeclarations.collapseVariableDeclarations(code);

    assertEquals(expected, result);
  }

  @Test
  public void testCollapse_assignmentsAndVariableDeclarations_noVariableDeclarations_success() {
    String code = "a = true;\n" +
        "b = true;\n" +
        "console.log(a);";

    String expected = code;

    String result = collapseVariableDeclarations.collapseVariableDeclarations(code);

    assertEquals(expected, result);
  }

}