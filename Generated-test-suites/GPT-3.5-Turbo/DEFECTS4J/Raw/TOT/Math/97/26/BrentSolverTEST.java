import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_typical() {
    BrentSolver solver = new BrentSolver();

    // Typical test case
    double x0 = 0.0; // lower bound of the interval
    double f0 = -1.0; // function value at lower bound
    double x1 = 2.0; // higher bound of the interval
    double f1 = 3.0; // function value at higher bound

    // Define the function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 2; // function: y = x^2 - 2
      }
    };

    // Define the convergence checker
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

    // Set max iterations to 100
    int maxIter = 100;

    try {
      boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      assertTrue(rootFound);
      double root = solver.getRoot();
      assertEquals(1.414, root, 0.001); // expected square root of 2
    } catch (FunctionException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testFindRoot_edgeCase() {
    BrentSolver solver = new BrentSolver();

    // Edge case test
    double x0 = 0.0; // lower bound of the interval
    double f0 = -1.0; // function value at lower bound
    double x1 = 2.0; // higher bound of the interval
    double f1 = 3.0; // function value at higher bound

    // Define the function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x + 1; // function: y = x^2 + 1
      }
    };

    // Define the convergence checker
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

    // Set max iterations to 100
    int maxIter = 100;

    try {
      boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      assertFalse(rootFound); // No root found within the interval
    } catch (FunctionException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testFindRoot_errorScenario() {
    BrentSolver solver = new BrentSolver();

    // Error scenario test
    double x0 = 0.0; // lower bound of the interval
    double f0 = -1.0; // function value at lower bound
    double x1 = 2.0; // higher bound of the interval
    double f1 = 3.0; // function value at higher bound

    // Define the function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Error in function evaluation");
      }
    };

    // Define the convergence checker
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

    // Set max iterations to 100
    int maxIter = 100;

    try {
      boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      assertFalse(rootFound); // No root found due to error in function evaluation
    } catch (FunctionException e) {
      assertTrue(e.getMessage().equals("Error in function evaluation")); // Exception thrown as expected
    }
  }
}