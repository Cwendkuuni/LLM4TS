package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * JUnit 4 test file for BrentSolver class
 */
public class BrentSolverTest {

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} */
  @Test
  public void testFindRootMethod() throws FunctionException {
    // Basic functionality test
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(f) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -2.0, -4.0, 2.0, -1.0);
    assertTrue(result);
    assertEquals(-1.5, solver.getRoot(), 0.001);

    // Edge cases and exception handling tests
    try {
      solver.findRoot(null, checker, 100, -2.0, -4.0, 2.0, -1.0);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Expected
    }

    try {
      solver.findRoot(function, null, 100, -2.0, -4.0, 2.0, -1.0);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Expected
    }

    try {
      solver.findRoot(function, checker, 0, -2.0, -4.0, 2.0, -1.0);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Expected
    }

    try {
      solver.findRoot(function, checker, 100, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Expected
    }

    try {
      solver.findRoot(function, checker, 100, -2.0, Double.NaN, 2.0, -1.0);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Expected
    }

    try {
      solver.findRoot(function, checker, 100, -2.0, -4.0, Double.NaN, -1.0);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Expected
    }

    try {
      solver.findRoot(function, checker, 100, -2.0, -4.0, 2.0, Double.NaN);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Expected
    }

  }

}