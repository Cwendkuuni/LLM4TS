import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_Success() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        // Define your function here
        return x*x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Define your convergence checker here
        return (Math.abs(xHigh - xLow) < 0.0001) ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 3.0;
    double f1 = 5.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_Failure() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        // Define your function here
        return x*x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Define your convergence checker here
        return (Math.abs(xHigh - xLow) < 0.0001) ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 5;
    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 3.0;
    double f1 = 5.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), 0.0001);
  }

}