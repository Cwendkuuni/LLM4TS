
import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        collapseVariableDeclarations = new CollapseVariableDeclarations();
    }

    @Test
    public void testCollapseVariableDeclarations() {
        // Test case for collapsing multiple variable declarations
        // var a;
        // var b = 1;
        // var c = 2;
        // should become:
        // var a, b = 1, c = 2;

        String code = "var a;\nvar b = 1;\nvar c = 2;";
        String expected = "var a, b = 1, c = 2;";

        String actual = collapseVariableDeclarations.collapseVariableDeclarations(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseAssigns() {
        // Test case for collapsing assigns
        // a = true;
        // b = true;
        // var c = true;
        // should become:
        // var c = b = a = true;

        String code = "a = true;\nb = true;\nvar c = true;";
        String expected = "var c = b = a = true;";

        String actual = collapseVariableDeclarations.collapseVariableDeclarations(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseVariableDeclarationsAndAssigns() {
        // Test case for collapsing both variable declarations and assigns
        // var a;
        // b = true;
        // var c = 1;
        // d = 2;
        // should become:
        // var a, c = 1, b = d = true;

        String code = "var a;\nb = true;\nvar c = 1;\nd = 2;";
        String expected = "var a, c = 1, b = d = true;";

        String actual = collapseVariableDeclarations.collapseVariableDeclarations(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseVariableDeclarationsWithEdgeCases() {
        // Test case for edge cases in variable declarations
        // var a;
        // var b, c;
        // var d = 1;
        // should become:
        // var a, b, c, d = 1;

        String code = "var a;\nvar b, c;\nvar d = 1;";
        String expected = "var a, b, c, d = 1;";

        String actual = collapseVariableDeclarations.collapseVariableDeclarations(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseAssignsWithEdgeCases() {
        // Test case for edge cases in assigns
        // a = true;
        // c = d = true;
        // var b = 1;
        // should become:
        // var b = 1, c = d = a = true;

        String code = "a = true;\nc = d = true;\nvar b = 1;";
        String expected = "var b = 1, c = d = a = true;";

        String actual = collapseVariableDeclarations.collapseVariableDeclarations(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseVariableDeclarationsAndAssignsWithEdgeCases() {
        // Test case for edge cases in variable declarations and assigns
        // var a;
        // b = true;
        // c = d = 1;
        // should become:
        // var a, b, c = d = b = true;

        String code = "var a;\nb = true;\nc = d = 1;";
        String expected = "var a, b, c = d = b = true;";

        String actual = collapseVariableDeclarations.collapseVariableDeclarations(code);
        assertEquals(expected, actual);
    }
}
