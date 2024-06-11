import static org.junit.Assert.*;
import org.junit.Test;

public class VarCheckTest {
  
  @Test
  public void testUNDEFINED_VAR_ERROR() {
    VarCheck varCheck = new VarCheck();
    // Test for undefined variable error
    // edge case: variable is undefined
    try {
      varCheck.process("externs", "root");
      fail("Expected UNDEFINED_VAR_ERROR");
    } catch (Exception e) {
      assertEquals("variable {0} is undefined", e.getMessage());
    }
  }
  
  @Test
  public void testVIOLATED_MODULE_DEP_ERROR() {
    VarCheck varCheck = new VarCheck();
    // Test for violated module dependency error
    // edge case: referencing variable across modules without proper declaration
    try {
      varCheck.process("externs", "root");
      fail("Expected VIOLATED_MODULE_DEP_ERROR");
    } catch (Exception e) {
      assertEquals("module {0} cannot reference {2}, defined in module {1}, since {1} loads after {0}", e.getMessage());
    }
  }
  
  @Test
  public void testMISSING_MODULE_DEP_ERROR() {
    VarCheck varCheck = new VarCheck();
    // Test for missing module dependency warning
    // edge case: missing module dependency for variable reference
    try {
      varCheck.process("externs", "root");
      // Assert warning message
    } catch (Exception e) {
      fail("Unexpected MISSING_MODULE_DEP_ERROR");
    }
  }
  
  @Test
  public void testSTRICT_MODULE_DEP_ERROR() {
    VarCheck varCheck = new VarCheck();
    // Test for strict module dependency error
    // edge case: module dependency is explicitly disallowed
    try {
      varCheck.process("externs", "root");
      fail("Expected STRICT_MODULE_DEP_ERROR");
    } catch (Exception e) {
      assertEquals("module {0} cannot reference {2}, defined in module {1}", e.getMessage());
    }
  }
  
  @Test
  public void testNAME_REFERENCE_IN_EXTERNS_ERROR() {
    VarCheck varCheck = new VarCheck();
    // Test for name reference in externs warning
    // edge case: referencing a name in externs that has no effect
    try {
      varCheck.process("externs", "root");
      // Assert warning message
    } catch (Exception e) {
      fail("Unexpected NAME_REFERENCE_IN_EXTERNS_ERROR");
    }
  }
  
  @Test
  public void testUNDEFINED_EXTERN_VAR_ERROR() {
    VarCheck varCheck = new VarCheck();
    // Test for undefined extern variable warning
    // edge case: referencing an undefined variable in externs
    try {
      varCheck.process("externs", "root");
      // Assert warning message
    } catch (Exception e) {
      fail("Unexpected UNDEFINED_EXTERN_VAR_ERROR");
    }
  }
  
  @Test
  public void testINVALID_FUNCTION_DECL() {
    VarCheck varCheck = new VarCheck();
    // Test for invalid function declaration error
    // edge case: function declaration without a name
    try {
      varCheck.process("externs", "root");
      fail("Expected INVALID_FUNCTION_DECL");
    } catch (Exception e) {
      assertEquals("Syntax error: function declaration must have a name", e.getMessage());
    }
  }
}