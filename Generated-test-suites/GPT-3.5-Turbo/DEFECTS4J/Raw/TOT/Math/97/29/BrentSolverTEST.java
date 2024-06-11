import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot_WhenLowerAndHigherBoundsAreValid_ExpectRootFound() throws FunctionException {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        return LOW;
      }
    };
    double lowerBound = 4;
    double lowerBoundValue = -1;
    double higherBound = 6;
    double higherBoundValue = 1;

    BrentSolver solver = new BrentSolver();

    // Act
    boolean result = solver.findRoot(function, checker, 100, lowerBound, lowerBoundValue, higherBound, higherBoundValue);

    // Assert
    assertTrue(result);
    assertEquals(5, solver.getRoot(), 0);
  }

  @Test
  public void testFindRoot_WhenLowerAndHigherBoundsAreInvalid_ExpectRootNotFound() throws FunctionException {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        return NOT_CONVERGED;
      }
    };
    double lowerBound = 4;
    double lowerBoundValue = -1;
    double higherBound = 6;
    double higherBoundValue = 1;

    BrentSolver solver = new BrentSolver();

    // Act
    boolean result = solver.findRoot(function, checker, 1, lowerBound, lowerBoundValue, higherBound, higherBoundValue);

    // Assert
    assertFalse(result);
  }

  // Other test cases for the remaining methods...
  
}