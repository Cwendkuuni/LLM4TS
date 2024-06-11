import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new Compiler();
    }

    @Test
    public void shouldFlagUnsafeUseOfGlobalThis() {
        // Create the JavaScript code to test
        String code = "function evil() { this.useful = undefined; }";

        // Parse the code into an AST
        CompilerOptions options = new CompilerOptions();
        CompilerInput input = new CompilerInput(SourceFile.fromCode("test", code));
        Node rootNode = compiler.parse(input);

        // Create the CheckGlobalThis instance to test
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        // Traverse the AST and check for global this usage
        NodeTraversal.traverse(compiler, rootNode, checkGlobalThis);

        // Get the warnings reported by the compiler
        JSError[] warnings = compiler.getWarnings();

        // Verify that the warning for dangerous use of global this is present
        assertNotNull(warnings);
        assertTrue(warnings.length > 0);
        for (JSError warning : warnings) {
            if (warning.getType() == CheckGlobalThis.GLOBAL_THIS) {
                return;
            }
        }
        fail("Expected warning for dangerous use of global this");
    }

    @Test
    public void shouldNotFlagSafeUseOfGlobalThis() {
        // Create the JavaScript code to test
        String code = "function safe() { var a = this; a.useful = undefined; }";

        // Parse the code into an AST
        CompilerOptions options = new CompilerOptions();
        CompilerInput input = new CompilerInput(SourceFile.fromCode("test", code));
        Node rootNode = compiler.parse(input);

        // Create the CheckGlobalThis instance to test
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        // Traverse the AST and check for global this usage
        NodeTraversal.traverse(compiler, rootNode, checkGlobalThis);

        // Get the warnings reported by the compiler
        JSError[] warnings = compiler.getWarnings();

        // Verify that no warning is reported
        assertNull(warnings);
    }
}