
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();

        // Test case 1: Empty variable declaration
        String result1 = collapse.collapseVariableDeclarations("var a;");
        assertEquals("var a;", result1);

        // Test case 2: Variable declaration with initializer
        String result2 = collapse.collapseVariableDeclarations("var b = 1;");
        assertEquals("var b = 1;", result2);

        // Test case 3: Multiple variable declarations
        String result3 = collapse.collapseVariableDeclarations("var c; var d = 2;");
        assertEquals("var c, d = 2;", result3);

        // Test case 4: Assignments with variable declaration
        String result4 = collapse.collapseVariableDeclarations("a = true; b = true; var c = true;");
        assertEquals("var c = b = a = true;", result4);
    }

    @Test
    public void testCollapseVariableDeclarations_edgeCases() {
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();

        // Test case 1: No variable declarations
        String result1 = collapse.collapseVariableDeclarations("");
        assertEquals("", result1);

        // Test case 2: Variable declaration with multiple assignments
        String result2 = collapse.collapseVariableDeclarations("var e = f = g = 3;");
        assertEquals("var e = f = g = 3;", result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollapseVariableDeclarations_exception() {
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();

        // Test case: Invalid input
        collapse.collapseVariableDeclarations("Invalid input");
    }
}

