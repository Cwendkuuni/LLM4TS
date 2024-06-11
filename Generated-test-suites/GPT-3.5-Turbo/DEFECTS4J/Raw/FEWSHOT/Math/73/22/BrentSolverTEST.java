package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return Math.sin(x);
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) {
          return ConvergenceChecker.LOW;
        }
        if (fHigh == 0) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    
    BrentSolver solver = new BrentSolver();
    
    boolean found = solver.findRoot(function, checker, 100, 0, 0, Math.PI, 0);
    assertTrue(found);
    assertEquals(0, solver.getRoot(), 1e-15);
    
    found = solver.findRoot(function, checker, 100, Math.PI/2, 0, Math.PI, 0);
    assertTrue(found);
    assertEquals(Math.PI, solver.getRoot(), 1e-15);
    
    found = solver.findRoot(function, checker, 100, Math.PI/4, 0, 3*Math.PI/4, 0);
    assertFalse(found);
  }
}