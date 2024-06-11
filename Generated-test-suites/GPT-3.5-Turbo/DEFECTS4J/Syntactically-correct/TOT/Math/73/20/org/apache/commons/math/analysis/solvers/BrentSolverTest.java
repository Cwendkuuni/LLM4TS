import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_WhenRootIsInInterval_ReturnsTrue() throws FunctionException {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-10) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-10) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = -5;
    double x1 = 10;
    double f1 = 5;
    BrentSolver solver = new BrentSolver();

    // Act
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = solver.getRoot();

    // Assert
    assertTrue(result);
    assertEquals(5, root, 0.0001);
  }

  @Test
  public void testFindRoot_WhenRootIsNotInInterval_ReturnsFalse() throws FunctionException {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-10) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-10) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = -10;
    double x1 = 10;
    double f1 = -10;
    BrentSolver solver = new BrentSolver();

    // Act
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = solver.getRoot();

    // Assert
    assertFalse(result);
    assertTrue(Double.isNaN(root));
  }

  // Add more test cases for each method if necessary

}