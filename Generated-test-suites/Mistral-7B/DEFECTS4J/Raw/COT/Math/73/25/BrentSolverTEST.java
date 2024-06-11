import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws Exception when an error occurs
   */
  @Test
  public void testFindRoot() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return (f > 0.0) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();

    assertFalse(solver.findRoot(function, checker, 10, -2.0, 2.0, 0.0, 0.0));
    assertFalse(Double.isNaN(solver.getRoot()));

    solver.findRoot(function, checker, 10, -2.0, 2.0, 0.0, 0.0);
    double root = solver.getRoot();
    assertEquals(-1.0, root, epsilon);

    solver.findRoot(function, checker, 10, -2.0, 2.0, 1.0, 0.0);
    root = solver.getRoot();
    assertEquals(1.0, root, epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with
   * convergence checker that always reports that the convergence has not been achieved.
   * @throws Exception when an error occurs
   */
  @Test
  public void testFindRootWithUnconvergedChecker() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();

    assertFalse(solver.findRoot(function, checker, 10, -2.0, 2.0, 0.0, 0.0));
    assertFalse(Double.isNaN(solver.getRoot()));
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with
   * convergence checker that always reports that the convergence has been achieved.
   * @throws Exception when an error occurs
   */
  @Test
  public void testFindRootWithAlwaysConvergedChecker() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("FunctionException message");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.HIGH;
      }
    };

    BrentSolver solver = new BrentSolver();

    assertFalse(solver.findRoot(function, checker, 10, -2.0, 2.0, 0.0, 0.0));
    assertFalse(Double.isNaN(solver.getRoot()));
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with
   * convergence checker that reports the convergence if the function value is zero.
   * @throws Exception when an error occurs
   */
  @Test
  public void testFindRootWithZeroChecker() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return (f == 0.0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 10, -2.0, 2.0, 0.0, 0.0);
    double root = solver.getRoot();
    assertEquals(-1.0, root, epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with
   * convergence checker that reports the convergence if the function value is close enough to zero.
   * @throws Exception when an error occurs
   */
  @Test
  public void testFindRootWithCloseToZeroChecker() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return (Math.abs(f) < 0.01) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 10, -2.0, 2.0, 0.0, 0.0);
    double root = solver.getRoot();
    assertEquals(-1.0, root, 0.01);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with
   * convergence checker that reports the convergence if the difference between the function values is close enough to zero.
   * @throws Exception when an error occurs
   */
  @Test
  public void testFindRootWithCloseToZeroFunctionValuesDifferenceChecker() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0 + Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        double difference = Math.abs(f - fPrev);
        return (difference < 0.01) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 10, -2.0, 2.0, 0.0, 0.0);
    double root = solver.getRoot();
    // the exact root of the function is not known, however we can check if the root is close enough to the root of the polynomial part of the function
    double polynomialRoot = Math.sqrt(4.0);
    assertTrue(Math.abs(root - polynomialRoot) < 0.1);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with
   * convergence checker that reports the convergence if the function value is greater than a threshold.
   * @throws Exception when an error occurs
   */
  @Test
  public void testFindRootWithFunctionValueGreaterThanThresholdChecker() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return (f > 0.01) ? ConvergenceChecker.NONE : ConvergenceChecker.HIGH;
      }
    };

    BrentSolver solver = new BrentSolver();

    assertFalse(solver.findRoot(function, checker, 10, -2.0, 2.0, 0.0, 0.0));
    assertFalse(Double.isNaN(solver.getRoot()));
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with
   * convergence checker that reports the convergence if the difference between the function values is greater than a threshold.
   * @throws Exception when an error occurs
   */
  @Test
  public void testFindRootWithFunctionValuesDifferenceGreaterThanThresholdChecker() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0 + Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        double difference = Math.abs(f - fPrev);
        return (difference > 0.01) ? ConvergenceChecker.NONE : ConvergenceChecker.HIGH;
      }
    };

    BrentSolver solver = new BrentSolver();

    assertFalse(solver.findRoot(function, checker, 10, -2.0, 2.0, 0.0, 0.0));
    assertFalse(Double.isNaN(solver.getRoot()));
  }

}