import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.VarCheck;

public class VarCheckTest {
    private Compiler compiler;
    private VarCheck varCheck;

    @Before
    public void setup() {
        compiler = new Compiler();
        CompilerOptions options = new CompilerOptions();
        varCheck = new VarCheck(compiler);
        compiler.initOptions(options);
    }

    @Test
    public void testProcess_VariableDeclaredInSameFile_NoError() {
        String filename = "test.js";
        String sourceCode = "var x = 5;";
        compiler.addSourceCode(filename, sourceCode);
        compiler.getPassConfig().addPass(varCheck, false);
        assertTrue(compiler.getErrorManager().getErrorCount() == 0);
    }

    @Test
    public void testProcess_VariableDeclaredInAnotherFile_ErrorReported() {
        String filename1 = "test1.js";
        String filename2 = "test2.js";
        String sourceCode1 = "var x = 5;";
        String sourceCode2 = "console.log(x);";
        compiler.addSourceCode(filename1, sourceCode1);
        compiler.addSourceCode(filename2, sourceCode2);
        compiler.getPassConfig().addPass(varCheck, false);
        assertTrue(compiler.getErrorManager().getErrorCount() > 0);
    }

    @Test
    public void testProcess_ModuleDependencyDeclared_NoError() {
        String filename1 = "test1.js";
        String filename2 = "test2.js";
        String sourceCode1 = "goog.module('module1'); var x = 5;";
        String sourceCode2 = "goog.module('module2'); goog.require('module1'); console.log(module1.x);";
        compiler.addSourceCode(filename1, sourceCode1);
        compiler.addSourceCode(filename2, sourceCode2);
        compiler.getPassConfig().addPass(varCheck, false);
        assertTrue(compiler.getErrorManager().getErrorCount() == 0);
    }

    @Test
    public void testProcess_ModuleDependencyNotDeclared_ErrorReported() {
        String filename1 = "test1.js";
        String filename2 = "test2.js";
        String sourceCode1 = "goog.module('module1'); var x = 5;";
        String sourceCode2 = "goog.module('module2'); console.log(module1.x);";
        compiler.addSourceCode(filename1, sourceCode1);
        compiler.addSourceCode(filename2, sourceCode2);
        compiler.getPassConfig().addPass(varCheck, false);
        assertTrue(compiler.getErrorManager().getErrorCount() > 0);
    }

    @Test
    public void testProcess_InvalidFunctionDeclaration_ErrorReported() {
        String filename = "test.js";
        String sourceCode = "function() {}";
        compiler.addSourceCode(filename, sourceCode);
        compiler.getPassConfig().addPass(varCheck, false);
        assertTrue(compiler.getErrorManager().getErrorCount() > 0);
    }

    @Test
    public void testProcess_VariableAccessInExterns_NoEffectWarning() {
        String filename1 = "test1.js";
        String filename2 = "test2.js";
        String sourceCode1 = "function foo() {}";
        String sourceCode2 = "var x = test1.foo();";
        compiler.addSourceCode(filename1, sourceCode1);
        compiler.addSourceCode(filename2, sourceCode2);
        compiler.getPassConfig().addPass(varCheck, false);
        assertTrue(compiler.getErrorManager().getErrorCount() > 0);
    }

    @Test
    public void testProcess_UndefinedVariableInExterns_WarningReported() {
        String filename1 = "test1.js";
        String filename2 = "test2.js";
        String sourceCode1 = "";
        String sourceCode2 = "var x = y;";
        compiler.addSourceCode(filename1, sourceCode1);
        compiler.addSourceCode(filename2, sourceCode2);
        compiler.getPassConfig().addPass(varCheck, false);
        assertTrue(compiler.getErrorManager().getErrorCount() > 0);
    }

    @Test
    public void testProcess_SanityCheckMode_VarsToDeclareInExternsAdded() {
        String filename = "test.js";
        String sourceCode = "var x = y;";
        compiler.addSourceCode(filename, sourceCode);
        varCheck = new VarCheck(compiler, true);
        compiler.getPassConfig().addPass(varCheck, false);
        assertTrue(varCheck.varsToDeclareInExterns.contains("y"));
    }

    @Test
    public void testProcess_VariableDeclaredAsConstant_InSynthesizedExterns() {
        String filename = "test.js";
        String sourceCode = "const c = 10;";
        compiler.addSourceCode(filename, sourceCode);
        compiler.getPassConfig().addPass(varCheck, false);
        assertTrue(varCheck.getSynthesizedExternsRoot().hasChildren());
    }

    @Test
    public void testProcess_VariableDeclaredAsConstant_NotInSynthesizedExterns() {
        String filename = "test.js";
        String sourceCode = "const c = 10;";
        compiler.addSourceCode(filename, sourceCode);
        compiler.getCodingConvention().setConstant("c");
        compiler.getPassConfig().addPass(varCheck, false);
        assertFalse(varCheck.getSynthesizedExternsRoot().hasChildren());
    }

}