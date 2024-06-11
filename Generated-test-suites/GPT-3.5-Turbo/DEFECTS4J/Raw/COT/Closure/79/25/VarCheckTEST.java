
import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

    @Test
    public void testUndefinedVarError() {
      // Test case to check for undefined variable error
      // Arrange
      VarCheck varCheck = new VarCheck();
      String varName = "undefinedVar";

      // Act and Assert
      try {
        varCheck.process(varName);
        fail("Expected UNDEFINED_VAR_ERROR to be thrown");
      } catch (Exception e) {
        assertEquals("variable " + varName + " is undefined", e.getMessage());
      }
    }

    @Test
    public void testViolatedModuleDepError() {
      // Test case to check for violated module dependency error
      // Arrange
      VarCheck varCheck = new VarCheck();
      String currModule = "currModule";
      String varModule = "varModule";
      String varName = "violatedVar";

      // Act and Assert
      try {
        varCheck.process(currModule, varModule, varName);
        fail("Expected VIOLATED_MODULE_DEP_ERROR to be thrown");
      } catch (Exception e) {
        assertEquals("module " + currModule + " cannot reference " + varName + ", defined in module " + varModule + ", since " + varModule + " loads after " + currModule, e.getMessage());
      }
    }

    @Test
    public void testMissingModuleDepWarning() {
      // Test case to check for missing module dependency warning
      // Arrange
      VarCheck varCheck = new VarCheck();
      String currModule = "currModule";
      String varModule = "varModule";
      String varName = "missingVar";

      // Act and Assert
      try {
        varCheck.process(currModule, varModule, varName);
        fail("Expected MISSING_MODULE_DEP_ERROR to be thrown");
      } catch (Exception e) {
        assertEquals("missing module dependency; module " + currModule + " should depend on module " + varModule + " because it references " + varName, e.getMessage());
      }
    }

    @Test
    public void testStrictModuleDepError() {
      // Test case to check for strict module dependency error
      // Arrange
      VarCheck varCheck = new VarCheck();
      String currModule = "currModule";
      String varModule = "varModule";
      String varName = "strictVar";

      // Act and Assert
      try {
        varCheck.process(currModule, varModule, varName);
        fail("Expected STRICT_MODULE_DEP_ERROR to be thrown");
      } catch (Exception e) {
        assertEquals("module " + currModule + " cannot reference " + varName + ", defined in module " + varModule, e.getMessage());
      }
    }
}

