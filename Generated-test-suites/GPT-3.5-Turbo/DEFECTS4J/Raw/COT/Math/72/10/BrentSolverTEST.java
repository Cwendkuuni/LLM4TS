import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return fLow * fHigh;
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);
    
    boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(found);
    assertEquals(5.0, solver.getRoot(), 0.00001);
  }
  
  @Test
  public void testFindRootNoRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return fLow * fHigh;
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 2;
    double f1 = function.valueAt(x1);
    
    boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertFalse(found);
  }
  
  // Add additional test cases for edge cases and exception handling scenarios
  
}