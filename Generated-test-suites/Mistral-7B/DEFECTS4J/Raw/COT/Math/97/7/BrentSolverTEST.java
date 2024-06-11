package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test file for {@link BrentSolver} class.
 */
public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleCase() throws Exception {
    // given
    ComputableFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    int maxIter = 10;
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;

    // when
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0,
        function.valueAt(x0), x1, function.valueAt(x1));

    // then
    assertTrue(result);
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_ConvergenceChecker_Low() throws Exception {
    // given
    ComputableFunction function = new FunctionConstants.SQUARE();
    ConvergenceChecker checker = new ConvergenceChecker.Default() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    };
    int maxIter = 10;
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 2.0;

    // when
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0,
        function.valueAt(x0), x1, function.valueAt(x1));

    // then
    assertTrue(result);
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_ConvergenceChecker_High() throws Exception {
    // given
    ComputableFunction function = new FunctionConstants.SQUARE();
    ConvergenceChecker checker = new ConvergenceChecker.Default() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.HIGH;
      }
    };
    int maxIter = 10;
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 3.0;

    // when
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0,
        function.valueAt(x0), x1, function.valueAt(x1));

    // then
    assertTrue(result);
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_MaxIterationsExceeded() throws Exception {
    // given
    ComputableFunction function = new FunctionConstants.SQUARE();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    int maxIter = 0;
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 2.5;

    // when
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0,
        function.valueAt(x0), x1, function.valueAt(x1));

    // then
    assertFalse(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() throws Exception {
    // given
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("FunctionException test");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    int maxIter = 10;
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 3.0;

    // when
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, maxIter, x0, 0.0, x1, 0.0);

    // then (exception is thrown)
  }
}