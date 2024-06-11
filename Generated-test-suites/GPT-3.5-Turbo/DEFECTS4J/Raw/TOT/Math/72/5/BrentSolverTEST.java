import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot_WithValidFunctionAndInterval_ShouldReturnTrueAndSetRoot() {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter = 100;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    // Act
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    // Assert
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_WithInvalidFunction_ShouldThrowFunctionException() {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new RuntimeException("Invalid function");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    // Act & Assert
    try {
      solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      assertEquals("Invalid function", e.getMessage());
    }
  }

  @Test
  public void testFindRoot_WithConvergingChecker_ShouldReturnTrueAndSetRoot() {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 2;
    double f1 = function.valueAt(x1);

    // Act
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    // Assert
    assertTrue(result);
    assertEquals(Math.sqrt(2), solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_WithNonConvergingChecker_ShouldReturnFalseAndNotSetRoot() {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 3;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 2;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 2;
    double f1 = function.valueAt(x1);

    // Act
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    // Assert
    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), 1e-6);
  }

}