package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.RootFinderTestUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.RootsFinder;

/**
 * Test class for {@link BrentSolver}
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   */
  @Test
  public void testFindRoot() throws Exception {

    // test basic functionality
    testFindRootFunction(BrentSolver.class, "x^3 - 2*x^2 + x - 1", 1.0, 2.0, 1.5);

    // test edge cases and exceptions
    testFindRootFunctionAtRoot(BrentSolver.class, "x^3 - 2*x^2 + x - 1", 1.0, 1.0, 1.0);
    testFindRootFunctionAtRoot(BrentSolver.class, "x^3 - 2*x^2 + x - 1", -1.0, -1.0, -1.0);

    testFindRootFunctionDifferentRoots(BrentSolver.class, "x^3 - 2*x^2 + x - 1", 1.0, 1.5, 2.5);
    testFindRootFunctionDifferentRoots(BrentSolver.class, "x^3 - 2*x^2 + x - 1", -1.0, -1.5, -2.5);

    testFindRootFunctionNonConverging(BrentSolver.class, "x^3 - 2*x^2 + x - 1", 0.0, 0.5, 1.0, 2.0);
    testFindRootFunctionNonConverging(BrentSolver.class, "x^3 - 2*x^2 + x - 1", 2.0, 1.5, 0.5, 0.0);

    testFindRootFunctionNaN(BrentSolver.class, FunctionUtils.constantFunction(Double.NaN), 0.0, 0.0, 0.0);
    testFindRootFunctionNaN(BrentSolver.class, FunctionUtils.constantFunction(-Double.NaN), 0.0, 0.0, 0.0);

    testFindRootFunctionException(BrentSolver.class, FunctionUtils.divisionByZeroFunction(), 0.0, 0.0, 0.0);
    testFindRootFunctionException(BrentSolver.class, FunctionUtils.divisionByZeroFunction(), 0.0, 0.0, 0.0);

  }

  /**
   * Helper method to test {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a known function and interval
   *
   * @param clazz the class of the solver
   * @param function the computable function
   * @param x0 the abscissa of the lower bound of the interval
   * @param x1 the abscissa of the higher bound of the interval
   * @param expectedRoot the expected root
   */
  private void testFindRootFunction(Class<?> clazz, String functionExpression, double x0, double x1, double expectedRoot) {
    ComputableFunction function = RootFinderTestUtils.compileFunctionFromString(functionExpression);
    ConvergenceChecker convergenceChecker = RootFinderTestUtils.simpleConvergenceChecker();
    RootsFinder solver = clazz.newInstance();

    double root = solver.findRoot(function, convergenceChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(expectedRoot, root, epsilon);
  }

  /**
   * Helper method to test {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a known function and root
   *
   * @param clazz the class of the solver
   * @param function the computable function
   * @param x0 the abscissa of the lower bound of the interval
   * @param xRoot the abscissa of the root
   */
  private void testFindRootFunctionAtRoot(Class<?> clazz, String functionExpression, double x0, double xRoot) {
    ComputableFunction function = RootFinderTestUtils.compileFunctionFromString(functionExpression);
    ConvergenceChecker convergenceChecker = RootFinderTestUtils.simpleConvergenceChecker();
    RootsFinder solver = clazz.newInstance();

    assertFalse(solver.findRoot(function, convergenceChecker, 100, x0, function.valueAt(x0), xRoot, function.valueAt(xRoot)).isSuccess());
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

  /**
   * Helper method to test {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a known function and different roots
   *
   * @param clazz the class of the solver
   * @param function the computable function
   * @param x0 the abscissa of the lower bound of the interval
   * @param xRoot1 the abscissa of the first root
   * @param xRoot2 the abscissa of the second root
   */
  private void testFindRootFunctionDifferentRoots(Class<?> clazz, String functionExpression, double x0, double xRoot1, double xRoot2) {
    ComputableFunction function = RootFinderTestUtils.compileFunctionFromString(functionExpression);
    ConvergenceChecker convergenceChecker = RootFinderTestUtils.simpleConvergenceChecker();
    RootsFinder solver = clazz.newInstance();

    // test root1
    double root1 = solver.findRoot(function, convergenceChecker, 100, x0, function.valueAt(x0), xRoot1, function.valueAt(xRoot1)).getRoot();
    assertEquals(xRoot1, root1, epsilon);

    // test root2
    solver.reset(); // reset solver state
    double root2 = solver.findRoot(function, convergenceChecker, 100, x0, function.valueAt(x0), xRoot2, function.valueAt(xRoot2)).getRoot();
    assertEquals(xRoot2, root2, epsilon);
  }

  /**
   * Helper method to test {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a non-converging function
   *
   * @param clazz the class of the solver
   * @param function the computable function
   * @param x0 the abscissa of the lower bound of the interval
   * @param x1 the abscissa of the higher bound of the interval
   * @param xIter the expected iteration
   * @param xRoot the expected root
   */
  private void testFindRootFunctionNonConverging(Class<?> clazz, String functionExpression, double x0, double x1, double xIter, double xRoot) {
    ComputableFunction function = RootFinderTestUtils.compileFunctionFromString(functionExpression);
    ConvergenceChecker convergenceChecker = RootFinderTestUtils.simpleConvergenceChecker();
    RootsFinder solver = clazz.newInstance();

    assertFalse(solver.findRoot(function, convergenceChecker, xIter, x0, function.valueAt(x0), x1, function.valueAt(x1)).isSuccess());
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

  /**
   * Helper method to test {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that always returns NaN
   *
   * @param clazz the class of the solver
   * @param function the computable function
   * @param x0 the abscissa of the lower bound of the interval
   */
  private void testFindRootFunctionNaN(Class<?> clazz, ComputableFunction function, double x0) {
    ConvergenceChecker convergenceChecker = RootFinderTestUtils.simpleConvergenceChecker();
    RootsFinder solver = clazz.newInstance();

    assertFalse(solver.findRoot(function, convergenceChecker, 100, x0, function.valueAt(x0), x0, function.valueAt(x0)).isSuccess());
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

  /**
   * Helper method to test {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with a function that throws an exception
   *
   * @param clazz the class of the solver
   * @param function the computable function
   */
  private void testFindRootFunctionException(Class<?> clazz, ComputableFunction function) {
    ConvergenceChecker convergenceChecker = RootFinderTestUtils.simpleConvergenceChecker();
    RootsFinder solver = clazz.newInstance();

    try {
      solver.findRoot(function, convergenceChecker, 100, 0.0, 0.0, 0.0, 0.0);
      fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      // expected
    }

    assertFalse(solver.isSuccess());
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

}