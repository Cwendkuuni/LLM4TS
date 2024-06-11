import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Create an instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Define the function and checker
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 3;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow * fHigh < 0) {
          return ConvergenceChecker.HIGH;
        } else if (fLow == 0) {
          return ConvergenceChecker.LOW;
        } else if (fHigh == 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    // Set the maximum number of iterations and initial interval
    int maxIter = 100;
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    // Call the findRoot method
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    // Check if a root was found
    assertTrue(foundRoot);

    // Check the root value
    assertEquals(1.732, solver.getRoot(), 0.001);
  }

  // Add more test methods for other methods in BrentSolver class

}