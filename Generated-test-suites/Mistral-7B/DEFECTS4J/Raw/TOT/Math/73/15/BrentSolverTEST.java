package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   */
  @Test
  public void testFindRoot() throws Exception {
    // Test with a simple function
    ComputableFunction function = new RationalFunction("x^2 - 2");
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;
    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), epsilon);

    // Test with a function with multiple roots
    function = new RationalFunction("x^3 - 2");
    checker = new ConvergenceChecker.Simple();
    x0 = 0.0;
    x1 = 2.0;
    root = 1.0;
    solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), epsilon);

    root = -1.0;
    solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), epsilon);

    // Test with a function with a local minimum
    function = new RationalFunction("x^3 + x^2 - 2");
    checker = new ConvergenceChecker.Simple();
    x0 = 0.0;
    x1 = 2.0;
    root = -1.0;
    solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));

    x0 = -2.0;
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), epsilon);

    // Test with a function with a local maximum
    function = new RationalFunction("x^3 - x^2 - 2");
    checker = new ConvergenceChecker.Simple();
    x0 = 0.0;
    x1 = 2.0;
    root = 1.0;
    solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));

    x0 = 3.0;
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), epsilon);

    // Test with a function with a root close to the boundaries
    function = new RationalFunction("x^3 - 2");
    checker = new ConvergenceChecker.Simple();
    x0 = 0.5;
    x1 = 1.5;
    root = 1.0;
    solver = new BrentSolver();
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), epsilon);

  }

  /**
   * Test method for {@link BrentSolver#getRoot()}.
   */
  @Test
  public void testGetRoot() throws Exception {
    // Test with a solver with a root
    ComputableFunction function = new RationalFunction("x^2 - 2");
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, 1.0, function.valueAt(1.0), 2.0, function.valueAt(2.0));
    assertEquals(1.5, solver.getRoot(), epsilon);

    // Test with a solver without a root
    function = new RationalFunction("x^3 - x^2 - 2");
    solver = new BrentSolver();
    assertNull(solver.getRoot());
  }

  /**
   * Test method for {@link ConvergenceChecker#converged(double, double, double, double)}.
   */
  public static class ConvergenceChecker {

    public static final int LOW = 0;
    public static final int HIGH = 1;
    public static final int NOT_CONVERGED = 2;

    public enum TestResult {
      LOW,
      HIGH,
      NOT_CONVERGED
    }

    /** Simple implementation of a convergence checker. */
    public static class Simple implements ConvergenceChecker {

      /**
       * Test method for the convergence checker.
       * @param xLow abscissa of the lower bound of the interval
       * @param fLow value of the function at the lower bound
       * @param xHigh abscissa of the higher bound of the interval
       * @param fHigh value of the function at the higher bound
       * @return the convergence result
       */
      @Override
      public TestResult converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (xLow < xHigh && fLow * fHigh < 0) {
          return TestResult.LOW;
        } else if (xLow > xHigh && fLow * fHigh > 0) {
          return TestResult.HIGH;
        } else {
          return TestResult.NOT_CONVERGED;
        }
      }
    }
  }
}