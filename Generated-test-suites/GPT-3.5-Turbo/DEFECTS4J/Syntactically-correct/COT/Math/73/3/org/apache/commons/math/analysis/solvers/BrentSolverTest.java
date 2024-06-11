
import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver brentSolver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        double tolerance = 1e-6;
        if (Math.abs(fHigh) < tolerance) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < tolerance) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NO_CONVERGENCE;
        }
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -4.0;
    double x1 = 3.0;
    double f1 = 5.0;
    
    boolean rootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(2.000000107008993, brentSolver.getRoot(), 1e-6);
  }
  
  // Add more test cases for other methods if necessary
  
}

