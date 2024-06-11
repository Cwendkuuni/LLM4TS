
import org.junit.Assert;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

        // Test case 1: var a; var b = 1; var c = 2;
        String expectedOutput1 = "var a, b = 1, c = 2;";
        String input1 = "var a;\nvar b = 1;\nvar c = 2;\n";
        String actualOutput1 = collapseVariableDeclarations.collapseVariables(input1);
        Assert.assertEquals(expectedOutput1, actualOutput1);

        // Test case 2: a = true; b = true; var c = true;
        String expectedOutput2 = "var c = b = a = true;";
        String input2 = "a = true;\nb = true;\nvar c = true;\n";
        String actualOutput2 = collapseVariableDeclarations.collapseVariables(input2);
        Assert.assertEquals(expectedOutput2, actualOutput2);
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testCollapseVariableDeclarations_EmptyInput() {
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

        // Test case: empty input
        String expectedOutput = "";
        String input = "";
        String actualOutput = collapseVariableDeclarations.collapseVariables(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCollapseVariableDeclarations_OneDeclaration() {
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

        // Test case: var a;
        String expectedOutput = "var a;";
        String input = "var a;";
        String actualOutput = collapseVariableDeclarations.collapseVariables(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCollapseVariableDeclarations_Reassignment() {
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

        // Test case: a = true; b = true; var a = false;
        String expectedOutput = "var a = false;\nb = a = true;";
        String input = "a = true;\nb = true;\nvar a = false;\n";
        String actualOutput = collapseVariableDeclarations.collapseVariables(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}
