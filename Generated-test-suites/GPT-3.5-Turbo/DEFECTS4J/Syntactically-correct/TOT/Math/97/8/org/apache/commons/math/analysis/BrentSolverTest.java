package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Test case 1: Typical use case - Function has a root within the given interval
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
            return x * x - 9; // Polynomial function with root at x=3
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-6) {  // Considering 0 as convergence
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-6) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    };
    int maxIter = 100;
    double x0 = 2.5;
    double f0 = function.valueAt(x0);
    double x1 = 3.5;
    double f1 = function.valueAt(x1);
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(3.0, solver.getRoot(), 1e-6); // Expected root is 3.0

    // Test case 2: Edge case - Function has a root exactly at the lower bound of the interval
    x0 = 0;
    f0 = function.valueAt(x0);
    x1 = 3;
    f1 = function.valueAt(x1);
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(0, solver.getRoot(), 1e-6); // Expected root is 0.0

    // Test case 3: Edge case - Function has a root exactly at the upper bound of the interval
    x0 = 2;
    f0 = function.valueAt(x0);
    x1 = 3;
    f1 = function.valueAt(x1);
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(3.0, solver.getRoot(), 1e-6); // Expected root is 3.0

    // Test case 4: Error scenario - Function does not have a root within the given interval
    x0 = 0;
    f0 = function.valueAt(x0);
    x1 = 1;
    f1 = function.valueAt(x1);
    
    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));

  }
}