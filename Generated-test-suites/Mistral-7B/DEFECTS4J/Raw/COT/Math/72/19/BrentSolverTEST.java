package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.function.FunctionUtils;
import org.apache.commons.math3.function.UnivariateFunction;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BracketedBrentSolver;
import org.apache.commons.math3.solvers.ConvergenceChecker;
import org.apache.commons.math3.solvers.LBrentSolver;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.RootsFinder;

/**
 * Test class for {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}.
   * @throws Exception if an error occurs
   */
  @Test
  public void testFindRoot() throws Exception {
    // Test with a simple function
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLower, double fxLower,
          double xUpper, double fxUpper) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fxUpper) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    RootsFinder solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;
    double root = 0.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertEquals(root, solver.getRoot(), 0.001);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    // Test with a function having no roots
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.cos(x) - 0.5;
      }
    };

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Root not found.");
    } catch (NoBracketingException e) {
      // Expected
    } catch (MaxIterationsExceededException e) {
      fail("Too many iterations.");
    } catch (ConvergenceException e) {
      fail("Convergence failure.");
    }

    // Test with a function having multiple roots
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 6 * x + 4;
      }
    };

    double xRoot = 1.0;
    double fRoot = function.valueAt(xRoot);

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), xRoot, function.valueAt(xRoot));
      assertEquals(root, solver.getRoot(), 0.001);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    // Test with a function having complex roots
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) + 1.0;
      }
    };

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Complex roots not detected.");
    } catch (FunctionException e) {
      assertTrue(e.getMessage().contains("Complex roots found"));
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with {@link BracketedBrentSolver}.
   * @throws Exception if an error occurs
   */
  @Test
  public void testFindRootWithBracketedBrentSolver() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLower, double fxLower,
          double xUpper, double fxUpper) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fxUpper) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    RootsFinder solver = new LBrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Root not found.");
    } catch (NoBracketingException e) {
      // Expected
    } catch (MaxIterationsExceededException e) {
      fail("Too many iterations.");
    } catch (ConvergenceException e) {
      fail("Convergence failure.");
    }

    UnivariateFunction brentFunction = new UnivariateFunction() {
      @Override
      public double value(double x) {
        return function.valueAt(x);
      }

      @Override
      public double derivative(double x) {
        return function.derivativeAt(x);
      }
    };

    BracketedBrentSolver bracketedBrentSolver = new BracketedBrentSolver();

    solver = bracketedBrentSolver;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      double root = solver.getRoot();
      assertEquals(Math.asin(0.5), root, 0.001);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  /**
   * Test method for {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * with {@link BrentSolver}.
   * @throws Exception if an error occurs
   */
  @Test
  public void testFindRootWithBrentSolver() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - 0.5;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLower, double fxLower,
          double xUpper, double fxUpper) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fxUpper) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };

    RootsFinder solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Root not found.");
    } catch (NoBracketingException e) {
      // Expected
    } catch (MaxIterationsExceededException e) {
      fail("Too many iterations.");
    } catch (ConvergenceException e) {
      fail("Convergence failure.");
    }

    UnivariateFunction brentFunction = new UnivariateFunction() {
      @Override
      public double value(double x) {
        return function.valueAt(x);
      }

      @Override
      public double derivative(double x) {
        return function.derivativeAt(x);
      }
    };

    try {
      solver.findRoot(brentFunction, checker, 100, x0, brentFunction.value(x0), x1, brentFunction.value(x1));
      double root = solver.getRoot();
      assertEquals(Math.asin(0.5), root, 0.001);
    } catch (FunctionException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

}