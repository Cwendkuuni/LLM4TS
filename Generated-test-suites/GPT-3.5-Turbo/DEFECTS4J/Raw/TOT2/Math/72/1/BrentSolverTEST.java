import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRootWithValidInput() {
    // Given
    BrentSolver solver = new BrentSolver();
    // Create a dummy function and checker for testing
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    
    // When
    boolean rootFound = solver.findRoot(function, checker, 100, 1.0, -3.0, 3.0, 7.0);
    
    // Then
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-15);
  }

  @Test
  public void testFindRootWithInvalidInput() {
    // Given
    BrentSolver solver = new BrentSolver();
    // Create a dummy function and checker for testing
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 0.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    
    // When
    boolean rootFound = solver.findRoot(function, checker, 100, 1.0, 0.0, 3.0, 0.0);
    
    // Then
    assertFalse(rootFound);
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  // Add more test cases for other public methods if needed

}