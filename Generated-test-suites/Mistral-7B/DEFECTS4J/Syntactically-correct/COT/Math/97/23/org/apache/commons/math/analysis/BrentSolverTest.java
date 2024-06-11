import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws FunctionException
   */
  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = function.valueAt(x0);
    double x1 = 3.0;
    double fx1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);

    assertTrue(result);
    assertEquals(Math.sqrt(2.0), solver.getRoot(), 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws FunctionException
   */
  @Test
  public void testFindRootException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Error message");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = Double.NaN;
    double x1 = 3.0;
    double fx1 = Double.NaN;

    try {
      solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);
      fail();
    } catch (FunctionException e) {
      // expected
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws FunctionException
   */
  @Test
  public void testFindRootNoRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double fx0 = Math.sin(x0);
    double x1 = Math.PI;
    double fx1 = Math.sin(x1);

    boolean result = solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);

    assertFalse(result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws FunctionException
   */
  @Test
  public void testFindRootDivergent() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 1.0 / x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = 1.0 / x0;
    double x1 = 0.1;
    double fx1 = 1.0 / x1;

    boolean result = solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);

    assertFalse(result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws FunctionException
   */
  @Test
  public void testFindRootMarginalConvergence() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 3.0) * Math.sin(Math.PI / x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.5;
    double fx0 = Math.pow(x0 - 1.0, 3.0) * Math.sin(Math.PI / x0);
    double x1 = 1.6;
    double fx1 = Math.pow(x1 - 1.0, 3.0) * Math.sin(Math.PI / x1);

    boolean result = solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);

    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 0.01);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws FunctionException
   */
  @Test
  public void testFindRootHighAccuracy() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 2.0, 3.0) * Math.exp(-x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.95;
    double fx0 = Math.pow(x0 - 2.0, 3.0) * Math.exp(-x0);
    double x1 = 1.96;
    double fx1 = Math.pow(x1 - 2.0, 3.0) * Math.exp(-x1);

    boolean result = solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);

    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

}