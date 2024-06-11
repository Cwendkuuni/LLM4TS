package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver brentSolver;
  private ConvergenceChecker convergenceChecker;
  private MockComputableFunction function;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
    convergenceChecker = mock(ConvergenceChecker.class);
    function = new MockComputableFunction();
  }

  @Test
  public void testFindRoot_convergenceCheckerReturnsLow_rootIsReturned() throws FunctionException {
    double x0 = 1.0;
    double f0 = 2.0;
    double x1 = 3.0;
    double f1 = 1.0;
    when(convergenceChecker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.LOW);

    assertTrue(brentSolver.findRoot(function, convergenceChecker, 10, x0, f0, x1, f1));
    assertEquals(x0, brentSolver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergenceCheckerReturnsHigh_rootIsReturned() throws FunctionException {
    double x0 = 1.0;
    double f0 = 2.0;
    double x1 = 3.0;
    double f1 = 1.0;
    when(convergenceChecker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.HIGH);

    assertTrue(brentSolver.findRoot(function, convergenceChecker, 10, x0, f0, x1, f1));
    assertEquals(x1, brentSolver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergenceCheckerReturnsNoConvergence_falseReturned() throws FunctionException {
    double x0 = 1.0;
    double f0 = 2.0;
    double x1 = 3.0;
    double f1 = 1.0;
    when(convergenceChecker.converged(x0, f0, x1, f1)).thenReturn(ConvergenceChecker.NO_CONVERGENCE);

    assertFalse(brentSolver.findRoot(function, convergenceChecker, 10, x0, f0, x1, f1));
    assertNotNull(brentSolver.getRoot());
  }

  @Test
  public void testFindRoot_maxIterationsReached_falseReturned() throws FunctionException {
    double x0 = 1.0;
    double f0 = 2.0;
    double x1 = 3.0;
    double f1 = 1.0;
    ComputableFunction functionWithException = (x) -> {
      throw new FunctionException("Exception");
    };
    when(convergenceChecker.converged(x0, f0, x1, f1)).thenReturn(null);

    assertFalse(brentSolver.findRoot(functionWithException, convergenceChecker, 1, x0, f0, x1, f1));
    assertNotNull(brentSolver.getRoot());
  }

  @Test
  public void testFindRoot_functionThrowsException_falseReturned() throws FunctionException {
    double x0 = 1.0;
    double f0 = 2.0;
    double x1 = 3.0;
    double f1 = 1.0;
    ComputableFunction functionWithException = (x) -> {
      throw new FunctionException("Exception");
    };

    assertFalse(brentSolver.findRoot(functionWithException, convergenceChecker, 10, x0, f0, x1, f1));
    assertNotNull(brentSolver.getRoot());
  }
}