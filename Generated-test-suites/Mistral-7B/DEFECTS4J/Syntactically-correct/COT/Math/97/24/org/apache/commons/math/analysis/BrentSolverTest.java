package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.ConstantFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   */
  @Test
  public void testFindRoot() throws FunctionException {
    // Test with a simple function
    ComputableFunction function = new ConstantFunction(2.0);
    ConvergenceChecker checker = new DefaultConvergenceChecker();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;

    solver.findRoot(function, checker, 100, x0, 2.0 * x0, x1, 2.0 * x1);

    assertEquals(expectedRoot, solver.getRoot(), epsilon);

    // Test with a function that has two roots
    function = new ModifiedQuadraticFunction();
    checker = new DefaultConvergenceChecker();
    solver = new BrentSolver();
    x0 = 1.0;
    x1 = 3.0;
    double expectedRoot1 = -1.0;
    double expectedRoot2 = 1.5;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(expectedRoot1, solver.getRoot(), epsilon);

    solver = new BrentSolver();
    solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0));

    assertEquals(expectedRoot2, solver.getRoot(), epsilon);

    // Test with a function that has no root in the given interval
    function = new NonRootFunction();
    checker = new DefaultConvergenceChecker();
    solver = new BrentSolver();
    x0 = 0.0;
    x1 = 4.0;

    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  /**
   * Test method for {@link BrentSolver#getRoot()}.
   */
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();

    // Test with a root found
    ComputableFunction function = new ConstantFunction(2.0);
    ConvergenceChecker checker = new DefaultConvergenceChecker();
    solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 2.0);

    assertEquals(2.0, solver.getRoot(), epsilon);

    // Test with no root found
    function = new NonRootFunction();
    solver = new BrentSolver();
    solver.findRoot(function, checker, 100, 0.0, 0.0, 4.0, 0.0);

    assertFalse(Double.isFinite(solver.getRoot()));
  }

  /**
   * Test method for {@link ConvergenceChecker}.
   */
  private static class DefaultConvergenceChecker implements ConvergenceChecker {

    /**
     * @see ConvergenceChecker#converged(double, double, double, double)
     */
    @Override
    public ConvergenceStatus converged(double x, double fx, double xPrev, double fxPrev) {
      if (Math.abs(x - xPrev) < epsilon) {
        return ConvergenceStatus.LOW;
      } else if (Math.abs(fx) < epsilon) {
        return ConvergenceStatus.HIGH;
      } else {
        return ConvergenceStatus.NEITHER;
      }
    }

  }

  /**
   * Function with a single root.
   */
  private static class ModifiedQuadraticFunction implements ComputableFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public double valueAt(double x) {
      return Math.pow(x - 1.0, 2.0) - 1.0;
    }

  }

  /**
   * Function with no roots in a given interval.
   */
  private static class NonRootFunction implements ComputableFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public double valueAt(double x) {
      return Math.sin(x) / x;
    }

  }

}