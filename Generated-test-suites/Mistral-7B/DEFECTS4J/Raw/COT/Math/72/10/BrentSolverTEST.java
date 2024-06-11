package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstant;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * JUnit test file for the BrentSolver class
 */
public class BrentSolverTest {

  /**
   * Test constructor
   */
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  /**
   * Test findRoot method with a simple function that has a root
   */
  @Test
  public void testFindRootSimpleFunction() throws Exception {
    ComputableFunction function = new FunctionConstant(Math::pow, 2);
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 4.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(Math.pow(2, 2.0), solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a simple function that has no root
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoot() throws Exception {
    ComputableFunction function = new FunctionConstant(Math::sin);
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 2.0 * Math.PI;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  /**
   * Test findRoot method with a function that has multiple roots
   */
  @Test
  public void testFindRootMultipleRoots() throws Exception {
    ComputableFunction function = new FunctionConstant(Math::sin);
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = Math.PI;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), epsilon);

    solver.reset();

    x0 = 1.0;
    x1 = Math.PI;
    result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(-1.0, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a function that has a root very close to the bounds
   */
  @Test
  public void testFindRootRootCloseToBounds() throws Exception {
    ComputableFunction function = new FunctionConstant(Math::pow, 2);
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 0.01;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a function that has a root very close to the bounds (reverse)
   */
  @Test
  public void testFindRootRootCloseToBoundsReverse() throws Exception {
    ComputableFunction function = new FunctionConstant(Math::pow, 2);
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.01;
    double x1 = 0.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a function that has a root between the bounds
   */
  @Test
  public void testFindRootRootBetweenBounds() throws Exception {
    ComputableFunction function = new FunctionConstant(Math::pow, 3);
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), epsilon);
  }

  /**
   * Test findRoot method with a function that has a root far from the bounds
   */
  @Test
  public void testFindRootRootFarFromBounds() throws Exception {
    ComputableFunction function = new FunctionConstant(Math::pow, 3);
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 10.0;
    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(3.0, solver.getRoot(), epsilon);
  }

}