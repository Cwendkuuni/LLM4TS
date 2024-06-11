import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver
 */
public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {
    // Test case 1: x^2 - 1 = 0
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 1;
      }

      @Override
      public double derivativeAt(double x) {
        return 2 * x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        double tol = 1e-6;
        if (Math.abs(f) < tol) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(x - xLow) < tol) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver brentSolver = new BrentSolver();
    brentSolver.findRoot(function, checker, 10, -2.0, 2.0);
    double root = brentSolver.getRoot();
    assertEquals("Root should be -1 or 1", -1.0, root, 1e-6);

    // Test case 2: sin(x) - x = 0
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }

      @Override
      public double derivativeAt(double x) {
        return Math.cos(x) - 1;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        double tol = 1e-6;
        if (Math.abs(f) < tol) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(x - xLow) < tol) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    brentSolver = new BrentSolver();
    brentSolver.findRoot(function, checker, 10, 0.0, Math.PI / 2.0);
    root = brentSolver.getRoot();
    assertEquals("Root should be pi/4", Math.PI / 4, root, 1e-6);

    // Test case 3: f(x) = x^3 - 2
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x * x - 2;
      }

      @Override
      public double derivativeAt(double x) {
        return 3 * x * x - 2;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        double tol = 1e-6;
        if (Math.abs(f) < tol) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(x - xLow) < tol) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    brentSolver = new BrentSolver();
    brentSolver.findRoot(function, checker, 10, -2.0, 2.0);
    root = brentSolver.getRoot();
    assertEquals("Root should be approximately -1.157", -1.157, root, 1e-3);

    // Test case 4: f(x) = exp(-x) - x
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(-x) - x;
      }

      @Override
      public double derivativeAt(double x) {
        return -Math.exp(-x) - 1;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        double tol = 1e-6;
        if (Math.abs(f) < tol) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(x - xLow) < tol) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    brentSolver = new BrentSolver();
    brentSolver.findRoot(function, checker, 10, 0.0, 1.0);
    root = brentSolver.getRoot();
    assertEquals("Root should be approximately 0.5671", 0.5671, root, 1e-4);
  }

  @Test(expected = FunctionException.class)
  public void testFunctionException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function error");
      }

      @Override
      public double derivativeAt(double x) {
        throw new FunctionException("Derivative error");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver brentSolver = new BrentSolver();
    brentSolver.findRoot(function, checker, 10, 0.0, 1.0);
  }

}