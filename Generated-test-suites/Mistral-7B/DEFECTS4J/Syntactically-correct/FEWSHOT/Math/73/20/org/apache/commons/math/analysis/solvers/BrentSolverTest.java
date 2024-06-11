package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * JUnit 4 test cases for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Mock implementation of the ComputableFunction interface.
   */
  static class MockFunction implements ComputableFunction {
    double value;

    @Override
    public double valueAt(double x) {
      return value;
    }

    public void setValue(double value) {
      this.value = value;
    }
  }

  /**
   * Mock implementation of the ConvergenceChecker interface.
   */
  static class MockConvergenceChecker implements ConvergenceChecker {

    enum ConvergenceStatus {
      LOW,
      HIGH,
      NONE
    }

    ConvergenceStatus status;

    @Override
    public ConvergenceChecker.ConvergenceStatus converged(double x, double fx, double x2, double fx2) {
      return status;
    }

    public void setConvergenceStatus(ConvergenceStatus status) {
      this.status = status;
    }
  }

  @Test
  public void testFindRoot_rootsInInterval() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(2.0);

    MockConvergenceChecker convergenceChecker = new MockConvergenceChecker();

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, convergenceChecker, 10, x0, 1.0, x1, 2.0);

    assertTrue(solver.getRoot() - root < epsilon);
  }

  @Test
  public void testFindRoot_noRootInInterval() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    double x0 = 1.0;
    double x1 = 2.0;

    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(2.0);

    MockConvergenceChecker convergenceChecker = new MockConvergenceChecker();

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, convergenceChecker, 10, x0, 1.0, x1, 2.0);

    assertFalse(Double.isFinite(solver.getRoot()));
  }

  @Test
  public void testFindRoot_rootsAtBoundaries() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    double x0 = 1.0;
    double x1 = 1.0;
    double root = 1.0;

    when(function.valueAt(x0)).thenReturn(2.0);
    when(function.valueAt(x1)).thenReturn(-1.0);

    MockConvergenceChecker convergenceChecker = new MockConvergenceChecker();
    convergenceChecker.setConvergenceStatus(ConvergenceChecker.LOW);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, convergenceChecker, 10, x0, 2.0, x1, -1.0);

    assertTrue(Math.abs(solver.getRoot() - root) < epsilon);
  }

  @Test
  public void testFindRoot_rootsAtEndpoints() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    double x0 = 1.0;
    double x1 = 1.0;
    double root = 1.0;

    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(1.0);

    MockConvergenceChecker convergenceChecker = new MockConvergenceChecker();
    convergenceChecker.setConvergenceStatus(ConvergenceChecker.HIGH);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, convergenceChecker, 10, x0, 1.0, x1, 1.0);

    assertTrue(solver.getRoot() - root < epsilon);
  }

  @Test
  public void testFindRoot_rootsAtMidpoint() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    when(function.valueAt(x0)).thenReturn(2.0);
    when(function.valueAt(x1)).thenReturn(-1.0);

    MockConvergenceChecker convergenceChecker = new MockConvergenceChecker();

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, convergenceChecker, 10, x0, 2.0, x1, -1.0);

    MockFunction mockFunction = new MockFunction();
    mockFunction.setValue(x -> x * x - 1.0);

    double tolerance = 0.001;
    double expectedRoot = Math.sqrt(1.0 + tolerance);

    solver.findRoot(mockFunction, convergenceChecker, 10, -2.0, -1.0, 2.0, 1.0);

    assertTrue(Math.abs(solver.getRoot() - expectedRoot) < tolerance);
  }

  @Test
  public void testFindRoot_rootsAtMidpointWithIterativeCheck() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    when(function.valueAt(x0)).thenReturn(2.0);
    when(function.valueAt(x1)).thenReturn(-1.0);

    MockConvergenceChecker convergenceChecker = new MockConvergenceChecker();

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, convergenceChecker, 10, x0, 2.0, x1, -1.0);

    MockFunction mockFunction = new MockFunction();
    mockFunction.setValue(x -> x * x - 1.0);

    double tolerance = 0.001;
    double expectedRoot = Math.sqrt(1.0 + tolerance);

    solver.findRoot(mockFunction, convergenceChecker, 10, -2.0, -1.0, 2.0, 1.0);

    MockConvergenceChecker convergenceCheckerIter = new MockConvergenceChecker();
    convergenceCheckerIter.setConvergenceStatus(ConvergenceChecker.NONE);

    solver.findRoot(mockFunction, convergenceCheckerIter, 10, -2.0, -1.0, 2.0, 1.0);

    assertTrue(Math.abs(solver.getRoot() - expectedRoot) < tolerance);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_dividedByZero() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    double x0 = 1.0;
    double x1 = 2.0;

    when(function.valueAt(x0)).thenReturn(1.0);
    when(function.valueAt(x1)).thenReturn(0.0);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 10, x0, 1.0, x1, 0.0);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    double x0 = 1.0;
    double x1 = 2.0;

    when(function.valueAt(x0)).thenThrow(new FunctionException("Function exception"));

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 10, x0, 1.0, x1, 2.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_nullArgument() throws FunctionException {
    ComputableFunction function = null;
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 10, 1.0, 1.0, 2.0, 2.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_nullChecker() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = null;

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 10, 1.0, 1.0, 2.0, 2.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_negativeMaxIter() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, -1, 1.0, 1.0, 2.0, 2.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_intervalEndPointsInSamePosition() throws FunctionException {
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);

    double x0 = 1.0;
    double x1 = 1.0;

    BrentSolver solver = new BrentSolver();

    solver.findRoot(function, checker, 10, x0, 1.0, x1, 1.0);
  }
}