package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test cases for BrentSolver class
 */
public class BrentSolverTest {

  private static final double EPSILON = 1e-6;

  @Test
  public void testBrentSolverFindRoot() {
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        double tol = Math.abs(fx) < EPSILON ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
        if (Math.abs(fx1) < EPSILON) {
          tol = ConvergenceChecker.HIGH;
        }
        return tol;
      }
    };

    BrentSolver solver = new BrentSolver();

    double[] x = {-5.0, 0.0, 5.0};
    double[] fx = {Double.NaN, 0.0, Double.NaN};
    for (int i = 0; i < x.length; ++i) {
      ComputableFunction function = new DummyFunction(x[i]);
      boolean rootFound = solver.findRoot(function, checker, 10, x[i], fx[i], x[i + 1], fx[i + 1]);
      assertTrue(rootFound);
      assertTrue(Math.abs(solver.getRoot() - x[i]) < EPSILON);
    }

    // Test with zero function
    ComputableFunction zeroFunction = new DummyFunction(0.0);
    double[] zeroX = {};
    double[] zeroFx = {0.0};
    boolean zeroRootFound = solver.findRoot(zeroFunction, checker, 10, zeroX[0], zeroFx[0], 1.0, 0.0);
    assertTrue(zeroRootFound);
    assertTrue(Math.abs(solver.getRoot() - 0.0) < EPSILON);

    // Test with non converged case
    ComputableFunction nonConvergedFunction = new DummyFunction(Math.sin);
    double[] nonConvergedX = {0.0};
    double[] nonConvergedFx = {0.0};
    boolean nonConvergedRootFound = solver.findRoot(nonConvergedFunction, checker, 10, nonConvergedX[0], nonConvergedFx[0],
        nonConvergedX[0] + 1, nonConvergedFx[0]);
    assertFalse(nonConvergedRootFound);
    assertNull(solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testBrentSolverFindRootFunctionException() {
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();

    ComputableFunction exceptionFunction = new DummyFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function exception");
      }
    };

    boolean rootFound = solver.findRoot(exceptionFunction, checker, 10, 0.0, 0.0, 1.0, 0.0);
    assertFalse(rootFound);
    assertNull(solver.getRoot());
  }

  @Test
  public void testBrentSolverConvergenceChecker() {
    BrentSolver solver = new BrentSolver();

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x1, double fx1) {
        return ConvergenceChecker.NONE;
      }
    };

    ComputableFunction function = new DummyFunction(Math.sin);

    // Test with low convergence
    double xLow = 0.0;
    double fLow = 0.0;
    double xHigh = 0.1;
    double fHigh = 0.1;
    boolean lowConvergence = solver.findRoot(function, checker, 10, xLow, fLow, xHigh, fHigh);
    assertFalse(lowConvergence);
    assertNull(solver.getRoot());

    // Test with high convergence
    xLow = 0.0;
    fLow = 1.0;
    xHigh = 0.1;
    fHigh = 0.0;
    boolean highConvergence = solver.findRoot(function, checker, 10, xLow, fLow, xHigh, fHigh);
    assertTrue(highConvergence);
    assertTrue(Math.abs(solver.getRoot() - xLow) < EPSILON);

    // Test with tolerance
    xLow = 0.0;
    fLow = 0.0;
    xHigh = 0.1;
    fHigh = 1e-10;
    boolean toleranceConvergence = solver.findRoot(function, checker, 10, xLow, fLow, xHigh, fHigh);
    assertTrue(toleranceConvergence);
    assertTrue(Math.abs(solver.getRoot() - xLow) < EPSILON);

    // Test with maximum number of iterations
    xLow = 0.0;
    fLow = 0.0;
    xHigh = 0.1;
    fHigh = 0.0;
    boolean maxIterations = solver.findRoot(function, checker, 0, xLow, fLow, xHigh, fHigh);
    assertFalse(maxIterations);
    assertNull(solver.getRoot());
  }

}