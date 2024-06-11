import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

  @Test
  public void testUNDEFINED_VAR_ERROR() {
    // Create a VarCheck instance
    VarCheck varCheck = new VarCheck();

    // Test for undefined variable error
    // Expected error message for undefined variable
    assertEquals("variable {0} is undefined", varCheck.UNDEFINED_VAR_ERROR.getDescription());
  }

  @Test
  public void testVIOLATED_MODULE_DEP_ERROR() {
    // Create a VarCheck instance
    VarCheck varCheck = new VarCheck();

    // Test for violated module dependency error
    // Expected error message for violated module dependency
    assertEquals("module {0} cannot reference {2}, defined in module {1}, since {1} loads after {0}", varCheck.VIOLATED_MODULE_DEP_ERROR.getDescription());
  }

  @Test
  public void testMISSING_MODULE_DEP_ERROR() {
    // Create a VarCheck instance
    VarCheck varCheck = new VarCheck();

    // Test for missing module dependency error
    // Expected warning message for missing module dependency
    assertEquals("missing module dependency; module {0} should depend on module {1} because it references {2}", varCheck.MISSING_MODULE_DEP_ERROR.getDescription());
  }

  @Test
  public void testSTRICT_MODULE_DEP_ERROR() {
    // Create a VarCheck instance
    VarCheck varCheck = new VarCheck();

    // Test for strict module dependency error
    // Expected disabled message for strict module dependency
    assertEquals("module {0} cannot reference {2}, defined in module {1}", varCheck.STRICT_MODULE_DEP_ERROR.getDescription());
  }

  @Test
  public void testNAME_REFERENCE_IN_EXTERNS_ERROR() {
    // Create a VarCheck instance
    VarCheck varCheck = new VarCheck();

    // Test for name reference in externs error
    // Expected warning message for accessing name in externs having no effect
    assertEquals("accessing name {0} in externs has no effect", varCheck.NAME_REFERENCE_IN_EXTERNS_ERROR.getDescription());
  }

  @Test
  public void testUNDEFINED_EXTERN_VAR_ERROR() {
    // Create a VarCheck instance
    VarCheck varCheck = new VarCheck();

    // Test for undefined extern variable error
    // Expected warning message for undefined extern variable
    assertEquals("name {0} is not undefined in the externs.", varCheck.UNDEFINED_EXTERN_VAR_ERROR.getDescription());
  }

  @Test
  public void testINVALID_FUNCTION_DECL() {
    // Create a VarCheck instance
    VarCheck varCheck = new VarCheck();

    // Test for invalid function declaration error
    // Expected error message for invalid function declaration syntax
    assertEquals("Syntax error: function declaration must have a name", varCheck.INVALID_FUNCTION_DECL.getDescription());
  }
}