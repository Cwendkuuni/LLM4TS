
import org.junit.Assert;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        // Create an instance of the CollapseVariableDeclarations class
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

        // Test case 1: var a; var b = 1; var c = 2;
        String input1 = "var a;\nvar b = 1;\nvar c = 2;";
        String expected1 = "var a, b = 1, c = 2;";
        String result1 = collapseVariableDeclarations.collapse(input1);
        Assert.assertEquals(expected1, result1);

        // Test case 2: a = true; b = true; var c = true;
        String input2 = "a = true;\nb = true;\nvar c = true;";
        String expected2 = "var c = b = a = true;";
        String result2 = collapseVariableDeclarations.collapse(input2);
        Assert.assertEquals(expected2, result2);
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        // Create an instance of the CollapseVariableDeclarations class
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

        // Test case 1: Empty input
        String input1 = "";
        String expected1 = "";
        String result1 = collapseVariableDeclarations.collapse(input1);
        Assert.assertEquals(expected1, result1);

        // Test case 2: Already collapsed input
        String input2 = "var a, b = 1, c = 2;";
        String expected2 = "var a, b = 1, c = 2;";
        String result2 = collapseVariableDeclarations.collapse(input2);
        Assert.assertEquals(expected2, result2);

        // Test case 3: Single variable declaration
        String input3 = "var a;";
        String expected3 = "var a;";
        String result3 = collapseVariableDeclarations.collapse(input3);
        Assert.assertEquals(expected3, result3);

        // Test case 4: Single variable assignment
        String input4 = "a = true;";
        String expected4 = "a = true;";
        String result4 = collapseVariableDeclarations.collapse(input4);
        Assert.assertEquals(expected4, result4);
    }
}

