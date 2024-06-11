import com.google.javascript.jscomp.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    private CheckSideEffects checkSideEffects;

    @Before
    public void setUp() {
        CompilerOptions options = new CompilerOptions();
        Compiler compiler = new Compiler();
        compiler.initOptions(options);
        // Set up the necessary dependencies for the CheckSideEffects class
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checkSideEffects.problemNodes.clear(); // Clear the previous test's problem nodes
        // Add any additional necessary dependencies for the CheckSideEffects class
    }

    // Test for process method
    @Test
    public void testProcess() {
        String sourceCode = "var x = 'this string is ' \n" +
                "        'continued on the next line but you forgot the +';\n" +
                "x == foo();\n" +
                "foo();;\n";

        Compiler compiler = new Compiler();
        Node externs = compiler.parseTestCode("var externs = {};");

        Node root = compiler.parseTestCode(sourceCode);

        checkSideEffects.process(externs, root);

        // Assert that the problemNodes list contains the correct number of problem nodes
        assertEquals(3, checkSideEffects.problemNodes.size());
    }

    // Test for hotSwapScript method
    @Test
    public void testHotSwapScript() {
        String sourceCode = "var x = 'this string is ' \n" +
                "        'continued on the next line but you forgot the +';\n" +
                "x == foo();\n" +
                "foo();;\n";

        Compiler compiler = new Compiler();
        Node scriptRoot = compiler.parseTestCode(sourceCode);
        Node originalRoot = compiler.parseTestCode(sourceCode);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Assert that the problemNodes list contains the correct number of problem nodes
        assertEquals(3, checkSideEffects.problemNodes.size());
    }

    // Test for visit method
    @Test
    public void testVisit() {
        String sourceCode = "var x = 'this string is ' \n" +
                "        'continued on the next line but you forgot the +';\n" +
                "x == foo();\n" +
                "foo();;\n";

        Compiler compiler = new Compiler();
        Node externs = compiler.parseTestCode("var externs = {};");

        Node root = compiler.parseTestCode(sourceCode);

        NodeTraversal.traverse(compiler, root, checkSideEffects);

        // Assert that the problemNodes list contains the correct number of problem nodes
        assertEquals(3, checkSideEffects.problemNodes.size());
    }

    // Test for protectSideEffects method
    @Test
    public void testProtectSideEffects() {
        String sourceCode = "var x = 'this string is ' \n" +
                "        'continued on the next line but you forgot the +';\n" +
                "x == foo();\n" +
                "foo();;\n";

        Compiler compiler = new Compiler();
        Node externs = compiler.parseTestCode("var externs = {};");

        Node root = compiler.parseTestCode(sourceCode);

        NodeTraversal.traverse(compiler, root, checkSideEffects);
        checkSideEffects.protectSideEffects();

        // Assert that the protected side-effect free code is correctly modified
        String expectedCode = "var x = JSCOMPILER_PRESERVE('this string is ' \n" +
                "        'continued on the next line but you forgot the +');\n" +
                "JSCOMPILER_PRESERVE(x == foo());\n" +
                "JSCOMPILER_PRESERVE(foo());;\n";
        assertEquals(expectedCode, compiler.toSource(root.getFirstChild()));
    }

    // Test for addExtern method
    @Test
    public void testAddExtern() {
        String sourceCode = "var x = 'this string is ' \n" +
                "        'continued on the next line but you forgot the +';\n" +
                "x == foo();\n" +
                "foo();;\n";

        Compiler compiler = new Compiler();
        Node externs = compiler.parseTestCode("var externs = {};");

        Node root = compiler.parseTestCode(sourceCode);

        NodeTraversal.traverse(compiler, root, checkSideEffects);
        checkSideEffects.protectSideEffects();
        checkSideEffects.addExtern();

        // Assert that the extern function is correctly added to the AST
        String expectedCode = "var x = JSCOMPILER_PRESERVE('this string is ' \n" +
                "        'continued on the next line but you forgot the +');\n" +
                "JSCOMPILER_PRESERVE(x == foo());\n" +
                "JSCOMPILER_PRESERVE(foo());;\n" +
                "var JSCOMPILER_PRESERVE;" +
                "";
        assertEquals(expectedCode, compiler.toSource(externs));
    }

    // Test for StripProtection class
    @Test
    public void testStripProtection() {
        String sourceCode = "var x = 'this string is ' \n" +
                "        'continued on the next line but you forgot the +';\n" +
                "x == foo();\n" +
                "foo();;\n";

        Compiler compiler = new Compiler();
        Node externs = compiler.parseTestCode("var externs = {};");

        Node root = compiler.parseTestCode(sourceCode);

        NodeTraversal.traverse(compiler, root, checkSideEffects);
        checkSideEffects.protectSideEffects();
        checkSideEffects.addExtern();

        Node externsForStripProtection = compiler.parseTestCode(compiler.toSource(externs));
        Node rootForStripProtection = compiler.parseTestCode(compiler.toSource(root));

        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        stripProtection.process(externsForStripProtection, rootForStripProtection);

        // Assert that the protection has been stripped from the code
        String expectedCode = "var x = 'this string is ' \n" +
                "        'continued on the next line but you forgot the +';\n" +
                "x == foo();\n" +
                "foo();;\n";
        assertEquals(expectedCode, compiler.toSource(rootForStripProtection));
    }

}