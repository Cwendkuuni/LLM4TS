package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRootSimpleCase() throws FunctionException {
    ComputableFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -1.0, 1.0, 0.0, 0.0);
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootEdgeCase1() throws FunctionException {
    ComputableFunction function = new EdgeFunction1();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -1000.0, 1000.0, 0.0, 0.0);
    assertTrue(result);
    assertTrue(Math.abs(solver.getRoot()) > 1000.0);
  }

  @Test
  public void testFindRootEdgeCase2() throws FunctionException {
    ComputableFunction function = new EdgeFunction2();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -1.0, 1.0, 0.0, 0.0);
    assertFalse(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootErrorCase() throws FunctionException {
    ComputableFunction function = new ErrorFunction();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -1.0, 1.0, 0.0, 0.0);
  }

  @Test
  public void testGetRootValue() throws FunctionException {
    ComputableFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -1.0, 1.0, 0.0, 0.0);
    double root = solver.getRoot();
    assertEquals(0.0, root, epsilon);
  }

  private static class SimpleFunction implements ComputableFunction {
    public double valueAt(double x) {
      return Math.sin(x) - x;
    }
  }

  private static class EdgeFunction1 implements ComputableFunction {
    public double valueAt(double x) {
      return Math.exp(x) - 1000.0;
    }
  }

  private static class EdgeFunction2 implements ComputableFunction {
    public double valueAt(double x) {
      throw new UnsupportedOperationException();
    }
  }

  private static class ErrorFunction implements ComputableFunction {
    public double valueAt(double x) {
      throw new ArithmeticException("Divide by zero");
    }
  }

  private static class ConvergenceCheckerImpl implements ConvergenceChecker {
    public int converged(double x1, double f1, double x2, double f2) {
      if (f1 * f2 > 0) {
        return LOW;
      } else if (Math.abs(x1 - x2) < epsilon) {
        return HIGH;
      } else {
        return NONE;
      }
    }
  }

}