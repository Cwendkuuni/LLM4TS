import static org.junit.Assert.*;
import org.junit.Test;

public class VarCheckTest {
    @Test
    public void testProcess_WithUndefinedVarError() {
        // Set up
        String input = "function add(a, b) { return a + x; }";
        Compiler compiler = new Compiler();
        CompilerOptions options = new CompilerOptions();
        compiler.initOptions(options);
        compiler.getOptions().setWarningLevel(DiagnosticGroups.UNDEFINED_VARIABLES, CheckLevel.ERROR);
        compiler.setFeatureSet(FeatureSet.latest().without(Feature.CONSTANT_NAME_CHECK));
        JSSourceFile source = JSSourceFile.fromCode("test.js", input);
        compiler.compile(SourceFile.fromCode("externs.js", ""), source, options);

        // Assertions
        assertEquals(1, compiler.getErrorCount());
        assertEquals("variable x is undefined", compiler.getErrors()[0].getDescription());
    }

    @Test
    public void testVisit_WithInvalidFunctionDeclarationError() {
        // Set up
        String input = "function() {}";
        Compiler compiler = new Compiler();
        JSErrorReporter errorReporter = new JSErrorReporter();
        compiler.setErrorManager(errorReporter);
        compiler.setFeatureSet(FeatureSet.latest().without(Feature.CONSTANT_NAME_CHECK));
        Node root = compiler.parseTestCode(input);
        CompilerOptions options = new CompilerOptions();
        VarCheck varCheck = new VarCheck(compiler);
        NodeTraversal.traverse(compiler, root, varCheck);
        
        // Assertions
        assertEquals(1, errorReporter.getCount());
        assertEquals("Syntax error: function declaration must have a name", errorReporter.getErrors().get(0).getDescription());
    }
}