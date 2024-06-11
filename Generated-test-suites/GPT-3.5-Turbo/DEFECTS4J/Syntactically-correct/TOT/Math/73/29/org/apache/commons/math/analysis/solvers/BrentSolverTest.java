import org.junit.Assert;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot_typical() throws FunctionException {
    // Create a BrentSolver object
    BrentSolver brentSolver = new BrentSolver();

    // Create a simple linear function
    ComputableFunction linearFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return 2 * x + 1;
      }
    };

    // Define checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    // Find root within interval [0, 10] (a root exists at x = -1/2)
    boolean rootFound = brentSolver.findRoot(linearFunction, checker, 100, 0, 1, 10, 21);
    Assert.assertTrue(rootFound);
    Assert.assertEquals(-0.5, brentSolver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_edge() throws FunctionException {
    // Create a BrentSolver object
    BrentSolver brentSolver = new BrentSolver();

    // Create a quadratic function
    ComputableFunction quadraticFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    // Define checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    // Find root within interval [-10, 10] (roots exists at x = -2 and x = 2)
    boolean rootFound = brentSolver.findRoot(quadraticFunction, checker, 100, -10, 96, 10, 96);
    Assert.assertTrue(rootFound);
    Assert.assertEquals(-2.0, brentSolver.getRoot(), 1e-6);

    rootFound = brentSolver.findRoot(quadraticFunction, checker, 100, -10, 96, 10, 96);
    Assert.assertTrue(rootFound);
    Assert.assertEquals(2.0, brentSolver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_error() throws FunctionException {
    // Create a BrentSolver object
    BrentSolver brentSolver = new BrentSolver();

    // Create a constant function
    ComputableFunction constantFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return 1;
      }
    };

    // Define checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    // Find root within interval [-1, 1] (no root exists)
    boolean rootFound = brentSolver.findRoot(constantFunction, checker, 100, -1, 1, 1, 1);
    Assert.assertFalse(rootFound);
  }

}