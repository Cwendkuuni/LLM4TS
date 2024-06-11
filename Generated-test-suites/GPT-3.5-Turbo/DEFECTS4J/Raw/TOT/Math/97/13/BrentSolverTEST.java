import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Typical use case
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 100;
    double x0 = 1.0;
    double f0 = -3.0;
    double x1 = 3.0;
    double f1 = 5.0;

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithNegativeValues() {
    // Edge case: function with negative values
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x + 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 100;
    double x0 = -1.0;
    double f0 = 3.0;
    double x1 = 1.0;
    double f1 = 5.0;

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithZero() {
    // Edge case: function with zero value
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 100;
    double x0 = -1.0;
    double f0 = 1.0;
    double x1 = 1.0;
    double f1 = 1.0;

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithNoConvergence() {
    // Error scenario: function with no convergence
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 10;
    double x0 = 1.0;
    double f0 = -3.0;
    double x1 = 3.0;
    double f1 = 5.0;

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(rootFound);
  }
}