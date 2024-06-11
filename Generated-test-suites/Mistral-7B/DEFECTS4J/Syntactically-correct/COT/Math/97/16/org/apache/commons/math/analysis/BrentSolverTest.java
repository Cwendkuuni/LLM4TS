package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.function.FunctionUtils;
import org.apache.commons.math3.function.UnivariateFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/** Test class for BrentSolver.

 */
public class BrentSolverTest {

  /** Test function f(x) = x^3 - 2. */
  private static UnivariateFunction testFunction = new SimpleComputableFunction(x -> Math.pow(x, 3) - 2);

  /** Checker for the convergence of the function. */
  private static ConvergenceChecker checker = new ConvergenceChecker() {

    @Override
    public int converged(double x1, double fx1, double x2, double fx2) {
      if (Math.abs(fx1) < 1e-15 && Math.abs(fx2) < 1e-15) {
        return ConvergenceChecker.BOTH;
      } else if (Math.abs(fx1) < 1e-15) {
        return ConvergenceChecker.LOW;
      } else if (Math.abs(fx2) < 1e-15) {
        return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.NONE;
      }
    }
  };

  /** Tests the constructor. */
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  /** Tests the findRoot method with a simple function f(x) = x^3 - 2. */
  @Test
  public void testFindRootFunction() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    boolean result = solver.findRoot(testFunction, checker, 10, x0, testFunction.valueAt(x0), x1, testFunction.valueAt(x1));
    assertTrue(result);

    double root = solver.getRoot();
    assertEquals(Precision.round(Math.sqrt(2.0) - 1.0), root, 1e-10);
  }

  /** Tests the findRoot method with a simple function f(x) = x^3 - 2
   * and different initial bounds. */
  @Test
  public void testFindRootFunctionDifferentBounds() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;

    boolean result = solver.findRoot(testFunction, checker, 10, x0, testFunction.valueAt(x0), x1, testFunction.valueAt(x1));
    assertTrue(result);

    double root = solver.getRoot();
    assertEquals(Precision.round(Math.sqrt(2.0) - 1.0), root, 1e-10);
  }

  /** Tests the findRoot method with a function f(x) = sin(x) - 1. */
  @Test(expected = ConvergenceException.class)
  public void testFindRootSineFunction() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = Math.PI;

    solver.findRoot(FunctionUtils.sin, checker, 10, x0, Math.sin(x0), x1, Math.sin(x1));

    // The test should fail because the sine function does not have a
    // finite root in the given interval.
  }

  /** Tests the findRoot method with a function f(x) = 1/x. */
  @Test(expected = ZeroException.class)
  public void testFindRootReciprocalFunction() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 0.1;

    solver.findRoot(x -> 1.0 / x, checker, 10, x0, 1.0 / x0, x1, 1.0 / x1);

    // The test should fail because the function has a singularity at x = 0.
  }

  /** Tests the findRoot method with a function f(x) = x^2. */
  @Test
  public void testFindRootSquareFunction() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    boolean result = solver.findRoot(testFunction, checker, 10, x0, testFunction.valueAt(x0), x1, testFunction.valueAt(x1));
    assertTrue(result);

    double root = solver.getRoot();
    assertEquals(0.0, root, 1e-10);
  }

  /** Tests the findRoot method with a function f(x) = x^2 with different
   * initial bounds. */
  @Test
  public void testFindRootSquareFunctionDifferentBounds() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;

    boolean result = solver.findRoot(testFunction, checker, 10, x0, testFunction.valueAt(x0), x1, testFunction.valueAt(x1));
    assertTrue(result);

    double root = solver.getRoot();
    assertEquals(0.0, root, 1e-10);
  }

  /** Tests the findRoot method with a function f(x) = x^2 with a maximal
   * number of iterations of 1. */
  @Test(expected = MaxIterationsExceededException.class)
  public void testFindRootSquareFunctionMaxIter() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    boolean result = solver.findRoot(testFunction, checker, 1, x0, testFunction.valueAt(x0), x1, testFunction.valueAt(x1));

    // The test should fail because the maximal number of iterations is too
    // low to find the root.
  }

  /** Tests the findRoot method with a function f(x) = x^2 and a checker
   * that always returns ConvergenceChecker.NONE. */
  @Test
  public void testFindRootSquareFunctionNoConvergence() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    ConvergenceChecker noConvergenceChecker = new ConvergenceChecker() {

      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.NONE;
      }
    };

    boolean result = solver.findRoot(testFunction, noConvergenceChecker, 10, x0, testFunction.valueAt(x0), x1, testFunction.valueAt(x1));
    assertFalse(result);
  }

  /** Tests the findRoot method with a function f(x) = x^2 and a checker
   * that always returns ConvergenceChecker.LOW. */
  @Test
  public void testFindRootSquareFunctionLowConvergence() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    ConvergenceChecker lowConvergenceChecker = new ConvergenceChecker() {

      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.LOW;
      }
    };

    boolean result = solver.findRoot(testFunction, lowConvergenceChecker, 10, x0, testFunction.valueAt(x0), x1, testFunction.valueAt(x1));
    assertTrue(result);

    double root = solver.getRoot();
    assertEquals(0.0, root, 1e-10);
  }

  /** Tests the findRoot method with a function f(x) = x^2 and a checker
   * that always returns ConvergenceChecker.HIGH. */
  @Test
  public void testFindRootSquareFunctionHighConvergence() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    ConvergenceChecker highConvergenceChecker = new ConvergenceChecker() {

      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.HIGH;
      }
    };

    boolean result = solver.findRoot(testFunction, highConvergenceChecker, 10, x0, testFunction.valueAt(x0), x1, testFunction.valueAt(x1));
    assertTrue(result);

    double root = solver.getRoot();
    assertEquals(0.0, root, 1e-10);
  }

  /** Tests the findRoot method with a function f(x) = x^2 and a checker
   * that throws an exception. */
  @Test(expected = MathIllegalArgumentException.class)
  public void testFindRootSquareFunctionException() throws Exception {
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    ConvergenceChecker exceptionChecker = new ConvergenceChecker() {

      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        throw new MathIllegalArgumentException("converged method should not be called");
      }
    };

    boolean result = solver.findRoot(testFunction, exceptionChecker, 10, x0, testFunction.valueAt(x0), x1, testFunction.valueAt(x1));

    // The test should fail because the checker throws an exception.
  }

}