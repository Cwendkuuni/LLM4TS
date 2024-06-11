import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        collapseVariableDeclarations = new CollapseVariableDeclarations();
    }

    @After
    public void tearDown() {
        collapseVariableDeclarations = null;
    }

    @Test
    public void testProcess_WhenNoCollapsesNeeded_ShouldNotChangeCode() {
        // Arrange
        String originalCode = "var a;\nvar b = 1;\nvar c = 2;";
        String expectedCode = originalCode;

        // Act
        String actualCode = collapseVariableDeclarations.process(originalCode);

        // Assert
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testProcess_WhenMultipleDeclarations_ShouldCollapseIntoOneDeclaration() {
        // Arrange
        String originalCode = "var a;\nvar b = 1;\nvar c = 2;";
        String expectedCode = "var a, b = 1, c = 2;";

        // Act
        String actualCode = collapseVariableDeclarations.process(originalCode);

        // Assert
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testProcess_WhenAssignmentsPresent_ShouldCollapseAssignments() {
        // Arrange
        String originalCode = "a = true;\nb = true;\nvar c = true;";
        String expectedCode = "var c = b = a = true;";

        // Act
        String actualCode = collapseVariableDeclarations.process(originalCode);

        // Assert
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testProcess_WhenIfNodeWithAdjacentVarNodes_ShouldNotCollapseIntoOneDeclaration() {
        // Arrange
        String originalCode = "if (x) {\n    var a = 1;\n    var b = 2;\n} else {\n    var c = 3;\n    var d = 4;\n}";
        String expectedCode = originalCode;

        // Act
        String actualCode = collapseVariableDeclarations.process(originalCode);

        // Assert
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testProcess_WhenVarNodeWithBlacklistedStubs_ShouldNotCollapseIntoOneDeclaration() {
        // Arrange
        String originalCode = "var a;\nvar b = 1;\nbar.fn();";
        String expectedCode = originalCode;

        // Act
        String actualCode = collapseVariableDeclarations.process(originalCode);

        // Assert
        Assert.assertEquals(expectedCode, actualCode);
    }
}