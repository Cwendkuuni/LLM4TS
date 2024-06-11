import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {
  
  private VarCheck varCheck;

  @Before
  public void setUp() {
    varCheck = new VarCheck();
  }
  
  @Test
  public void testProcess() {
    // Test process() method
    // Implement test cases here
  }
  
  @Test
  public void testVisit() {
    // Test visit() method
    // Implement test cases here
  }
  
  @Test
  public void testCreateSynthesizedExternVar() {
    // Test createSynthesizedExternVar() method
    // Implement test cases here
  }
  
  @Test
  public void testGetSynthesizedExternsInput() {
    // Test getSynthesizedExternsInput() method
    // Implement test cases here
  }
  
  @Test
  public void testGetSynthesizedExternsRoot() {
    // Test getSynthesizedExternsRoot() method
    // Implement test cases here
  }
  
  @Test
  public void testUndefinedVarError() {
    // Test UNDEFINED_VAR_ERROR constant
    // Implement test cases here
  }

  @Test
  public void testViolatedModuleDepError() {
    // Test VIOLATED_MODULE_DEP_ERROR constant
    // Implement test cases here
  }
  
  @Test
  public void testMissingModuleDepError() {
    // Test MISSING_MODULE_DEP_ERROR constant
    // Implement test cases here
  }
  
  @Test
  public void testStrictModuleDepError() {
    // Test STRICT_MODULE_DEP_ERROR constant
    // Implement test cases here
  }
  
  @Test
  public void testNameReferenceInExternsError() {
    // Test NAME_REFERENCE_IN_EXTERNS_ERROR constant
    // Implement test cases here
  }
  
  @Test
  public void testUndefinedExternVarError() {
    // Test UNDEFINED_EXTERN_VAR_ERROR constant
    // Implement test cases here
  }
  
  @Test
  public void testInvalidFunctionDecl() {
    // Test INVALID_FUNCTION_DECL constant
    // Implement test cases here
  }
}