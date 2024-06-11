package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionBuilder;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.RootsFinder;

/**
 * JUnit tests for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test the findRoot method with a simple function and default parameters.
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testFindRootSimpleFunction() throws Exception {
    ComputableFunction function = FunctionBuilder.createFunction(-Math.pow(x, 2) + 1, "function");
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    boolean rootFound = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(rootFound);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  /**
   * Test the findRoot method with a function having a root at a boundary of the interval.
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testFindRootRootAtBoundary() throws Exception {
    ComputableFunction function = FunctionBuilder.createFunction(Math.pow(x, 2) - 1, "function");
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = -2.0;
    double x1 = 2.0;
    double expectedRoot = -1.0;

    boolean rootFound = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(rootFound);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  /**
   * Test the findRoot method with a function having multiple roots.
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testFindRootMultipleRoots() throws Exception {
    ComputableFunction function = FunctionBuilder.createFunction(Math.pow(x, 2) - 4, "function");
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = -2.0;
    double x1 = 2.0;
    double expectedRoot1 = -2.0;
    double expectedRoot2 = 2.0;

    boolean rootFound1 = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(rootFound1);
    assertEquals(expectedRoot1, solver.getRoot(), epsilon);

    solver.reset();

    boolean rootFound2 = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0));
    assertTrue(rootFound2);
    assertEquals(expectedRoot2, solver.getRoot(), epsilon);
  }

  /**
   * Test the findRoot method with a function having no real roots.
   * @throws Exception if an error occurs during the test
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoots() throws Exception {
    ComputableFunction function = FunctionBuilder.createFunction(Math.pow(x, 2) + 1, "function");
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  /**
   * Test the findRoot method with a function having roots at the end of the interval.
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testFindRootRootsAtEnd() throws Exception {
    ComputableFunction function = FunctionBuilder.createFunction(-Math.pow(x, 2) - 1, "function");
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = -3.0;
    double x1 = 3.0;
    double expectedRoot1 = -2.0;
    double expectedRoot2 = 2.0;

    boolean rootFound1 = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(rootFound1);
    assertEquals(expectedRoot1, solver.getRoot(), epsilon);

    solver.reset();

    boolean rootFound2 = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0));
    assertTrue(rootFound2);
    assertEquals(expectedRoot2, solver.getRoot(), epsilon);
  }

  /**
   * Test the findRoot method with function having roots at the boundaries and one inside the interval
   * @throws Exception if an error occurs during the test
   */
  @Test
  public void testFindRootRootsAtBoundariesAndInside() throws Exception {
    ComputableFunction function = FunctionBuilder.createFunction(-Math.pow(x, 2) + 1, "function");
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = -3.0;
    double x1 = 3.0;
    double expectedRoot1 = -2.0;
    double expectedRoot2 = 1.0;
    double expectedRoot3 = 2.0;

    boolean rootFound1 = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(rootFound1);
    assertEquals(expectedRoot1, solver.getRoot(), epsilon);

    solver.reset();

    boolean rootFound2 = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0));
    assertTrue(rootFound2);
    assertEquals(expectedRoot2, solver.getRoot(), epsilon);

    solver.reset();

    boolean rootFound3 = solver.findRoot(function, checker, 100, x0 + epsilon, function.valueAt(x0 + epsilon), x1 - epsilon, function.valueAt(x1 - epsilon));
    assertTrue(rootFound3);
    assertEquals(expectedRoot3, solver.getRoot(), epsilon);
  }

}