
import org.junit.Assert;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();
        
        // Test case 1: var a; var b = 1; var c = 2;
        String input1 = "var a;\nvar b = 1;\nvar c = 2;";
        String expectedOutput1 = "var a, b = 1, c = 2;";
        Assert.assertEquals(expectedOutput1, collapseVariableDeclarations.collapseDeclarations(input1));
        
        // Test case 2: var a = true; var b = true; var c = true;
        String input2 = "var a = true;\nvar b = true;\nvar c = true;";
        String expectedOutput2 = "var a = true, b = true, c = true;";
        Assert.assertEquals(expectedOutput2, collapseVariableDeclarations.collapseDeclarations(input2));
        
        // Test case 3: var a = 1; var b; var c = 2; var d = 3;
        String input3 = "var a = 1;\nvar b;\nvar c = 2;\nvar d = 3;";
        String expectedOutput3 = "var a = 1, b, c = 2, d = 3;";
        Assert.assertEquals(expectedOutput3, collapseVariableDeclarations.collapseDeclarations(input3));
    }

    @Test
    public void testCollapseVariableDeclarationsWithEdgeCasesAndExceptions() {
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();
        
        // Test case 1: Empty input
        String input1 = "";
        String expectedOutput1 = "";
        Assert.assertEquals(expectedOutput1, collapseVariableDeclarations.collapseDeclarations(input1));
        
        // Test case 2: No variable declarations
        String input2 = "console.log('Hello, world!');";
        String expectedOutput2 = "console.log('Hello, world!');";
        Assert.assertEquals(expectedOutput2, collapseVariableDeclarations.collapseDeclarations(input2));
        
        // Test case 3: Single variable declaration
        String input3 = "var a = 1;";
        String expectedOutput3 = "var a = 1;";
        Assert.assertEquals(expectedOutput3, collapseVariableDeclarations.collapseDeclarations(input3));
        
        // Test case 4: Single variable declaration with assignment
        String input4 = "var a; a = 1;";
        String expectedOutput4 = "var a = 1;";
        Assert.assertEquals(expectedOutput4, collapseVariableDeclarations.collapseDeclarations(input4));
        
        // Test case 5: Multiple variable declarations with assignments
        String input5 = "var a = 1; var b; var c = 2;";
        String expectedOutput5 = "var a = 1, b, c = 2;";
        Assert.assertEquals(expectedOutput5, collapseVariableDeclarations.collapseDeclarations(input5));
    }
}
