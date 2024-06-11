package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.PolynomialFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private PolynomialFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker.Simple();
    function = new PolynomialFunction(new double[]{-1.0, 4.0, -1.0, 0.0}, 0);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() throws FunctionException {
    // Basic test
    double x0 = 1.0;
    double x1 = 3.0;
    boolean expectedResult = true;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(expectedResult, Double.isFinite(root));
    assertEquals(expectedResult, solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)).isSuccess());
    assertEquals(0.0, root, 0.001);

    // Edge cases and exception handling

    // Function is not ComputableFunction
    try {
      solver.findRoot(null, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    } catch (FunctionException e) {
      assertEquals("Function is not ComputableFunction", e.getMessage());
    }

    // Function not defined in interval
    function = new PolynomialFunction(new double[]{1.0, 3.0, 0.0}, 1);
    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    } catch (FunctionException e) {
      assertEquals("Function not defined in interval", e.getMessage());
    }

    // Function value at root is zero (no root)
    function = new PolynomialFunction(new double[]{0.0, 1.0, 0.0}, 0);
    root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(false, Double.isFinite(root));
    assertEquals(false, solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)).isSuccess());

    // Function not converging
    function = new PolynomialFunction(new double[]{0.0, 1.0, -1.0}, 0);
    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    } catch (FunctionException e) {
      assertEquals("Function not converging", e.getMessage());
    }

    // Interval too small
    try {
      solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
    } catch (FunctionException e) {
      assertEquals("Interval too small", e.getMessage());
    }

    // Test with different convergence checkers
    ConvergenceChecker.Simple simpleChecker = new ConvergenceChecker.Simple();
    ConvergenceChecker.Ridders riddersChecker = new ConvergenceChecker.Ridders();
    ConvergenceChecker.Brent brentChecker = new ConvergenceChecker.Brent();

    // Root found with different checkers
    solver.findRoot(function, simpleChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    root = solver.findRoot(function, riddersChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(0.0, root, 0.001);

    solver.findRoot(function, brentChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    root = solver.findRoot(function, brentChecker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(0.0, root, 0.001);

    // Root not found with different checkers
    solver.findRoot(function, simpleChecker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
    root = solver.findRoot(function, simpleChecker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(Double.NaN, root);
    assertEquals(false, solver.findRoot(function, simpleChecker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1)).isSuccess());

    solver.findRoot(function, riddersChecker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
    root = solver.findRoot(function, riddersChecker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(Double.NaN, root);
    assertEquals(false, solver.findRoot(function, riddersChecker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1)).isSuccess());

    solver.findRoot(function, brentChecker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
    root = solver.findRoot(function, brentChecker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(Double.NaN, root);
    assertEquals(false, solver.findRoot(function, brentChecker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1)).isSuccess());

  }

  /**
   * Test method for {@link BrentSolver#getRoot()}
   */
  @Test
  public void testGetRoot() {
    // Basic test
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    double expectedRoot = solver.getRoot();
    assertEquals(0.0, expectedRoot, 0.001);

    // Edge cases and exception handling
    try {
      solver.getRoot();
    } catch (FunctionException e) {
      assertEquals("Function is not ComputableFunction", e.getMessage());
    }

    solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
    expectedRoot = solver.getRoot();
    assertEquals(Double.NaN, expectedRoot);

  }

}