package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.function.SinFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.ConvergenceChecker.ConvergenceStatus;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() throws Exception {
    ComputableFunction function = new SinFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceStatus.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    boolean result = brentSolver.findRoot(function, checker, 100, -1.0, 1.0, 0.0, 0.0);
    assertTrue(result);
    assertEquals(0.0, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function having a root outside the given interval
   */
  @Test(expected = FunctionException.class)
  public void testFindRootOutsideInterval() throws Exception {
    ComputableFunction function = new SinFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceStatus.HIGH;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    brentSolver.findRoot(function, checker, 100, -1.0, 0.5, 0.0, 0.0);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function having a root at the boundary of the given interval
   */
  @Test
  public void testFindRootAtBoundary() throws Exception {
    ComputableFunction function = new SinFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceStatus.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    boolean result = brentSolver.findRoot(function, checker, 100, -1.0, 1.0, 0.0, 0.0);
    assertTrue(result);
    assertEquals(0.0, brentSolver.getRoot(), epsilon);

    brentSolver.reset(); // reset for next test

    result = brentSolver.findRoot(function, checker, 100, -1.0, 1.0, 1.0, 0.0);
    assertTrue(result);
    assertEquals(0.0, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function having multiple roots
   */
  @Test
  public void testFindRootMultipleRoots() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) * Math.sin(x) - 1.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceStatus.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    boolean result = brentSolver.findRoot(function, checker, 100, -3.0, 3.0, 0.0, 0.0);
    assertTrue(result);
    assertEquals(-1.5708, brentSolver.getRoot(), epsilon);

    brentSolver.reset(); // reset for next test

    result = brentSolver.findRoot(function, checker, 100, -3.0, 3.0, 1.57, 0.0);
    assertTrue(result);
    assertEquals(1.5708, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function having a root very close to the interval boundary
   */
  @Test
  public void testFindRootRootCloseToIntervalBoundary() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) * Math.sin(x) - 1.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceStatus.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    boolean result = brentSolver.findRoot(function, checker, 100, 1.0, 2.0, 1.1, 0.0);
    assertTrue(result);
    assertEquals(1.1071, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function having a root far from the given interval
   */
  @Test(expected = FunctionException.class)
  public void testFindRootRootFarFromInterval() throws Exception {
    ComputableFunction function = new SinFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceStatus.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    brentSolver.findRoot(function, checker, 100, -1.0, 1.0, 5.0, 0.0);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function having roots at the interval endpoints
   */
  @Test
  public void testFindRootRootsAtIntervalEndpoints() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 4) - 1.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceStatus.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    boolean result = brentSolver.findRoot(function, checker, 100, 1.0, 1.0, 1.0, 0.0);
    assertTrue(result);
    assertEquals(1.0, brentSolver.getRoot(), epsilon);

    brentSolver.reset(); // reset for next test

    result = brentSolver.findRoot(function, checker, 100, -1.0, -1.0, -1.0, 0.0);
    assertTrue(result);
    assertEquals(-1.0, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with function having roots at the interval middle
   */
  @Test
  public void testFindRootRootsAtIntervalMiddle() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 4) - 1.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceStatus.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    boolean result = brentSolver.findRoot(function, checker, 100, -0.5, 0.5, 0.0, 0.0);
    assertTrue(result);
    assertEquals(0.0, brentSolver.getRoot(), epsilon);
  }
}