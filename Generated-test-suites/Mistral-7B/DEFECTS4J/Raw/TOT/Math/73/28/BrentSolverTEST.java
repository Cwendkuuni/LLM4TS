package org.spaceroots.mantissa.roots;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionTests;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;
import static org.junit.Assert.*;

/**
 * Test class for BrentSolver.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xi, double fi) {
        return ConvergenceChecker.NONE;
      }
    };
    function = FunctionTests.createTestFunction(TestFunctions.SIN);
  }

  @After
  public void tearDown() {
    solver = null;
  }

  /**
   * Test finding a root of a monotonic function.
   * @throws Exception if finding a root fails
   */
  @Test
  public void testFindRootMonotonicFunction() throws Exception {
    double x0 = -3.0;
    double x1 = 3.0;
    double root = Math.PI / 2;

    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  /**
   * Test finding a root of a function with multiple roots.
   * @throws Exception if finding a root fails
   */
  @Test
  public void testFindRootMultipleRoots() throws Exception {
    double x0 = 1.0;
    double x1 = 3.0;
    double root1 = 1.5;
    double root2 = 2.5;

    double[] expectedRoots = {root1, root2};

    for (double root : expectedRoots) {
      assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
      assertEquals(root, solver.getRoot(), 0.001);
      solver.reset();
    }

    x0 = 2.0;
    x1 = 4.0;

    for (double root : expectedRoots) {
      assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
      assertEquals(root, solver.getRoot(), 0.001);
      solver.reset();
    }
  }

  /**
   * Test finding a root of a function with no root in the interval.
   * @throws Exception if finding a root fails
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoot() throws Exception {
    double x0 = -3.0;
    double x1 = 3.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  /**
   * Test finding a root of a function with an infinite root.
   * @throws Exception if finding a root fails
   */
  @Test(expected = FunctionException.class)
  public void testFindRootInfiniteRoot() throws Exception {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = Double.POSITIVE_INFINITY;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  /**
   * Test finding a root of a function with a negative root.
   * @throws Exception if finding a root fails
   */
  @Test
  public void testFindRootNegativeRoot() throws Exception {
    double x0 = 0.0;
    double x1 = 2.0;
    double root = -1.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  /**
   * Test finding a root of a function with a root at a boundary.
   * @throws Exception if finding a root fails
   */
  @Test
  public void testFindRootRootAtBoundary() throws Exception {
    double x0 = 0.0;
    double x1 = 0.0;
    double root = 0.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }
}