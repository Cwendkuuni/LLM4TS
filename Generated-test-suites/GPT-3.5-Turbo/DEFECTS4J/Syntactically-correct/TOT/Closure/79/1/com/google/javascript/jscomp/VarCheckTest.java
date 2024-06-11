import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.VarCheck;

import static org.junit.Assert.*;

public class VarCheckTest {

    private CompilerOptions options;
    private VarCheck varCheck;

    @Before
    public void setUp() {
        options = new CompilerOptions();
        varCheck = new VarCheck(new Compiler(), false);
    }

    @Test
    public void testProcess_undefinedVar() {
        String source = "function test() { console.log(x); }";
        Compiler compiler = new Compiler();
        compiler.compile(SourceFile.fromCode("test.js", source), options);
        JSError[] errors = compiler.getErrors();
        for (JSError error : errors) {
            assertEquals(VarCheck.UNDEFINED_VAR_ERROR, error.getType());
        }
    }

    @Test
    public void testProcess_moduleDependencyViolation() {
        String source = "import { x } from 'module1'; console.log(x);";
        Compiler compiler = new Compiler();
        compiler.compile(SourceFile.fromCode("test.js", source), options);
        JSError[] errors = compiler.getErrors();
        for (JSError error : errors) {
            assertEquals(VarCheck.VIOLATED_MODULE_DEP_ERROR, error.getType());
        }
    }

    @Test
    public void testProcess_missingModuleDependency() {
        String source = "import { x } from 'module1'; function test() { console.log(x); }";
        Compiler compiler = new Compiler();
        compiler.compile(SourceFile.fromCode("test.js", source), options);
        JSError[] errors = compiler.getErrors();
        for (JSError error : errors) {
            assertEquals(VarCheck.MISSING_MODULE_DEP_ERROR, error.getType());
        }
    }

    @Test
    public void testProcess_globalVarInExterns() {
        String source = "function test() { console.log(window); }";
        Compiler compiler = new Compiler();
        compiler.compile(SourceFile.fromCode("test.js", source), options);
        JSError[] warnings = compiler.getWarnings();
        for (JSError warning : warnings) {
            assertEquals(VarCheck.UNDEFINED_EXTERN_VAR_ERROR, warning.getType());
        }
    }

    @Test
    public void testProcess_functionDeclarationWithoutName() {
        String source = "var test = function() {}; function() { console.log(test); }";
        Compiler compiler = new Compiler();
        compiler.compile(SourceFile.fromCode("test.js", source), options);
        JSError[] errors = compiler.getErrors();
        for (JSError error : errors) {
            assertEquals(VarCheck.INVALID_FUNCTION_DECL, error.getType());
        }
    }

}