import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

  @Test
  public void testNameReferenceInExterns() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());
    // Test case where a name reference in externs has no effect
    // TODO: Implement the test case
  }

  @Test
  public void testUndefinedExternVar() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());
    // Test case where a name is not defined in the externs
    // TODO: Implement the test case
  }

  @Test
  public void testUndefinedVar() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());
    // Test case where a variable is undefined
    // TODO: Implement the test case
  }

  @Test
  public void testViolatedModuleDependency() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());
    // Test case where a variable reference violates a declared module dependency
    // TODO: Implement the test case
  }

  @Test
  public void testMissingModuleDependency() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());
    // Test case where a variable reference is between two modules that have no dependency relationship
    // TODO: Implement the test case
  }

  @Test
  public void testStrictModuleDependency() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());
    // Test case where a module cannot reference a variable defined in another module
    // TODO: Implement the test case
  }

  @Test
  public void testInvalidFunctionDecl() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());
    // Test case where a function declaration has no name
    // TODO: Implement the test case
  }

  @Test
  public void testProcess() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());
    // Test case for the process() method
    // TODO: Implement the test case
  }

  @Test
  public void testVisit() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());
    // Test case for the visit() method
    // TODO: Implement the test case
  }

}