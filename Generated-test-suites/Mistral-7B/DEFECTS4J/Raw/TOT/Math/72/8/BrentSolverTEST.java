package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  /** Test method for the findRoot method of the BrentSolver class. */
  @Test
  public void testFindRoot() throws Exception {

    // Test a simple case
    RationalFunction function = new RationalFunction("1.0", "x", "1.0", "x", "1.0");
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (Math.abs(fx) < 0.001) ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };
    double root = 1.0;
    Assert.assertTrue(solver.findRoot(function, checker, 100, 0.5, function.valueAt(0.5), 1.5, function.valueAt(1.5)));
    Assert.assertEquals(root, solver.getRoot(), epsilon);

    // Test a case with multiple roots
    function = new RationalFunction("1.0", "x", "1.0", "x", "x");
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (Math.abs(fx) < 0.001) ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };
    root = 0.0;
    Assert.assertTrue(solver.findRoot(function, checker, 100, -2.0, function.valueAt(-2.0), 2.0, function.valueAt(2.0)));
    Assert.assertEquals(root, solver.getRoot(), epsilon);

    // Test a case where the interval doesn't contain a root
    function = new RationalFunction("1.0", "x", "1.0", "x", "x+1");
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    root = 1.0;
    Assert.assertFalse(solver.findRoot(function, checker, 100, 0.0, function.valueAt(0.0), 2.0, function.valueAt(2.0)));

    // Test a case with NaN input
    function = new RationalFunction("1.0", "x", "1.0", null, null);
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    Assert.assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 100, 0.0, Double.NaN, 2.0, Double.NaN));

  }

}