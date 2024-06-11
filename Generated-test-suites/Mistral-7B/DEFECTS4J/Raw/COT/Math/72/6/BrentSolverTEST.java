package org.spaceroots.mantissa.roots.test;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.RootsFinder;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleFunction() throws Exception {
    ComputableFunction function = new TestFunctions.RationalFunction(1.0, 2.0);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1))
        .getRoot();

    Assert.assertEquals(Math.PI / 4, Math.atan(root), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionThrowsException() throws Exception {
    ComputableFunction function = new FunctionThrowingException();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_FunctionWithThreeRoots() throws Exception {
    ComputableFunction function = new TestFunctions.CubicFunction(-1.0, 1.0, 1.0, -1.0);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = -1.5;
    double x1 = 1.5;
    double[] roots = {-1.0, 0.0, 1.0};
    for (double root : roots) {
      Assert.assertEquals(root, solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot());
      x0 = root;
    }
  }

  @Test
  public void testFindRoot_FunctionWithRootAtBoundary() throws Exception {
    ComputableFunction function = new TestFunctions.QuadraticFunction(1.0, 2.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();

    Assert.assertEquals(1.0, root, 0.001);
  }

  @Test
  public void testFindRoot_FunctionWithDivergingRoot() throws Exception {
    ComputableFunction function = new TestFunctions.LogFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).isFound());
  }

  @Test
  public void testFindRoot_FunctionWithRootOutsideInterval() throws Exception {
    ComputableFunction function = new TestFunctions.QuadraticFunction(1.0, 2.0, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 0.5;
    Assert.assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).isFound());
  }

  private static class FunctionThrowingException implements ComputableFunction {
    @Override
    public double valueAt(double x) throws FunctionException {
      throw new FunctionException("Test function exception");
    }
  }

}