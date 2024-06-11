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
  public void testUndefinedVarError() {
    // Test undefined variable error
    varCheck.process(null, null);
    // Assertion here
    // ...
  }
  
  @Test
  public void testViolatedModuleDepError() {
    // Test violated module dependency error
    varCheck.process(null, null);
    // Assertion here
    // ...
  }
  
  @Test
  public void testMissingModuleDepError() {
    // Test missing module dependency error
    varCheck.process(null, null);
    // Assertion here
    // ...
  }
  
  @Test
  public void testStrictModuleDepError() {
    // Test strict module dependency error
    varCheck.process(null, null);
    // Assertion here
    // ...
  }
  
  @Test
  public void testNameReferenceInExternsError() {
    // Test name reference in externs error
    varCheck.process(null, null);
    // Assertion here
    // ...
  }
  
  @Test
  public void testUndefinedExternVarError() {
    // Test undefined extern variable error
    varCheck.process(null, null);
    // Assertion here
    // ...
  }
  
  @Test
  public void testInvalidFunctionDecl() {
    // Test invalid function declaration error
    varCheck.process(null, null);
    // Assertion here
    // ...
  }
  
  @Test
  public void testCreateSynthesizedExternVar() {
    // Test creating synthesized extern var
    varCheck.process(null, null);
    // Assertion here
    // ...
  }
  
  @Test
  public void testGetSynthesizedExternsInput() {
    // Test getting synthesized externs input
    varCheck.process(null, null);
    // Assertion here
    // ...
  }
  
  @Test
  public void testGetSynthesizedExternsRoot() {
    // Test getting synthesized externs root
    varCheck.process(null, null);
    // Assertion here
    // ...
  }

}