import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {
    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setup() {
        collapseVariableDeclarations = new CollapseVariableDeclarations();
    }

    @Test
    public void testCollapseVariableDeclarations() {
        // Test case for basic functionality of collapsing variable declarations
        String input = "var a;\nvar b = 1;\nvar c = 2;";
        String expectedOutput = "var a, b = 1, c = 2;";
        String actualOutput = collapseVariableDeclarations.collapse(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCollapseAssignments() {
        // Test case for collapsing assignments
        String input = "a = true;\nb = true;\nvar c = true;";
        String expectedOutput = "var c = b = a = true;";
        String actualOutput = collapseVariableDeclarations.collapse(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCollapseVariableDeclarationsWithFunctionBlock() {
        // Test case for collapsing variable declarations inside a function block
        String input = "function test() {\nvar a;\nvar b = 1;\nvar c = 2;\n}";
        String expectedOutput = "function test() { var a, b = 1, c = 2; }";
        String actualOutput = collapseVariableDeclarations.collapse(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCollapseVariableDeclarationsWithIfBlock() {
        // Test case for not collapsing variable declarations inside an if block
        String input = "if (condition) {\nvar a;\nvar b = 1;\nvar c = 2;\n}";
        String expectedOutput = "if (condition) { var a;\nvar b = 1;\nvar c = 2;\n }";
        String actualOutput = collapseVariableDeclarations.collapse(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCollapseVariableDeclarationsWithMultipleAssignments() {
        // Test case for collapsing multiple assignments
        String input = "var a = b = c = 1;\nvar d = e = f = 2;";
        String expectedOutput = "var a = b = c = 1;\nvar d = e = f = 2;";
        String actualOutput = collapseVariableDeclarations.collapse(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCollapseVariableDeclarationsWithJSDoc() {
        // Test case for collapsing variables with JSDoc
        String input = "/** @type {string} */ var a;\n/** @type {number} */ var b = 1;";
        String expectedOutput = "/** @type {string} */ var a, b = 1;";
        String actualOutput = collapseVariableDeclarations.collapse(input);
        assertEquals(expectedOutput, actualOutput);
    }
}
