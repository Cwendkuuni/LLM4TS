import static org.junit.Assert.*;
import org.junit.Test;

public class VarCheckTest {

    @Test
    public void testUNDEFINED_VAR_ERROR() {
        // Test case for UNDEFINED_VAR_ERROR
        // Edge case: Undefined variable
    }

    @Test
    public void testVIOLATED_MODULE_DEP_ERROR() {
        // Test case for VIOLATED_MODULE_DEP_ERROR
        // Edge case: Violation of module dependency
    }

    @Test
    public void testMISSING_MODULE_DEP_ERROR() {
        // Test case for MISSING_MODULE_DEP_ERROR
        // Edge case: Missing module dependency
    }

    @Test
    public void testSTRICT_MODULE_DEP_ERROR() {
        // Test case for STRICT_MODULE_DEP_ERROR
        // Normal use case: Strict module dependency
    }

    @Test
    public void testNAME_REFERENCE_IN_EXTERNS_ERROR() {
        // Test case for NAME_REFERENCE_IN_EXTERNS_ERROR
        // Edge case: Name reference in externs
    }

    @Test
    public void testUNDEFINED_EXTERN_VAR_ERROR() {
        // Test case for UNDEFINED_EXTERN_VAR_ERROR
        // Error scenario: Undefined extern variable
    }

    @Test
    public void testINVALID_FUNCTION_DECL() {
        // Test case for INVALID_FUNCTION_DECL
        // Error scenario: Invalid function declaration
    }

}