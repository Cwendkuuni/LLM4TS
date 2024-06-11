import static org.junit.Assert.*;
import org.junit.Test;

public class VarCheckTest {
  
  @Test
  public void testUndefinedVar() {
    VarCheck varCheck = new VarCheck();
    varCheck.process(null, null);
  }
  
  @Test
  public void testViolatedModuleDependency() {
    VarCheck varCheck = new VarCheck();
    varCheck.process(null, null);
  }
  
  @Test
  public void testMissingModuleDependency() {
    VarCheck varCheck = new VarCheck();
    varCheck.process(null, null);
  }
  
  @Test
  public void testStrictModuleDependency() {
    VarCheck varCheck = new VarCheck();
    varCheck.process(null, null);
  }
  
  @Test
  public void testNameReferenceInExterns() {
    VarCheck varCheck = new VarCheck();
    varCheck.process(null, null);
  }
  
  @Test
  public void testUndefinedExternVar() {
    VarCheck varCheck = new VarCheck();
    varCheck.process(null, null);
  }
  
  @Test
  public void testInvalidFunctionDecl() {
    VarCheck varCheck = new VarCheck();
    varCheck.process(null, null);
  }
  
}