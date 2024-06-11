package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtil;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a simple quadratic function.
   */
  @Test
  public void testFindRootQuadraticFunction() {
    ComputableFunction function = FunctionUtil.quadraticFunction(-1.0, 1.0, 4.0);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = 16.0;
    double x1 = 2.0;
    double f1 = 4.0;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertEquals(1.0, solver.getRoot(), 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having only one root.
   */
  @Test
  public void testFindRootFunctionWithOneRoot() {
    ComputableFunction function = FunctionUtil.sinFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = 1.0;
    double x1 = Math.PI / 2.0;
    double f1 = -1.0;

    solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertEquals(Math.PI / 2.0, solver.getRoot(), 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having no roots.
   */
  @Test
  public void testFindRootFunctionWithNoRoots() {
    ComputableFunction function = FunctionUtil.polynomialFunction(3, 1.0, -1.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = 3.0;
    double x1 = 2.0;
    double f1 = 3.0;

    assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function with infinite roots.
   */
  @Test
  public void testFindRootFunctionWithInfiniteRoots() {
    ComputableFunction function = ComputableFunction.constantFunction(Double.POSITIVE_INFINITY);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double f0 = Double.POSITIVE_INFINITY;
    double x1 = 1.0;
    double f1 = Double.POSITIVE_INFINITY;

    assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 100, x0, f0, x1, f1));
  }
}