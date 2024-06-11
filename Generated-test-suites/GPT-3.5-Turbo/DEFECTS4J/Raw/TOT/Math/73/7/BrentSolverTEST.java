import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot_WithValidInterval_ShouldReturnTrueAndSetRoot() throws FunctionException {
    // Arrange
    double expectedRoot = 1.0;
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return LOW;
        }
        if (Math.abs(fHigh) < epsilon) {
          return HIGH;
        }
        return NONE;
      }
    };
    int maxIter = 100;
    double x0 = -2.0;
    double f0 = -3.0;
    double x1 = 2.0;
    double f1 = 3.0;

    // Act
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double actualRoot = solver.getRoot();

    // Assert
    assertTrue(result);
    assertEquals(expectedRoot, actualRoot, epsilon);
  }

  @Test
  public void testFindRoot_WithInvalidInterval_ShouldReturnFalseAndNotSetRoot() throws FunctionException {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return LOW;
        }
        if (Math.abs(fHigh) < epsilon) {
          return HIGH;
        }
        return NONE;
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 1.0;

    // Act
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double actualRoot = solver.getRoot();

    // Assert
    assertFalse(result);
    assertTrue(Double.isNaN(actualRoot));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_WithInvalidFunction_ShouldThrowFunctionException() throws FunctionException {
    // Arrange
    // Invalid function that throws an exception
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Invalid function");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return LOW;
        }
        if (Math.abs(fHigh) < epsilon) {
          return HIGH;
        }
        return NONE;
      }
    };
    int maxIter = 100;
    double x0 = -2.0;
    double f0 = -3.0;
    double x1 = 2.0;
    double f1 = 3.0;

    // Act
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }

}