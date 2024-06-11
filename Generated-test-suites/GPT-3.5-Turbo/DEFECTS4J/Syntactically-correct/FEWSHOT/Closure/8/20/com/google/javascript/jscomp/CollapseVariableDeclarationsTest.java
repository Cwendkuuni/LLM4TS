import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testProcess_emptySource() {
        Compiler compiler = new Compiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode("");

        pass.process(externs, root);

        // Assert code change
        assertTrue(compiler.hasCodeChanged());

        // Assert changes are applied correctly
        String expectedOutput = "\n";
        String actualOutput = compiler.toSource();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_singleVariableDeclaration() {
        Compiler compiler = new Compiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node externs = compiler.parseTestCode("var a;");
        Node root = compiler.parseTestCode("var a;");

        pass.process(externs, root);

        // Assert code change
        assertFalse(compiler.hasCodeChanged());

        // Assert changes are applied correctly
        String expectedOutput = "var a;\n";
        String actualOutput = compiler.toSource();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_multipleVariableDeclarations() {
        Compiler compiler = new Compiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node externs = compiler.parseTestCode("var a; var b;");
        Node root = compiler.parseTestCode("var a; var b;");

        pass.process(externs, root);

        // Assert code change
        assertTrue(compiler.hasCodeChanged());

        // Assert changes are applied correctly
        String expectedOutput = "var a, b;\n";
        String actualOutput = compiler.toSource();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_variableAssignments() {
        Compiler compiler = new Compiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node externs = compiler.parseTestCode("a = true; b = true; var c = true;");
        Node root = compiler.parseTestCode("a = true; b = true; var c = true;");

        pass.process(externs, root);

        // Assert code change
        assertTrue(compiler.hasCodeChanged());

        // Assert changes are applied correctly
        String expectedOutput = "var c = true; b = true; a = true;\n";
        String actualOutput = compiler.toSource();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_variableDeclarationsAndAssignments() {
        Compiler compiler = new Compiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node externs = compiler.parseTestCode("var a; a = true; var b; b = true; var c = true;");
        Node root = compiler.parseTestCode("var a; a = true; var b; b = true; var c = true;");

        pass.process(externs, root);

        // Assert code change
        assertTrue(compiler.hasCodeChanged());

        // Assert changes are applied correctly
        String expectedOutput = "var a = true; var b = true; var c = true;\n";
        String actualOutput = compiler.toSource();
        assertEquals(expectedOutput, actualOutput);
    }

}