package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test constructor
   */
  @Test
  public void testConstructor() {
    BrentSolver brentSolver = new BrentSolver();
    assertFalse(Double.isFinite(brentSolver.getRoot()));
  }

  /**
   * Test findRoot method with a simple function
   */
  @Test
  public void testFindRootSimpleFunction() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    brentSolver.findRoot(function, convergenceChecker, 10, -2.5, -1.5);

    double expectedRoot = -1.0;
    double actualRoot = brentSolver.getRoot();

    assertEquals(expectedRoot, actualRoot, 0.001);
  }

  /**
   * Test findRoot method with a complex function
   */
  @Test
  public void testFindRootComplexFunction() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI / x) / x;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    brentSolver.findRoot(function, convergenceChecker, 10, 1.0, 2.0);

    double expectedRoot = 1.341640792;
    double actualRoot = brentSolver.getRoot();

    assertEquals(expectedRoot, actualRoot, 0.001);
  }

  /**
   * Test findRoot method with a function that has no root in the given interval
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoot() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    brentSolver.findRoot(function, convergenceChecker, 10, -3.0, 3.0);
  }

  /**
   * Test findRoot method with a function that throws an exception
   */
  @Test(expected = FunctionException.class)
  public void testFindRootException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws Exception {
        if (x < 0) {
          throw new Exception("Function undefined for negative argument");
        }
        return Math.pow(x, 2) - 4;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    brentSolver.findRoot(function, convergenceChecker, 10, -1.0, 1.0);
  }

  /**
   * Test ConvergenceChecker.LOW case
   */
  private enum ConvergenceChecker {
    LOW,
    HIGH,
    NOT_CONVERGED
  }

}