package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setup() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRootExample1() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh - fLow) < 0.01) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootExample2() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh - fLow) < 0.01) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootExample3() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh - fLow) < 0.01) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertEquals(Math.PI, solver.getRoot(), 0.01);
  }

}