import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        collapseVariableDeclarations = new CollapseVariableDeclarations();
    }

    @Test
    public void collapseVariableDeclarations_withMultipleDeclarations_expectCollapsedDeclaration() {
        String input = "var a;\nvar b = 1;\nvar c = 2;";
        String expectedOutput = "var a, b = 1, c = 2;";

        String actualOutput = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void collapseVariableDeclarations_withNoDeclarations_expectSameOutput() {
        String input = "var a = 1;";
        String expectedOutput = "var a = 1;";

        String actualOutput = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void collapseVariableDeclarations_withAssignments_expectCollapsedAssignments() {
        String input = "a = true;\nb = true;\nvar c = true;";
        String expectedOutput = "var c = b = a = true;";

        String actualOutput = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void collapseVariableDeclarations_withNoAssignments_expectSameOutput() {
        String input = "var a;\nvar b;";
        String expectedOutput = "var a;\nvar b;";

        String actualOutput = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void collapseVariableDeclarations_withMixedDeclarationsAndAssignments_expectCollapsedDeclarationAndAssignments() {
        String input = "var a;\nvar b = true;\nvar c;\na = 1;\nb = 2;";
        String expectedOutput = "var a, c, b = true;\na = 1;\nb = 2;";

        String actualOutput = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void collapseVariableDeclarations_withNoDeclarationsOrAssignments_expectSameOutput() {
        String input = "";
        String expectedOutput = "";

        String actualOutput = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expectedOutput, actualOutput);
    }
}