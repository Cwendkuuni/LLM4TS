
import static org.junit.Assert.*;
import org.junit.Test;

public class VarCheckTest {

    @Test
    public void testUndefinedVarError() {
        VarCheck varCheck = new VarCheck();
        
        // Test case 1: Var is not defined
        String varName = "testVar";
        DiagnosticType errorType = VarCheck.UNDEFINED_VAR_ERROR;
        String expectedErrorMessage = "variable " + varName + " is undefined";
        String actualErrorMessage = null;
        try {
            // Call the method that throws the error
            varCheck.undefinedVarError(varName);
        } catch (DiagnosticType e) {
            actualErrorMessage = e.getMessage();
        }
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testViolatedModuleDepError() {
        VarCheck varCheck = new VarCheck();
        
        // Test case 1: Variable reference violates module dependency
        String currentModuleName = "moduleA";
        String varModuleName = "moduleB";
        String varName = "testVar";
        DiagnosticType errorType = VarCheck.VIOLATED_MODULE_DEP_ERROR;
        String expectedErrorMessage = "module " + currentModuleName + " cannot reference " + varName + ", defined in module " + varModuleName + ", since " + varModuleName + " loads after " + currentModuleName;
        String actualErrorMessage = null;
        try {
            varCheck.violatedModuleDepError(currentModuleName, varModuleName, varName);
        } catch (DiagnosticType e) {
            actualErrorMessage = e.getMessage();
        }
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testMissingModuleDepError() {
        VarCheck varCheck = new VarCheck();
        
        // Test case 1: Missing module dependency
        String currentModuleName = "moduleA";
        String varModuleName = "moduleB";
        String varName = "testVar";
        DiagnosticType warningType = VarCheck.MISSING_MODULE_DEP_ERROR;
        String expectedWarningMessage = "missing module dependency; module " + currentModuleName + " should depend on module " + varModuleName + " because it references " + varName;
        String actualWarningMessage = null;
        try {
            varCheck.missingModuleDepError(currentModuleName, varModuleName, varName);
        } catch (DiagnosticType e) {
            actualErrorMessage = e.getMessage();
        }
        assertEquals(expectedWarningMessage, actualWarningMessage);
    }

    @Test
    public void testStrictModuleDepError() {
        VarCheck varCheck = new VarCheck();
        
        // Test case 1: Strict module dependency violation
        String currentModuleName = "moduleA";
        String varModuleName = "moduleB";
        String varName = "testVar";
        DiagnosticType errorType = VarCheck.STRICT_MODULE_DEP_ERROR;
        String expectedErrorMessage = "module " + currentModuleName + " cannot reference " + varName + ", defined in module " + varModuleName;
        String actualErrorMessage = null;
        try {
            varCheck.strictModuleDepError(currentModuleName, varModuleName, varName);
        } catch (DiagnosticType e) {
            actualErrorMessage = e.getMessage();
        }
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testNameReferenceInExternsError() {
        VarCheck varCheck = new VarCheck();
        
        // Test case 1: Name reference in externs
        String varName = "testVar";
        DiagnosticType warningType = VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR;
        String expectedWarningMessage = "accessing name " + varName + " in externs has no effect";
        String actualWarningMessage = null;
        try {
            varCheck.nameReferenceInExternsError(varName);
        } catch (DiagnosticType e) {
            actualWarningMessage = e.getMessage();
        }
        assertEquals(expectedWarningMessage, actualWarningMessage);
    }

    @Test
    public void testUndefinedExternVarError() {
        VarCheck varCheck = new VarCheck();
        
        // Test case 1: Undefined extern variable
        String varName = "testVar";
        DiagnosticType warningType = VarCheck.UNDEFINED_EXTERN_VAR_ERROR;
        String expectedWarningMessage = "name " + varName + " is not undefined in the externs.";
        String actualWarningMessage = null;
        try {
            varCheck.undefinedExternVarError(varName);
        } catch (DiagnosticType e) {
            actualWarningMessage = e.getMessage();
        }
        assertEquals(expectedWarningMessage, actualWarningMessage);
    }

    @Test
    public void testInvalidFunctionDecl() {
        VarCheck varCheck = new VarCheck();
        
        // Test case 1: Invalid function declaration
        DiagnosticType errorType = VarCheck.INVALID_FUNCTION_DECL;
        String expectedErrorMessage = "Syntax error: function declaration must have a name";
        String actualErrorMessage = null;
        try {
            varCheck.invalidFunctionDecl();
        } catch (DiagnosticType e) {
            actualErrorMessage = e.getMessage();
        }
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
}
