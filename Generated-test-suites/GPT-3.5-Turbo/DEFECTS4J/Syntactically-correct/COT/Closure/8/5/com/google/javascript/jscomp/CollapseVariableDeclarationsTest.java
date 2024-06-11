
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapse;

    @Before
    public void setUp() {
        collapse = new CollapseVariableDeclarations();
    }

    @Test
    public void testCollapseVariableDeclarations_basic() {
        // Given
        String code = "var a;\nvar b = 1;\nvar c = 2;";

        // When
        String result = collapse.collapseVariableDeclarations(code);

        // Then
        assertEquals("var a, b = 1, c = 2;", result);
    }

    @Test
    public void testCollapseVariableDeclarations_withAssignments() {
        // Given
        String code = "a = true;\nb = true;\nvar c = true;";

        // When
        String result = collapse.collapseVariableDeclarations(code);

        // Then
        assertEquals("var c = b = a = true;", result);
    }

    @Test
    public void testCollapseVariableDeclarations_noVariableDeclarations() {
        // Given
        String code = "var a = 1;\nvar b = 2;\nvar c = 3;";

        // When
        String result = collapse.collapseVariableDeclarations(code);

        // Then
        assertEquals("var a = 1;\nvar b = 2;\nvar c = 3;", result);
    }

    @Test
    public void testCollapseVariableDeclarations_noAssignments() {
        // Given
        String code = "var a;\nvar b;\nvar c;";

        // When
        String result = collapse.collapseVariableDeclarations(code);

        // Then
        assertEquals("var a, b, c;", result);
    }

    @Test
    public void testCollapseVariableDeclarations_emptyCode() {
        // Given
        String code = "";

        // When
        String result = collapse.collapseVariableDeclarations(code);

        // Then
        assertEquals("", result);
    }
}
