package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() {
    // Given
    ComputableFunction function = new RationalFunction("x^3 - 2*x^2 + x - 1");
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Then
    Assert.assertTrue(result);
    Assert.assertFalse(Double.isNaN(solver.getRoot()));
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with initial bounds bracketing a root
   */
  @Test
  public void testFindRootWithRootInInterval() {
    // Given
    ComputableFunction function = new RationalFunction("x^3 - 2*x^2 + x - 1");
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Then
    Assert.assertTrue(result);
    double root = solver.getRoot();
    Assert.assertEquals(1.0, root, 1e-6);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with initial bounds not bracketing a root
   */
  @Test
  public void testFindRootWithBoundsNotBracketingRoot() {
    // Given
    ComputableFunction function = new RationalFunction("x^3 - 2*x^2 + x - 1");
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.5;
    double f1 = function.valueAt(x1);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Then
    Assert.assertFalse(result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function throwing an exception
   */
  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionException() {
    // Given
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x > 1.0) {
          throw new FunctionException();
        }
        return Math.log(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Then
    Assert.assertFalse(result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with convergence checker not allowing convergence
   */
  @Test
  public void testFindRootWithCheckerNotConverged() {
    // Given
    ComputableFunction function = new RationalFunction("x^3 - 2*x^2 + x - 1");
    ConvergenceChecker checker = new ConvergenceChecker.NotConverged();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Then
    Assert.assertFalse(result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function returning infinite value
   */
  @Test(expected = ArithmeticException.class)
  public void testFindRootWithInfiniteValue() {
    // Given
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 1.0 / x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.1;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Then
    Assert.assertFalse(result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function returning NaN value
   */
  @Test
  public void testFindRootWithNaNValue() {
    // Given
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sqrt(-1.0);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    // When
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    // Then
    Assert.assertFalse(result);
  }
}