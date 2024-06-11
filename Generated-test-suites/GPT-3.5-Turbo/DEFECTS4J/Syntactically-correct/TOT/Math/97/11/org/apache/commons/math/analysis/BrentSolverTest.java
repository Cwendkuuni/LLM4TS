import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot_WhenValidIntervalProvided_ShouldReturnTrue() throws FunctionException {
    // Arrange
    // Create a test function
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    // Create a test convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0.0) {
          return ConvergenceChecker.LOW;
        } else if (fHigh == 0.0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.ONGOING;
        }
      }
    };

    // Set the maximal number of iterations
    int maxIter = 100;

    // Set the interval
    double x0 = -10.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);

    // Create the BrentSolver object
    BrentSolver solver = new BrentSolver();

    // Act
    boolean hasRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    // Assert
    assertTrue(hasRoot);
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_WhenInvalidFunctionProvided_ShouldThrowFunctionException() {
    // Arrange
    // Create a test function that always throws an exception
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Test exception");
      }
    };

    // Create a test convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrime, double fPrime) {
        return ConvergenceChecker.ONGOING;
      }
    };

    // Set the maximal number of iterations
    int maxIter = 100;

    // Set the interval
    double x0 = -10.0;
    double f0 = 0.0;
    double x1 = 10.0;
    double f1 = 0.0;

    // Create the BrentSolver object
    BrentSolver solver = new BrentSolver();

    // Act and Assert
    assertThrows(FunctionException.class,
        () -> solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
  }

  @Test
  public void testFindRoot_WhenInvalidIntervalProvided_ShouldReturnFalse() throws FunctionException {
    // Arrange
    // Create a test function
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    // Create a test convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0.0) {
          return ConvergenceChecker.LOW;
        } else if (fHigh == 0.0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.ONGOING;
        }
      }
    };

    // Set the maximal number of iterations
    int maxIter = 100;

    // Set the invalid interval
    double x0 = 4.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0; // x0 > x1
    double f1 = function.valueAt(x1);

    // Create the BrentSolver object
    BrentSolver solver = new BrentSolver();

    // Act
    boolean hasRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    // Assert
    assertFalse(hasRoot);
  }

}