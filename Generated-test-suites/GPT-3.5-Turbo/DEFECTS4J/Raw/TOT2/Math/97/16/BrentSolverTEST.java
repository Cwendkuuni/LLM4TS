package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot_ValidInterval_ReturnsTrue() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new SampleFunction();
    ConvergenceChecker checker = new SampleConvergenceChecker();
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    int maxIter = 100;
    try {
      assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    } catch (FunctionException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testGetRoot_RootIsCalculated_ReturnsRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new SampleFunction(), new SampleConvergenceChecker(), 100, 0.0, 1.0, -1.0, -2.0);
    assertEquals(0.0, solver.getRoot(), 0.0);
  }

  // Additional test cases for edge cases and error scenarios can be added here

}

class SampleFunction implements ComputableFunction {
  public double valueAt(double x) {
    return x * x - 4;
  }
}

class SampleConvergenceChecker implements ConvergenceChecker {
  public int converged(double xLow, double fLow, double xHigh, double fHigh) {
    return xLow < xHigh ? LOW : HIGH;
  }
}