package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
  }

  @Test
  public void testFindRoot() throws FunctionException {
    boolean result = solver.findRoot(function, new MockConvergenceChecker(), 10, 0.0, -4.0, 2.0, 0.0);
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootNoRoot() throws FunctionException {
    boolean result = solver.findRoot(function, new MockConvergenceChecker(), 10, 5.0, 21.0, 7.0, 33.0);
    assertFalse(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException();
      }
    }, new MockConvergenceChecker(), 10, 0.0, -4.0, 2.0, 0.0);
  }

  @Test
  public void testFindRootMaxIterations() throws FunctionException {
    boolean result = solver.findRoot(function, new MockConvergenceChecker(), 0, 0.0, -4.0, 2.0, 0.0);
    assertFalse(result);
  }

  // Additional test cases...

}

class MockConvergenceChecker implements ConvergenceChecker {
  public static final int LOW = 1;
  public static final int HIGH = 2;

  public int converged(double xLow, double fLow, double xHigh, double fHigh) {
    if (xLow == 2.0) {
      return LOW;
    } else if (xHigh == 2.0){
      return HIGH;
    } else {
      return 0;
    }
  }
}