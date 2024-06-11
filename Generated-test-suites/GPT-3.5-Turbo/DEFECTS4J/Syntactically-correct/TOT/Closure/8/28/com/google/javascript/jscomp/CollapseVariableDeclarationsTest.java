import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.WarningLevel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollapseVariableDeclarationsTest {

    private Compiler compiler;
    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() throws Exception {
        compiler = new Compiler();
        CompilerOptions options = new CompilerOptions();
        options.setWarningLevel(WarningLevel.DEFAULT);
        compiler.initOptions(options);
        collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    }

    @Test
    public void testCollapseVariableDeclarations_SingleVariableDeclarations() {
        String input = "" +
                "var a;\n" +
                "var b = 1;\n" +
                "var c = 2;\n";
        String expected = "" +
                "var a, b = 1, c = 2;";
        String actual = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseVariableDeclarations_Assignments() {
        String input = "" +
                "a = true;\n" +
                "b = true;\n" +
                "var c = true;\n";
        String expected = "" +
                "var c = b = a = true;";
        String actual = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseVariableDeclarations_MultipleScopes() {
        String input = "" +
                "var a = 1;\n" +
                "if (true) {\n" +
                "  var b = 2;\n" +
                "}\n" +
                "function myFunction() {\n" +
                "  var c = 3;\n" +
                "}\n";
        String expected = "" +
                "var a = 1;\n" +
                "if (true) {\n" +
                "  b = 2;\n" +
                "}\n" +
                "function myFunction() {\n" +
                "  var c = 3;\n" +
                "}\n";
        String actual = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseVariableDeclarations_NoVariablesToCollapse() {
        String input = "" +
                "var a = 1;\n" +
                "var b = 2;\n" +
                "var c = 3;\n";
        String expected = "" +
                "var a = 1;\n" +
                "var b = 2;\n" +
                "var c = 3;\n";
        String actual = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseVariableDeclarations_EmptyInput() {
        String input = "";
        String expected = "";
        String actual = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollapseVariableDeclarations_NullInput() {
        String input = null;
        String expected = "";
        String actual = collapseVariableDeclarations.collapseVariableDeclarations(input);
        assertEquals(expected, actual);
    }
}