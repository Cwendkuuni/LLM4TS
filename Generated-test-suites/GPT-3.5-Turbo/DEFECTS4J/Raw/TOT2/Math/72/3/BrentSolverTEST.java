import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Typical use case
    // Arrange
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ExampleFunction();
    ConvergenceChecker checker = new ExampleConvergenceChecker();
    int maxIter = 100;
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    // Act
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    // Assert
    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-10);
  }

  // Add test cases for other public methods as required

  // Edge cases and exception handling scenarios test cases

}

class ExampleFunction implements ComputableFunction {
  public double valueAt(double x) {
    return x * x - 4.0;
  }
}

class ExampleConvergenceChecker implements ConvergenceChecker {
  public int converged(double xLow, double fLow, double xHigh, double fHigh) {
    if (fLow * fHigh < 0) {
      return ConvergenceChecker.LOW;
    }
    return ConvergenceChecker.HIGH;
  }
}