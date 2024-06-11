package org.example.junit;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testBrentSolverConstruction() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRootWithSimpleFunction() throws Exception {
    ComputableFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootWithFunctionNoRoots() throws Exception {
    ComputableFunction function = new SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    assertFalse(solver.findRoot(function, checker, 10, x0, f0, x1, f1));
  }

  // Test case for a function with multiple roots

  // Test case for a function with roots close to the interval boundaries

  // Test case for a function with very large or very small roots

  // Test case for a function with discontinuities or singularities

  // Test case for an exception when the function cannot be computed

}