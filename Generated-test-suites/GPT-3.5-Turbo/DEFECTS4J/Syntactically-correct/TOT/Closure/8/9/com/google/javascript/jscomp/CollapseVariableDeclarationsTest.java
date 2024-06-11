import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations_singleDeclaration() {
    // Arrange
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Act
    String collapsedCode = collapseVariableDeclarations.collapseVariableDeclarations("var a;\nvar b = 1;\nvar c = 2;");

    // Assert
    assertEquals("var a, b = 1, c = 2;", collapsedCode);
  }

  @Test
  public void testCollapseVariableDeclarations_multipleDeclarations() {
    // Arrange
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Act
    String collapsedCode = collapseVariableDeclarations.collapseVariableDeclarations("var a;\nvar b = 1;\nvar c = 2;\nvar d;\nvar e = 3;");

    // Assert
    assertEquals("var a, b = 1, c = 2, d, e = 3;", collapsedCode);
  }

  @Test
  public void testCollapseVariableDeclarations_assignments() {
    // Arrange
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Act
    String collapsedCode = collapseVariableDeclarations.collapseVariableDeclarations("a = true;\nb = true;\nvar c = true;");

    // Assert
    assertEquals("var c = b = a = true;", collapsedCode);
  }

  @Test
  public void testCollapseVariableDeclarations_redeclaration() {
    // Arrange
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Act
    String collapsedCode = collapseVariableDeclarations.collapseVariableDeclarations("var a;\nvar a;\nvar a;");

    // Assert
    assertEquals("var a;\nvar a;\nvar a;", collapsedCode);
  }

  @Test
  public void testCollapseVariableDeclarations_emptyCode() {
    // Arrange
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Act
    String collapsedCode = collapseVariableDeclarations.collapseVariableDeclarations("");

    // Assert
    assertEquals("", collapsedCode);
  }

}