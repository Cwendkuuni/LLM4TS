package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

 private static ComputableFunction function = new ComputableFunction() {
   @Override
   public double valueAt(double x) throws FunctionException {
      return Math.pow(x, 3) - 2;
   }
 };

 private static ConvergenceChecker checker = new ConvergenceChecker() {
   @Override
   public int converged(double x, double fx, double x1, double fx1) {
      if (Math.abs(fx) < Double.MIN_VALUE) {
         return ConvergenceChecker.LOW;
      } else if (Math.abs(fx1) < Double.MIN_VALUE) {
         return ConvergenceChecker.HIGH;
      } else {
         return ConvergenceChecker.NONE;
      }
   }
 };

 @Test
 public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    double x0 = -2.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;
    assertEquals(0.0, root, 0.001);
 }

 @Test(expected = FunctionException.class)
 public void testFindRootException() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 0.0;
    solver.findRoot(new ComputableFunction() {
       @Override
       public double valueAt(double x) throws FunctionException {
          throw new FunctionException("Test function exception");
       }
    }, checker, 100, x0, 0.0, x1, 0.0);
 }

 @Test
 public void testFindRootDiverges() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 0.0;
    double root = solver.findRoot(new ComputableFunction() {
       @Override
       public double valueAt(double x) throws FunctionException {
          return Math.log(Math.abs(x) + 1.0);
       }
    }, checker, 100, x0, 0.0, x1, 0.0);
    assertNull(root);
 }
}