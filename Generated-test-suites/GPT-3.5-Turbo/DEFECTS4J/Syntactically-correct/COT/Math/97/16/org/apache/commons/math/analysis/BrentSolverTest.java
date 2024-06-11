import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.*;

public class BrentSolverTest {
  
  // Helper function for creating a test function
  private ComputableFunction createTestFunction() {
    return new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
  }
  
  // Helper function for creating a test convergence checker
  private ConvergenceChecker createTestConvergenceChecker() {
    return new ConvergenceChecker() {
      @Override
      public int converged(double xLo, double fLo, double xHi, double fHi) {
        if (Math.abs(fLo) < 1e-6) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fHi) < 1e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
  }
  
  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = createTestFunction();
    ConvergenceChecker checker = createTestConvergenceChecker();
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }
  
  // Edge case: Empty interval
  @Test
  public void testEmptyInterval() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = createTestFunction();
    ConvergenceChecker checker = createTestConvergenceChecker();
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertFalse(result);
  }
  
  // Edge case: Infinite interval
  @Test
  public void testInfiniteInterval() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = createTestFunction();
    ConvergenceChecker checker = createTestConvergenceChecker();
    int maxIter = 100;
    double x0 = Double.NEGATIVE_INFINITY;
    double f0 = function.valueAt(x0);
    double x1 = Double.POSITIVE_INFINITY;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertFalse(result);
  }
  
  // Exception scenario: Function throws an exception
  @Test(expected = FunctionException.class)
  public void testFunctionException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException();
      }
    };
    ConvergenceChecker checker = createTestConvergenceChecker();
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }

}