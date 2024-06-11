package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.FunctionHandler;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a simple function.
   */
  @Test
  public void testFindRootSimpleFunction() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.sin();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = 0.0;
    double x1 = 1.0;
    double root = Math.PI / 2.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, 0.0, x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.01);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function with multiple roots.
   */
  @Test
  public void testFindRootFunctionMultipleRoots() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.xSquared();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = -1.5;
    double x1 = 1.5;
    double root1 = -1.0;
    double root2 = 1.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertTrue(Math.abs(solver.getRoot() - root1) < 0.01 || Math.abs(solver.getRoot() - root2) < 0.01);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a root at the boundary.
   */
  @Test
  public void testFindRootFunctionRootAtBoundary() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.xMinusOneOverX();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = 0.0;
    double x1 = 1.0;
    double root = 1.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.01);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a root at the boundary with opposite sign.
   */
  @Test
  public void testFindRootFunctionRootAtBoundaryWithOppositeSign() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.xMinusOneOverX().negate();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = 0.0;
    double x1 = 1.0;
    double root = -1.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.01);
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having a root outside the interval.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionRootOutsideInterval() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.xSquared();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = 0.0;
    double x1 = 1.0;
    double root = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having opposite signs at the boundary.
   */
  @Test
  public void testFindRootFunctionOppositeSignsAtBoundary() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.xMinusOneOverX();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = 0.0;
    double x1 = 1.0;
    double root = 0.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException but none thrown.");
    } catch (FunctionException e) {
      // expected
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots at the boundary and opposite signs.
   */
  @Test
  public void testFindRootFunctionRootsAtBoundaryWithOppositeSigns() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.sinX();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = 0.0;
    double x1 = Math.PI;
    double root1 = 0.0;
    double root2 = Math.PI;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException but none thrown.");
    } catch (FunctionException e) {
      // expected
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots at the boundary with the same sign.
   */
  @Test
  public void testFindRootFunctionRootsAtBoundaryWithSameSign() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.sinX();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = 0.0;
    double x1 = -Math.PI;
    double root1 = 0.0;
    double root2 = -Math.PI;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException but none thrown.");
    } catch (FunctionException e) {
      // expected
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots at the boundary and same sign but with different interval.
   */
  @Test
  public void testFindRootFunctionRootsAtBoundaryWithSameSignButDifferentInterval() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.sinX();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = -Math.PI;
    double x1 = 0.0;
    double root1 = 0.0;
    double root2 = -Math.PI;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException but none thrown.");
    } catch (FunctionException e) {
      // expected
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function having roots at the boundary and same sign but with different interval 2.
   */
  @Test
  public void testFindRootFunctionRootsAtBoundaryWithSameSignButDifferentInterval2() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = FunctionFactory.sinX();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    double x0 = Math.PI;
    double x1 = 0.0;
    double root1 = 0.0;
    double root2 = -Math.PI;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException but none thrown.");
    } catch (FunctionException e) {
      // expected
    }
  }

}