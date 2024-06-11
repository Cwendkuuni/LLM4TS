import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  private CollapseVariableDeclarations collapseVariableDeclarations;

  @Before
  public void setUp() {
    collapseVariableDeclarations = new CollapseVariableDeclarations();
  }

  @Test
  public void testProcess_VariableDeclarations_Collapse() {
    String code = "" +
        "var a;" +
        "var b = 1;" +
        "var c = 2;";
    String expectedCode = "var a, b = 1, c = 2;";

    // Execute the process method on the provided Java class
    String processedCode = collapseVariableDeclarations.process(code);

    // Assert that the processed code matches the expected code
    assertEquals(expectedCode, processedCode);
  }

  @Test
  public void testProcess_VariableAssignments_Collapse() {
    String code = "" +
        "a = true;" +
        "b = true;" +
        "var c = true;";
    String expectedCode = "var c = b = a = true;";

    // Execute the process method on the provided Java class
    String processedCode = collapseVariableDeclarations.process(code);

    // Assert that the processed code matches the expected code
    assertEquals(expectedCode, processedCode);
  }

  @Test
  public void testProcess_VariableDeclarations_NonCollapsible() {
    String code = "" +
        "var a = 1;" +
        "var b = 2;" +
        "var c = 3;";
    String expectedCode = "var a = 1; var b = 2; var c = 3;";

    // Execute the process method on the provided Java class
    String processedCode = collapseVariableDeclarations.process(code);

    // Assert that the processed code matches the expected code
    assertEquals(expectedCode, processedCode);
  }

  @Test
  public void testProcess_VariableAssignments_NonCollapsible() {
    String code = "" +
        "a = 1;" +
        "b = 2;" +
        "var c = 3;";
    String expectedCode = "a = 1; b = 2; var c = 3;";

    // Execute the process method on the provided Java class
    String processedCode = collapseVariableDeclarations.process(code);

    // Assert that the processed code matches the expected code
    assertEquals(expectedCode, processedCode);
  }

  @Test
  public void testProcess_EmptyCode() {
    String code = "";
    String expectedCode = "";

    // Execute the process method on the provided Java class
    String processedCode = collapseVariableDeclarations.process(code);

    // Assert that the processed code matches the expected code
    assertEquals(expectedCode, processedCode);
  }

  @Test
  public void testProcess_NullCode() {
    String code = null;
    String expectedCode = "";

    // Execute the process method on the provided Java class
    String processedCode = collapseVariableDeclarations.process(code);

    // Assert that the processed code matches the expected code
    assertEquals(expectedCode, processedCode);
  }

  @Test
  public void testProcess_InvalidCode() {
    String code = "var a = 1; var b = 2;";
    String expectedCode = "";

    // Execute the process method on the provided Java class
    String processedCode = collapseVariableDeclarations.process(code);

    // Assert that the processed code matches the expected code
    assertEquals(expectedCode, processedCode);
  }
}