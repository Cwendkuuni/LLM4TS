package org.spaceroots.mantissa.roots.test;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /** Simple convergence checker. */
  static ConvergenceChecker convergenceChecker = new ConvergenceChecker() {

    @Override
    public int converged(double xLower, double fLower, double xUpper, double fUpper) {
      if (Math.abs(fUpper) < 1e-12) {
        return ConvergenceChecker.HIGH;
      } else if (Math.abs(fLower) < 1e-12) {
        return ConvergenceChecker.LOW;
      } else {
        return ConvergenceChecker.NONE;
      }
    }
  };

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} */
  @Test
  public void testFindRoot() throws Exception {

    // Test function: f(x) = x^3 - 2
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };

    // Test interval: [-2, 2]
    BrentSolver solver = new BrentSolver();
    double root = solver.findRoot(function, convergenceChecker, 100, -2, Math.pow(2, 3) - 2, 2, Math.pow(2, 3) - 1).getRoot();
    Assert.assertEquals(0.0, root, epsilon);

  }

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with a function having roots at negative values */
  @Test
  public void testFindRootNegativeRoots() throws Exception {

    // Test function: f(x) = x^3 + 1
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) + 1;
      }
    };

    // Test interval: [-2, 2]
    BrentSolver solver = new BrentSolver();
    double root = solver.findRoot(function, convergenceChecker, 100, -2, -Math.pow(2, 3) - 1, 2, Math.pow(2, 3)).getRoot();
    Assert.assertEquals(-1.0, root, epsilon);

  }

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with a function having multiple roots */
  @Test
  public void testFindRootMultipleRoots() throws Exception {

    // Test function: f(x) = (x - 1)^3 (x - 2)^3
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        double xCubedMinusOne = Math.pow(x - 1, 3);
        double xCubedMinusTwo = Math.pow(x - 2, 3);
        return xCubedMinusOne * xCubedMinusTwo;
      }
    };

    // Test interval: [-3, 3]
    BrentSolver solver = new BrentSolver();
    double root1 = solver.findRoot(function, convergenceChecker, 100, -3, Math.pow(-3, 3) * 2, 3, Math.pow(3, 3) * 2).getRoot();
    Assert.assertEquals(-1.0, root1, epsilon);

    double root2 = solver.findRoot(function, convergenceChecker, 100, -3, Math.pow(-3, 3), 3, Math.pow(3, 3)).getRoot();
    Assert.assertEquals(-2.0, root2, epsilon);

  }

  /** Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)} with a function having no root in the interval */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoot() throws Exception {

    // Test function: f(x) = x^2 - 4
    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    // Test interval: [-2, 2]
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, convergenceChecker, 100, -2, Math.pow(2, 2) - 4, 2, Math.pow(2, 2) - 2);

  }

}