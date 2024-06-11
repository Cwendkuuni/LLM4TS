package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Typical use case
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4; // Function x^2 - 4
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker();
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);
    
    try {
      assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    } catch (FunctionException e) {
      fail("Unexpected FunctionException");
    }
    
    assertEquals(2.0, solver.getRoot(), 1e-10); // Expected root is 2.0
  }
  
  // Add more test cases for other methods as needed

}