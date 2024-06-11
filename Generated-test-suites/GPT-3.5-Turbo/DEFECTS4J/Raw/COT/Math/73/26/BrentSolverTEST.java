import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  // Test findRoot method with basic functionality
  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) return LOW;
        if (fHigh == 0) return HIGH;
        return NONE;
      }
    };
    int maxIter = 100;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 1e-15);
  }

  // Test findRoot method with different initial interval values
  @Test
  public void testFindRootWithDifferentInterval() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) return LOW;
        if (fHigh == 0) return HIGH;
        return NONE;
      }
    };
    int maxIter = 100;
    double x0 = -5;
    double f0 = function.valueAt(x0);
    double x1 = 5;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 1e-15);
  }

  // Test findRoot method with invalid input (null function)
  @Test(expected = FunctionException.class)
  public void testFindRootWithNullFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = null;
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) return LOW;
        if (fHigh == 0) return HIGH;
        return NONE;
      }
    };
    int maxIter = 100;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }

  // Test findRoot method with invalid input (null checker)
  @Test(expected = NullPointerException.class)
  public void testFindRootWithNullChecker() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = null;
    int maxIter = 100;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }

  // Test findRoot method with convergence at lower bound
  @Test
  public void testFindRootWithConvergenceAtLowerBound() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) return LOW;
        if (fHigh == 0) return HIGH;
        if (xLow < -100) return LOW;
        return NONE;
      }
    };
    int maxIter = 100;
    double x0 = -1000;
    double f0 = function.valueAt(x0);
    double x1 = 0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(-2.0, solver.getRoot(), 1e-15);
  }

  // Test findRoot method with convergence at upper bound
  @Test
  public void testFindRootWithConvergenceAtUpperBound() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) return LOW;
        if (fHigh == 0) return HIGH;
        if (xHigh > 100) return HIGH;
        return NONE;
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 1000;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 1e-15);
  }

  // Test findRoot method with non-convergence after maximum iterations
  @Test
  public void testFindRootWithNonConvergence() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return NONE;
      }
    };
    int maxIter = 5;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
  }

  // Test getRoot method
  @Test
  public void testGetRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) return LOW;
        if (fHigh == 0) return HIGH;
        return NONE;
      }
    };
    int maxIter = 100;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertEquals(2.0, solver.getRoot(), 1e-15);
  }

}