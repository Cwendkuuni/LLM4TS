package org.spaceroots.mantissa.roots.test;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleFunction() throws FunctionException {
    ComputableFunction function = new RationalFunction("x, 1.0 - x^2");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 0.0;
    double root = 0.707106781186548; // approximately

    boolean result = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, brentSolver.getRoot(), 1e-5);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_errorFunction() throws FunctionException {
    ComputableFunction function = new RationalFunction("x, 1.0 / x");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_functionWithMultipleRoots() throws FunctionException {
    ComputableFunction function = new RationalFunction("x, (x - 1.0) * (x - 2.0) * (x - 3.0)");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5; // approximately

    boolean result = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, brentSolver.getRoot(), 1e-5);
  }

  @Test
  public void testFindRoot_functionWithLargeInterval() throws FunctionException {
    ComputableFunction function = new RationalFunction("x, Math.sin(x) - Math.cos(x)");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = -10.0;
    double x1 = 10.0;
    double root = 0.0; // approximately

    boolean result = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, brentSolver.getRoot(), 1e-5);
  }

  @Test
  public void testFindRoot_functionWithSmallInterval() throws FunctionException {
    ComputableFunction function = new RationalFunction("x, Math.sin(x) - Math.cos(x)");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;

    boolean result = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(0.0, brentSolver.getRoot(), 1e-5);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionWithNoRoot() throws FunctionException {
    ComputableFunction function = new RationalFunction("x, x * x - 2.0");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;

    brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_functionWithSingleRootAtEdge() throws FunctionException {
    ComputableFunction function = new RationalFunction("x, x^2 - 1.0");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;
    double root = 1.0; // exactly

    boolean result = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, brentSolver.getRoot(), 1e-5);
  }

  @Test
  public void testFindRoot_functionWithRootAtEnd() throws FunctionException {
    ComputableFunction function = new RationalFunction("x, x^2 - 1.0");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.0; // exactly

    boolean result = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, brentSolver.getRoot(), 1e-5);
  }

  @Test
  public void testFindRoot_functionWithRootAtBeginning() throws FunctionException {
    ComputableFunction function = new RationalFunction("x, x^2 - 1.0");
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 0.5;
    double x1 = 0.0;
    double root = 0.0; // exactly

    boolean result = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, brentSolver.getRoot(), 1e-5);
  }

}