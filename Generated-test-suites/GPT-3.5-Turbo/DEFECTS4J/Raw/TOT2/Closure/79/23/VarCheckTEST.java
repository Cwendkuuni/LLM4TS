import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class VarCheckTest {
  
  private VarCheck varCheck;
  
  @Before
  public void setUp() {
    varCheck = new VarCheck();
  }
  
  @Test
  public void testUndefinedVarError() {
    // Test case for undefined variable error message
    // Add your test logic here
  }
  
  @Test
  public void testViolatedModuleDepError() {
    // Test case for violated module dependency error message
    // Add your test logic here
  }
  
  @Test
  public void testMissingModuleDepError() {
    // Test case for missing module dependency error message
    // Add your test logic here
  }
  
  @Test
  public void testStrictModuleDepError() {
    // Test case for strict module dependency error message
    // Add your test logic here
  }
  
  @Test
  public void testNameReferenceInExternsError() {
    // Test case for name reference in externs error message
    // Add your test logic here
  }
  
  @Test
  public void testUndefinedExternVarError() {
    // Test case for undefined extern variable error message
    // Add your test logic here
  }
  
  @Test
  public void testInvalidFunctionDecl() {
    // Test case for invalid function declaration error message
    // Add your test logic here
  }
  
  @Test
  public void testProcess() {
    // Test case for the process method of VarCheck
    // Add your test logic here
  }
  
  @Test
  public void testVisit() {
    // Test case for the visit method of VarCheck
    // Add your test logic here
  }
}