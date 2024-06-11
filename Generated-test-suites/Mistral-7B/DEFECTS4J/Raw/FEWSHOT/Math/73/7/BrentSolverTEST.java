package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private MockComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new MockComputableFunction();
    checker = new MockConvergenceChecker();
  }

  @Test
  public void testFindRoot_converged_low() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    function.setExpectedValues(x0, 1.0, x1, 2.0, expectedRoot, 0.0);

    solver.findRoot(function, checker, 10, x0, 1.0, x1, 2.0);

    assertEquals(expectedRoot, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_converged_high() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    function.setExpectedValues(x0, 1.0, x1, 2.0, 0.0, 2.0);

    solver.findRoot(function, checker, 10, x0, 1.0, x1, 2.0);

    assertEquals(expectedRoot, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_converged_interval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    function.setExpectedValues(x0, 1.0, x1, 2.0, 0.0, 0.0);

    solver.findRoot(function, checker, 10, x0, 1.0, x1, 2.0);

    assertEquals(expectedRoot, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_notConverged() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    function.setExpectedValues(x0, 1.0, x1, 2.0, 0.0, 0.0);

    for (int i = 1; i <= 10; i++) {
      if (solver.findRoot(function, checker, 10, x0, 1.0, x1, 2.0)) {
        break;
      }
    }

    assertFalse(solver.isRootFound());
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionExceptionThrown() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;

    function.setThrowException(true);

    solver.findRoot(function, checker, 10, x0, 1.0, x1, 2.0);
  }

  @Test
  public void testIsRootFound() {
    assertFalse(solver.isRootFound());

    solver.findRoot(function, checker, 10, 0.0, 1.0, 0.0, 1.0);

    assertTrue(solver.isRootFound());
  }

  private static class MockComputableFunction implements ComputableFunction {
    private double x;
    private double y;
    private boolean throwException;

    public MockComputableFunction() {
      this.x = Double.NaN;
      this.y = Double.NaN;
      this.throwException = false;
    }

    public void setExpectedValues(double... expectedValues) {
      this.x = expectedValues[0];
      this.y = expectedValues[1];
    }

    public void setThrowException(boolean throwException) {
      this.throwException = throwException;
    }

    @Override
    public double valueAt(double x) throws FunctionException {
      this.x = x;
      if (this.throwException) {
        throw new FunctionException();
      }
      return this.y;
    }
  }

  private static class MockConvergenceChecker implements ConvergenceChecker {
    public static final int LOW = 0;
    public static final int HIGH = 1;
    public static final int NOT_CONVERGED = 2;

    @Override
    public int converged(double x, double fx, double x2, double fx2) {
      if (x < x2 && fx < 0) {
        return LOW;
      }
      if (x > x2 && fx > 0) {
        return HIGH;
      }
      return NOT_CONVERGED;
    }
  }

}