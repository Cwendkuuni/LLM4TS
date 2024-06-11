package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Typical use case
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4; // Function x^2 - 4, root at x = 2
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    boolean found = solver.findRoot(function, checker, 100, 1.0, -3.0, 3.0, 5.0);
    
    assertTrue(found);
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  // Add more test cases for other public methods including edge cases and error scenarios

}