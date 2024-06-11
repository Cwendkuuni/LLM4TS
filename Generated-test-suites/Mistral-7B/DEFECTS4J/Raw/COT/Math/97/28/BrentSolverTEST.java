package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * JUnit 4 test file for the BrentSolver class.
 *
 * @author your_name
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a simple function.
   */
  @Test
  public void testFindRootSimpleFunction() {
    ComputableFunction function = new DummyFunction(-1.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 0.0, 1.0, 0.5, 0.25);
    assertTrue(result);
    double expectedRoot = 0.366496583059332;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a root between the bounds.
   */
  @Test
  public void testFindRootRootBetweenBounds() {
    ComputableFunction function = new DummyFunction(-1.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 1.0, 2.0, 1.5, 1.75);
    assertTrue(result);
    double expectedRoot = 1.41421356237309;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots outside the bounds.
   */
  @Test
  public void testFindRootRootsOutsideBounds() {
    ComputableFunction function = new DummyFunction(-5.0, 5.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 0.0, 1.0, 2.0, 3.0);
    assertFalse(result);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots close to the bounds.
   */
  @Test
  public void testFindRootRootsCloseToBounds() {
    ComputableFunction function = new DummyFunction(-1.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, -0.1, 0.1, -0.05, 0.05);
    assertTrue(result);
    double expectedRoot = 0.0;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots with different signs.
   */
  @Test
  public void testFindRootRootsWithDifferentSigns() {
    ComputableFunction function = new DummyFunction(-3.0, 3.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, -2.0, 2.0, -1.0, 1.0);
    assertTrue(result);
    double expectedRoot1 = -1.0;
    double actualRoot1 = solver.getRoot();
    assertEquals(expectedRoot1, actualRoot1, 0.001);

    result = solver.findRoot(function, checker, 10, -2.0, 2.0, 1.0, 3.0);
    assertTrue(result);
    double expectedRoot2 = 1.0;
    double actualRoot2 = solver.getRoot();
    assertEquals(expectedRoot2, actualRoot2, 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots with equal values.
   */
  @Test
  public void testFindRootRootsWithEqualValues() {
    ComputableFunction function = new DummyFunction(-1.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 0.5, 0.5, 0.49, 0.51);
    assertTrue(result);
    double expectedRoot = 0.5;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a root at a boundary.
   */
  @Test
  public void testFindRootRootAtBoundary() {
    ComputableFunction function = new DummyFunction(-1.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 0.0, 0.0, 0.001, 0.002);
    assertTrue(result);
    double expectedRoot = 0.0;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.001);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots that are not bracketed.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootRootsNotBracketed() {
    ComputableFunction function = new DummyFunction(-1.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, 1.0, 2.0, 0.5, 1.5);
    fail("FunctionException expected");
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots that are very close.
   */
  @Test
  public void testFindRootRootsVeryClose() {
    ComputableFunction function = new DummyFunction(-1.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 10, -0.1, 0.1, -0.01, 0.01);
    assertTrue(result);
    double expectedRoot = 0.0;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.001);
  }
}