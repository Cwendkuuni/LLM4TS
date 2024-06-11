import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testRootNotFound() {
    BrentSolver solver = new BrentSolver();

    // Create a function where a root does not exist
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 2;
      }
    };

    // Set up initial interval
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    ConvergenceChecker checker = new ConvergenceChecker();

    // Run findRoot method
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Assert that a root was not found
    assertFalse(rootFound);

    // Assert that the value of getRoot is NaN
    assertTrue(Double.isNaN(solver.getRoot()));
  }

  @Test
  public void testRootFound() {
    BrentSolver solver = new BrentSolver();

    // Create a function with a known root
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 3;
      }
    };

    // Set up initial interval
    double x0 = 2.0;
    double f0 = function.valueAt(x0);
    double x1 = 4.0;
    double f1 = function.valueAt(x1);

    ConvergenceChecker checker = new ConvergenceChecker();

    // Run findRoot method
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Assert that a root was found
    assertTrue(rootFound);

    // Assert that the value of getRoot is within the tolerance of the actual root
    assertEquals(3.0, solver.getRoot(), 1e-15);
  }

  @Test
  public void testMaxIterationsExceeded() {
    BrentSolver solver = new BrentSolver();

    // Create a function with a known root
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    // Set up initial interval
    double x0 = 2.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    ConvergenceChecker checker = new ConvergenceChecker();

    // Run findRoot method with a small number of iterations
    boolean rootFound = solver.findRoot(function, checker, 5, x0, f0, x1, f1);

    // Assert that a root was not found
    assertFalse(rootFound);

    // Assert that the value of getRoot is NaN
    assertTrue(Double.isNaN(solver.getRoot()));
  }

  @Test
  public void testConvergenceChecker() {
    BrentSolver solver = new BrentSolver();

    // Create a function with a known root
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    // Set up initial interval
    double x0 = 2.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    // Create a custom convergence checker that considers a root to be found if
    // the absolute value of the function value at a point is below a certain threshold
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        double threshold = 1e-5;

        if (Math.abs(fLow) < threshold) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < threshold) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    // Run findRoot method
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Assert that a root was found
    assertTrue(rootFound);

    // Assert that the value of getRoot is within the tolerance of the actual root
    assertEquals(2.0, solver.getRoot(), 1e-5);
  }
}