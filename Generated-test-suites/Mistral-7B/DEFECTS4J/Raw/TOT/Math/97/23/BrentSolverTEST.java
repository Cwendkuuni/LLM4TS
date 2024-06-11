package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.DummyFunction;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Unit tests for {@link BrentSolver}.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.PROGRESS;
      }
    };
  }

  /**
   * Test finding a root in a simple case.
   */
  @Test
  public void testFindRootSimple() throws FunctionException {
    ComputableFunction function = new DummyFunction(0.0);
    double root = 1.0;
    double x0 = 1.1;
    double x1 = 0.9;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), 1e-6);
  }

  /**
   * Test finding a root in a case where the interval is too large.
   */
  @Test
  public void testFindRootLargeInterval() throws FunctionException {
    ComputableFunction function = new DummyFunction(0.0);
    double root = 1.0;
    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertTrue(Double.isNaN(solver.getRoot()));
  }

  /**
   * Test finding a root in a case where the function is not monotonic.
   */
  @Test
  public void testFindRootNotMonotonic() throws FunctionException {
    ComputableFunction function = new DummyFunction(0.0, 1.0); // function f(x) = x

    double root = 0.5;
    double x0 = 0.1;
    double x1 = 0.9;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail();
    } catch (AssertionError e) {
      // expected
    }
  }

  /**
   * Test finding a root in a case where the function is not defined in the interval.
   */
  @Test
  public void testFindRootFunctionUndefined() throws FunctionException {
    ComputableFunction function = new DummyFunction(Double.POSITIVE_INFINITY);

    double root = 0.0;
    double x0 = -1.0;
    double x1 = 1.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail();
    } catch (AssertionError e) {
      // expected
    }
  }

  /**
   * Test finding a root in a case where the function throws an exception.
   */
  @Test
  public void testFindRootFunctionException() throws FunctionException {
    ComputableFunction function = new FunctionThrowingException();

    double root = 0.0;
    double x0 = 0.0;
    double x1 = 0.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail();
    } catch (FunctionException e) {
      // expected
    }
  }

}

class DummyFunction implements ComputableFunction {

  private double value;

  public DummyFunction(double value) {
    this.value = value;
  }

  @Override
  public double valueAt(double x) {
    return value;
  }

}

class FunctionThrowingException implements ComputableFunction {

  @Override
  public double valueAt(double x) throws FunctionException {
    throw new FunctionException();
  }

}