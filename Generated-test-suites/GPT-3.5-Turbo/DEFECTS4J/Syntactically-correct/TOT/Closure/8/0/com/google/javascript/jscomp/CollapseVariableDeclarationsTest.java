import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    @Test
    public void testCollapseVariableDeclarations() {
        // Typical use case
        String input1 = "var a; var b = 1; var c = 2;";
        String expectedOutput1 = "var a, b = 1, c = 2;";
        assertEquals(expectedOutput, collapseVariableDeclarations.collapse(input1));

        // Edge cases
        String input2 = "var a, b = 1, c = 2;";
        String expectedOutput2 = "var a, b = 1, c = 2;";
        assertEquals(expectedOutput2, collapseVariableDeclarations.collapse(input2));

        String input3 = "var a = 1;";
        String expectedOutput3 = "var a = 1;";
        assertEquals(expectedOutput3, collapseVariableDeclarations.collapse(input3));

        String input4 = "";
        String expectedOutput4 = "";
        assertEquals(expectedOutput4, collapseVariableDeclarations.collapse(input4));

        // Error scenario
        String input5 = "var a; b; var c = 2;";
        String expectedOutput5 = "var a, var c = 2;";
        assertEquals(expectedOutput5, collapseVariableDeclarations.collapse(input5));
    }
}