package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

 private ConvergenceChecker checker = new ConvergenceChecker() {
   @Override
   public int convergence(double xLow, double fLow, double xHigh, double fHigh) {
      if (xLow < 0 && fLow < 0) {
         return LOW;
      } else if (xHigh > 0 && fHigh < 0) {
         return HIGH;
      } else {
         return NONE;
      }
   }
 };

 @Test
 public void testFindRoot() throws FunctionException {
    MockComputableFunction function = new MockComputableFunction();
    function.setExpectedFunctionValueAt(-2.0, -3.0);
    function.setExpectedFunctionValueAt(0.0, 0.0);
    function.setExpectedFunctionValueAt(2.0, 3.0);

    BrentSolver solver = new BrentSolver();
    double x0 = -2.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, -3.0, x1, 3.0);

    assertEquals(0.0, solver.getRoot(), 0.001);
 }

 @Test
 public void testFindRoot_noRoot() throws FunctionException {
    MockComputableFunction function = new MockComputableFunction();
    function.setExpectedFunctionValueAt(-2.0, -3.0);
    function.setExpectedFunctionValueAt(0.0, 0.0);
    function.setExpectedFunctionValueAt(2.0, 4.0);

    BrentSolver solver = new BrentSolver();
    double x0 = -2.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, -3.0, x1, 3.0);

    assertNull(solver.getRoot());
 }

 @Test
 public void testFindRoot_noConvergence() throws FunctionException {
    MockComputableFunction function = new MockComputableFunction();
    function.setExpectedFunctionValueAt(-2.0, -3.0);
    function.setExpectedFunctionValueAt(0.0, 0.0);
    function.setExpectedFunctionValueAt(2.0, 0.0);

    BrentSolver solver = new BrentSolver();
    double x0 = -2.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, -3.0, x1, 3.0);

    assertFalse(solver.getRoot() != Double.NaN);
 }

}