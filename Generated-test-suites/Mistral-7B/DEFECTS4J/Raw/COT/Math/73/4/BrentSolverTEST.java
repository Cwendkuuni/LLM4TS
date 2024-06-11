package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @org.junit.Test
  public void testFindRoot_Basic() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();

    RationalFunction function = new RationalFunction("x^2 - 2");

    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = 0.0;

    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(1.5, solver.getRoot(), 0.001);
  }

  @org.junit.Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();

    RationalFunction function = new RationalFunction("x^2 - 2");

    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 0.0; // The interval [0, 1] does not contain a root for x^2 - 2
    double f1 = 1.0;

    try {
      solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    } catch (FunctionException ex) {
      // expected
    }
  }

  @org.junit.Test
  public void testFindRoot_ConvergenceChecker() {
    BrentSolver solver = new BrentSolver();

    RationalFunction function = new RationalFunction("x^2 - 2");

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceState(double x, double fx, double xPrev, double fxPrev) {
        if (fx < 0) {
          return ConvergenceChecker.LOW;
        } else if (fx > 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.CONVERGED;
        }
      }
    };

    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = 0.0;

    try {
      solver.findRoot(function, checker, 10, x0, f0, x1, f1);
      fail("Expected FunctionException");
    } catch (FunctionException ex) {
      // expected
    }
  }

}