import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        CollapseVariableDeclarations c = new CollapseVariableDeclarations();

        // Test case 1
        String source1 = "var a;\nvar b = 1;\nvar c = 2;";
        String expected1 = "var a, b = 1, c = 2;";
        assertEquals(expected1, c.collapse(source1));

        // Test case 2
        String source2 = "a = true;\nb = true;\nvar c = true;";
        String expected2 = "var c = b = a = true;";
        assertEquals(expected2, c.collapse(source2));

        // Test case 3
        String source3 = "var x;\nvar y;\nvar z;";
        String expected3 = "var x, y, z;";
        assertEquals(expected3, c.collapse(source3));

        // Test case 4
        String source4 = "a = 5;\nb = 10;";
        String expected4 = "var b = a = 5;";
        assertEquals(expected4, c.collapse(source4));
    }
}