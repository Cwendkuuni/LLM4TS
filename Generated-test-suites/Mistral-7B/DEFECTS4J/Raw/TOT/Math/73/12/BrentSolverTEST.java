package org.spaceroots.mantissa.roots;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setup() {
    solver = new BrentSolver();
    function = FunctionUtils.sinFunction();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        if (Math.abs(fx2) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NONE;
      }
    };
  }

  @Test
  public void testFindRoot_simple() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI / 2;

    boolean result = solver.findRoot(function, checker, 10, x0, FunctionUtils.sin(x0), x1, FunctionUtils.sin(x1));

    assertEquals("Root not found in the given interval", true, result);
    assertEquals("Root is not the expected value", root, solver.getRoot(), 1e-10);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws FunctionException {
    ComputableFunction function = () -> { throw new FunctionException(); };
    double x0 = 0.0;
    double x1 = 1.0;

    solver.findRoot(function, checker, 10, x0, 0.0, x1, FunctionUtils.sin(x1));
  }

  @Test
  public void testFindRoot_maxIter() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI / 2;

    BrentSolver solver2 = new BrentSolver();

    boolean result = solver2.findRoot(function, checker, 0, x0, FunctionUtils.sin(x0), x1, FunctionUtils.sin(x1));

    assertFalse("Root should not be found in the given interval", result);
  }

  @Test
  public void testFindRoot_convergence() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = 1.0;

    boolean result = solver.findRoot(function, checker, 10, x0, 0.0, x1, FunctionUtils.cos(x1));

    assertEquals("Root not found in the given interval", true, result);
    assertEquals("Root is not the expected value", root, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRoot_interval() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 10, x0, FunctionUtils.sin(x0), x1, FunctionUtils.sin(x1));

    assertTrue("Root should be found in the given interval", result);
    assertEquals("Root is not the expected value", root, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRoot_zeroInterval() throws FunctionException {
    double x0 = 0.0;
    double x1 = 0.0;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 10, x0, FunctionUtils.sin(x0), x1, FunctionUtils.sin(x1));

    assertFalse("Root should not be found in the given interval", result);
  }
}