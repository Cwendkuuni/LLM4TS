import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {
  
  // Test findRoot method with basic functionality
  @Test
  public void testFindRootBasic() {
    BrentSolver solver = new BrentSolver();
    
    // Create dummy function and checker for testing
    ComputableFunction function = new DummyFunction();
    ConvergenceChecker checker = new DummyChecker();
    
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }
  
  // Test findRoot method with edge case when function values are very close
  @Test
  public void testFindRootEdgeCase() {
    BrentSolver solver = new BrentSolver();
    
    ComputableFunction function = new EdgeCaseFunction();
    ConvergenceChecker checker = new EdgeCaseChecker();
    
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }
  
  // Test findRoot method with error scenario where function throws exception
  @Test(expected = FunctionException.class)
  public void testFindRootException() {
    BrentSolver solver = new BrentSolver();
    
    ComputableFunction function = new ExceptionFunction();
    ConvergenceChecker checker = new DummyChecker();
    
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    
    // This should throw FunctionException
    solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }
  
  // Dummy function implementation for testing
  private class DummyFunction implements ComputableFunction {
    public double valueAt(double x) {
      return x*x - 1;
    }
  }
  
  // Dummy checker implementation for testing
  private class DummyChecker implements ConvergenceChecker {
    public int converged(double x1, double f1, double x2, double f2) {
      return Math.abs(f1) < Math.abs(f2) ? LOW : HIGH;
    }
  }
  
  // Edge case function implementation for testing
  private class EdgeCaseFunction implements ComputableFunction {
    public double valueAt(double x) {
      return Math.sin(x) / x;
    }
  }
  
  // Edge case checker implementation for testing
  private class EdgeCaseChecker implements ConvergenceChecker {
    public int converged(double x1, double f1, double x2, double f2) {
      return Math.abs(f1) < 1e-6 ? LOW : HIGH;
    }
  }
  
  // Exception throwing function implementation for testing
  private class ExceptionFunction implements ComputableFunction {
    public double valueAt(double x) {
      throw new FunctionException("Function evaluation failed");
    }
  }
}