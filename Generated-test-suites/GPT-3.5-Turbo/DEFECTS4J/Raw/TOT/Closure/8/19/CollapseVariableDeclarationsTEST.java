import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations instance;

    @Before
    public void setUp() {
        instance = new CollapseVariableDeclarations();
    }

    /**
     * Test of process method, typical use case.
     */
    @Test
    public void testProcess() {
        // Typical use case
        String code = "var a; var b = 1; var c = 2;";
        String expectedResult = "var a, b = 1, c = 2;";

        String result = instance.process(code);

        assertEquals(expectedResult, result);
    }

    /**
     * Test of process method, edge case with multiple variable declarations.
     */
    @Test
    public void testProcess_EdgeCase() {
        // Edge case with multiple variable declarations
        String code = "var x; var y; var z; var a = 1;";
        String expectedResult = "var x, y, z, a = 1;";

        String result = instance.process(code);

        assertEquals(expectedResult, result);
    }

    /**
     * Test of process method, edge case with variable assignments.
     */
    @Test
    public void testProcess_EdgeCase2() {
        // Edge case with variable assignments
        String code = "x = true; y = true; var z = true;";
        String expectedResult = "var z = y = x = true;";

        String result = instance.process(code);

        assertEquals(expectedResult, result);
    }

    /**
     * Test of process method, error scenario with invalid code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProcess_Error() {
        // Error scenario with invalid code
        String code = "var a = 1; var b = 2; var a = 3;";

        instance.process(code);
    }
}