import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.PolynomialFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testFindRoot() throws Exception {

    // Test with a simple polynomial function
    PolynomialFunction function = new PolynomialFunction(3.0, -2.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double xLower, double fLower, double xUpper, double fUpper) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(2.0, root, 0.001);

    // Test with a function that has no root in the given interval
    function = new PolynomialFunction(1.0, 1.0, 1.0);
    checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double xLower, double fLower, double xUpper, double fUpper) {
        return ConvergenceChecker.HIGH;
      }
    };
    solver = new BrentSolver();
    x0 = -1.0;
    x1 = 1.0;
    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      // Expected
    }

    // Test with a function that has multiple roots
    function = new PolynomialFunction(1.0, 4.0, 4.0, 6.0);
    checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double xLower, double fLower, double xUpper, double fUpper) {
        return (fUpper > 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.LOW;
      }
    };
    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 4.0;
    root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertTrue(Math.abs(root - 2.0) < 0.001);
    assertTrue(Math.abs(function.valueAt(root)) < 1e-9);

    // Test with a function that has a root at infinity
    function = new PolynomialFunction(1.0, 1.0, 0.0);
    checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double xLower, double fLower, double xUpper, double fUpper) {
        return ConvergenceChecker.LOW;
      }
    };
    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 100.0;
    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      // Expected
    }

  }

  /**
   * Inner interface for convergence checker.
   */
  interface ConvergenceChecker {

    /**
     * Convergence test result.
     */
    enum ConvergenceResult {
      LOW,
      HIGH,
      CONVERGED
    }

    /**
     * Convergence test method.
     * @param xLower lower bound abscissa
     * @param fLower value of the function at the lower bound
     * @param xUpper higher bound abscissa
     * @param fUpper value of the function at the higher bound
     * @return convergence test result
     */
    ConvergenceResult convergenceTest(double xLower, double fLower, double xUpper, double fUpper);

  }

}