
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  CollapseVariableDeclarations collapseVariableDeclarations;

  @Before
  public void setUp() {
    collapseVariableDeclarations = new CollapseVariableDeclarations();
  }

  @Test
  public void testCollapse() {
    // Given
    String code = "var a;\n" +
        "var b = 1;\n" +
        "var c = 2;";

    String expected = "var a, b = 1, c = 2;";

    // When
    String actual = collapseVariableDeclarations.collapseVarDeclarations(code);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  public void testCollapseAssign() {
    // Given
    String code = "a = true;\n" +
        "b = true;\n" +
        "var c = true;";

    String expected = "var c = b = a = true;";

    // When
    String actual = collapseVariableDeclarations.collapseAssigns(code);

    // Then
    assertEquals(expected, actual);
  }
}
