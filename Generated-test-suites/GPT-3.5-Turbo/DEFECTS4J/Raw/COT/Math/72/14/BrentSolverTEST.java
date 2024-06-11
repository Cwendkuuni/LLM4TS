import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create a BrentSolver object
    BrentSolver solver = new BrentSolver();

    // Define the function and convergence checker
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return UNCONVERGED;
        }
      }
    };

    // Call the findRoot method
    boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 5, 21);

    // Check the result
    assertEquals(true, rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_noRoot() {
    // Create a BrentSolver object
    BrentSolver solver = new BrentSolver();

    // Define the function and convergence checker
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x + 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return UNCONVERGED;
        }
      }
    };

    // Call the findRoot method
    boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 5, 21);

    // Check the result
    assertEquals(false, rootFound);
    assertEquals(Double.NaN, solver.getRoot(), 1e-6);
  }

  // Add more test cases for other methods in BrentSolver class

}