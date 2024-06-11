import com.google.javascript.jscomp.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VarCheckTest {

    private Compiler compiler;

    @Before
    public void setup() {
        compiler = new Compiler();
        compiler.initOptions(new CompilerOptions());
    }

    @Test
    public void testProcess_WithUndefinedVarError_ShouldReportError() {
        VarCheck varCheck = new VarCheck(compiler);
        String jsCode = "function foo() { bar(); }";
        Node externs = new Node(Token.EXTERN_FUNCTIONS);
        Node root = compiler.parseTestCode(jsCode);
        varCheck.process(externs, root);

        JSError[] errors = compiler.getErrors();
        assertEquals(1, errors.length);
        assertEquals("JSC_UNDEFINED_VARIABLE", errors[0].getType().key);
        assertEquals("bar", errors[0].description);
    }

    @Test
    public void testProcess_WithViolatedModuleDepError_ShouldReportError() {
        VarCheck varCheck = new VarCheck(compiler);
        String jsCode = "(function() { var a = b; })();";
        Node externs = new Node(Token.EXTERN_FUNCTIONS);
        Node root = compiler.parseTestCode(jsCode);
        compiler.getModuleGraph().addModule(new JSModule("module1"));
        compiler.getModuleGraph().addModule(new JSModule("module2"));
        compiler.getInput(new InputId("input1")).setModule(compiler.getModuleGraph().getModuleByName("module1"));
        compiler.getInput(new InputId("input2")).setModule(compiler.getModuleGraph().getModuleByName("module2"));

        varCheck.process(externs, root);

        JSError[] errors = compiler.getErrors();
        assertEquals(1, errors.length);
        assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", errors[0].getType().key);
        assertEquals("module1", errors[0].description);
        assertEquals("module2", errors[0].descriptionArguments[1]);
        assertEquals("b", errors[0].descriptionArguments[2]);
    }

    @Test
    public void testProcess_WithMissingModuleDepWarning_ShouldReportWarning() {
        VarCheck varCheck = new VarCheck(compiler);
        String jsCode = "(function() { var a = b; })();";
        Node externs = new Node(Token.EXTERN_FUNCTIONS);
        Node root = compiler.parseTestCode(jsCode);
        compiler.getModuleGraph().addModule(new JSModule("module1"));
        compiler.getModuleGraph().addModule(new JSModule("module2"));
        compiler.getInput(new InputId("input1")).setModule(compiler.getModuleGraph().getModuleByName("module1"));
        compiler.getInput(new InputId("input2")).setModule(compiler.getModuleGraph().getModuleByName("module2"));

        varCheck.process(externs, root);

        JSError[] warnings = compiler.getWarnings();
        assertEquals(1, warnings.length);
        assertEquals("JSC_MISSING_MODULE_DEPENDENCY", warnings[0].getType().key);
        assertEquals("module1", warnings[0].description);
        assertEquals("module2", warnings[0].descriptionArguments[1]);
        assertEquals("b", warnings[0].descriptionArguments[2]);
    }

    @Test
    public void testProcess_WithStrictModuleDepError_ShouldReportError() {
        VarCheck varCheck = new VarCheck(compiler);
        String jsCode = "(function() { var a = b; })();";
        Node externs = new Node(Token.EXTERN_FUNCTIONS);
        Node root = compiler.parseTestCode(jsCode);
        compiler.getModuleGraph().addModule(new JSModule("module1"));
        compiler.getModuleGraph().addModule(new JSModule("module2"));
        compiler.getInput(new InputId("input1")).setModule(compiler.getModuleGraph().getModuleByName("module1"));
        compiler.getInput(new InputId("input2")).setModule(compiler.getModuleGraph().getModuleByName("module2"));

        varCheck.process(externs, root);

        JSError[] errors = compiler.getErrors();
        assertEquals(1, errors.length);
        assertEquals("JSC_STRICT_MODULE_DEPENDENCY", errors[0].getType().key);
        assertEquals("module1", errors[0].description);
        assertEquals("module2", errors[0].descriptionArguments[1]);
        assertEquals("b", errors[0].descriptionArguments[2]);
    }

    @Test
    public void testProcess_WithNameReferenceInExternsWarning_ShouldReportWarning() {
        VarCheck varCheck = new VarCheck(compiler);
        String jsCode = "(function() { var a = b; })();";
        Node externs = compiler.parseTestCode("var b;");
        Node root = compiler.parseTestCode(jsCode);
        compiler.getModuleGraph().addModule(new JSModule("module1"));
        compiler.getModuleGraph().addModule(new JSModule("module2"));
        compiler.getInput(new InputId("input1")).setModule(compiler.getModuleGraph().getModuleByName("module1"));
        compiler.getInput(new InputId("input2")).setModule(compiler.getModuleGraph().getModuleByName("module2"));

        varCheck.process(externs, root);

        JSError[] warnings = compiler.getWarnings();
        assertEquals(1, warnings.length);
        assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", warnings[0].getType().key);
        assertEquals("b", warnings[0].description);
    }

    @Test
    public void testProcess_WithUndefinedExternVarWarning_ShouldReportWarning() {
        VarCheck varCheck = new VarCheck(compiler);
        String jsCode = "(function() { var a = b; })();";
        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode(jsCode);
        compiler.getModuleGraph().addModule(new JSModule("module1"));
        compiler.getModuleGraph().addModule(new JSModule("module2"));
        compiler.getInput(new InputId("input1")).setModule(compiler.getModuleGraph().getModuleByName("module1"));
        compiler.getInput(new InputId("input2")).setModule(compiler.getModuleGraph().getModuleByName("module2"));

        varCheck.process(externs, root);

        JSError[] warnings = compiler.getWarnings();
        assertEquals(1, warnings.length);
        assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR", warnings[0].getType().key);
        assertEquals("b", warnings[0].description);
    }

    @Test
    public void testProcess_WithInvalidFunctionDeclError_ShouldReportError() {
        VarCheck varCheck = new VarCheck(compiler);
        String jsCode = "(function() { function () {} })();";
        Node externs = new Node(Token.EXTERN_FUNCTIONS);
        Node root = compiler.parseTestCode(jsCode);

        varCheck.process(externs, root);

        JSError[] errors = compiler.getErrors();
        assertEquals(1, errors.length);
        assertEquals("JSC_INVALID_FUNCTION_DECL", errors[0].getType().key);
        assertEquals("Syntax error: function declaration must have a name", errors[0].description);
    }
}