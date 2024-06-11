import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

  @Test
  public void testUndefinedVarError() {
    VarCheck varCheck = new VarCheck();
    
    // Test case for undefined variable
    // - Input: VarCheck.UNDEFINED_VAR_ERROR, "varName"
    // - Expected output: DiagnosticType error with message "variable varName is undefined"
    varCheck.process("varName");
    
    // Check if the error is reported correctly
    assertEquals(varCheck.getErrors().get(0).getType(), VarCheck.UNDEFINED_VAR_ERROR);
    assertEquals(varCheck.getErrors().get(0).getMessage(), "variable varName is undefined");
  }

  @Test
  public void testViolatedModuleDepError() {
    VarCheck varCheck = new VarCheck();
    
    // Test case for violated module dependency
    // - Input: VarCheck.VIOLATED_MODULE_DEP_ERROR, "currModule", "varModule", "varName"
    // - Expected output: DiagnosticType error with message "module currModule cannot reference varName, defined in module varModule, since varModule loads after currModule"
    varCheck.process("currModule", "varModule", "varName");
    
    // Check if the error is reported correctly
    assertEquals(varCheck.getErrors().get(0).getType(), VarCheck.VIOLATED_MODULE_DEP_ERROR);
    assertEquals(varCheck.getErrors().get(0).getMessage(), "module currModule cannot reference varName, defined in module varModule, since varModule loads after currModule");
  }

  @Test
  public void testMissingModuleDepError() {
    VarCheck varCheck = new VarCheck();
    
    // Test case for missing module dependency
    // - Input: VarCheck.MISSING_MODULE_DEP_ERROR, "currModule", "varModule", "varName"
    // - Expected output: DiagnosticType warning with message "missing module dependency; module currModule should depend on module varModule because it references varName"
    varCheck.process("currModule", "varModule", "varName");
    
    // Check if the warning is reported correctly
    assertEquals(varCheck.getErrors().get(0).getType(), VarCheck.MISSING_MODULE_DEP_ERROR);
    assertEquals(varCheck.getErrors().get(0).getMessage(), "missing module dependency; module currModule should depend on module varModule because it references varName");
  }

  @Test
  public void testStrictModuleDepError() {
    VarCheck varCheck = new VarCheck();
    
    // Test case for strict module dependency
    // - Input: VarCheck.STRICT_MODULE_DEP_ERROR, "currModule", "varModule", "varName"
    // - Expected output: DiagnosticType disabled with message "module currModule cannot reference varName, defined in module varModule"
    varCheck.process("currModule", "varModule", "varName");
    
    // Check if the disabled error is reported correctly
    assertEquals(varCheck.getErrors().get(0).getType(), VarCheck.STRICT_MODULE_DEP_ERROR);
    assertEquals(varCheck.getErrors().get(0).getMessage(), "module currModule cannot reference varName, defined in module varModule");
  }

  @Test
  public void testNameReferenceInExternsError() {
    VarCheck varCheck = new VarCheck();
    
    // Test case for name reference in externs error
    // - Input: VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR, "name"
    // - Expected output: DiagnosticType warning with message "accessing name name in externs has no effect"
    varCheck.process("name");
    
    // Check if the warning is reported correctly
    assertEquals(varCheck.getErrors().get(0).getType(), VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR);
    assertEquals(varCheck.getErrors().get(0).getMessage(), "accessing name name in externs has no effect");
  }

  @Test
  public void testUndefinedExternVarError() {
    VarCheck varCheck = new VarCheck();
    
    // Test case for undefined extern variable
    // - Input: VarCheck.UNDEFINED_EXTERN_VAR_ERROR, "name"
    // - Expected output: DiagnosticType warning with message "name name is not undefined in the externs."
    varCheck.process("name");
    
    // Check if the warning is reported correctly
    assertEquals(varCheck.getErrors().get(0).getType(), VarCheck.UNDEFINED_EXTERN_VAR_ERROR);
    assertEquals(varCheck.getErrors().get(0).getMessage(), "name name is not undefined in the externs.");
  }

  @Test
  public void testInvalidFunctionDecl() {
    VarCheck varCheck = new VarCheck();
    
    // Test case for invalid function declaration
    // - Input: VarCheck.INVALID_FUNCTION_DECL
    // - Expected output: DiagnosticType error with message "Syntax error: function declaration must have a name"
    varCheck.process("");
    
    // Check if the error is reported correctly
    assertEquals(varCheck.getErrors().get(0).getType(), VarCheck.INVALID_FUNCTION_DECL);
    assertEquals(varCheck.getErrors().get(0).getMessage(), "Syntax error: function declaration must have a name");
  }

}