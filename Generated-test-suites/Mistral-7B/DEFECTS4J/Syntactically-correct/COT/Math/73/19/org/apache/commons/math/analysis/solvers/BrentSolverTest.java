package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  /**
   * Test constructor
   */
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertFalse(solver.isRootFound());
  }

  /**
   * Test findRoot method with a simple function and convergence checker
   * @throws Exception
   */
  @Test
  public void testFindRootSimpleFunction() throws Exception {
    ComputableFunction function = TestFunctions.FUNCTION_SINX;
    ConvergenceChecker checker = new ConvergenceChecker.Abs();

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test findRoot method with a simple function and convergence checker
   * that converges at a high point
   * @throws Exception
   */
  @Test
  public void testFindRootHighPoint() throws Exception {
    ComputableFunction function = TestFunctions.FUNCTION_SINX;
    ConvergenceChecker checker = new ConvergenceChecker.AbsHigh();

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test findRoot method with a simple function and convergence checker
   * that converges at a low point
   * @throws Exception
   */
  @Test
  public void testFindRootLowPoint() throws Exception {
    ComputableFunction function = TestFunctions.FUNCTION_SINX;
    ConvergenceChecker checker = new ConvergenceChecker.AbsLow();

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test findRoot method with a simple function and convergence checker
   * that converges with a tolerance
   * @throws Exception
   */
  @Test
  public void testFindRootTolerance() throws Exception {
    ComputableFunction function = TestFunctions.FUNCTION_SINX;
    ConvergenceChecker checker = new ConvergenceChecker.AbsTolerance(0.01);

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 0.01, 0.0);
    assertTrue(result);
  }

  /**
   * Test findRoot method with a function that has multiple roots
   * @throws Exception
   */
  @Test
  public void testFindRootMultipleRoots() throws Exception {
    ComputableFunction function = TestFunctions.FUNCTION_SINX;
    ConvergenceChecker checker = new ConvergenceChecker.Abs();

    BrentSolver solver = new BrentSolver();
    double x0 = 0.5;
    double x1 = 3.5;
    double root = 1.5707963267948966;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test findRoot method with a function that has no root in the interval
   * @throws Exception
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoot() throws Exception {
    ComputableFunction function = TestFunctions.FUNCTION_SINX;
    ConvergenceChecker checker = new ConvergenceChecker.Abs();

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 4.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  /**
   * Test findRoot method with a function that throws an exception
   * @throws Exception
   */
  @Test(expected = FunctionException.class)
  public void testFindRootException() throws Exception {
    ComputableFunction function = () -> { throw new FunctionException("Test function exception"); };
    ConvergenceChecker checker = new ConvergenceChecker.Abs();

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 4.0;

    solver.findRoot(function, checker, 100, x0, FunctionUtils.UNDEFINED_VALUE, x1, FunctionUtils.UNDEFINED_VALUE);
  }

  /**
   * Test getRoot method with a solver that has found a root
   */
  @Test
  public void testGetRootFound() {
    ComputableFunction function = TestFunctions.FUNCTION_SINX;
    ConvergenceChecker checker = new ConvergenceChecker.Abs();

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  /**
   * Test getRoot method with a solver that has not found a root
   */
  @Test
  public void testGetRootNotFound() {
    ComputableFunction function = TestFunctions.FUNCTION_SINX;
    ConvergenceChecker checker = new ConvergenceChecker.Abs();

    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 4.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertNaN(solver.getRoot());
  }

}