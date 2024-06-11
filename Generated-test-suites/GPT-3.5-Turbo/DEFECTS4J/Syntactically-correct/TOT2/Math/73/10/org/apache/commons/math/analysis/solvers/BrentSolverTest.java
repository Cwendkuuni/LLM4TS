import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Typical use case
    BrentSolver solver = new BrentSolver();
    double x0 = 2.0;
    double f0 = 3.0;
    double x1 = 4.0;
    double f1 = 5.0;
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) { return x * x - 4; }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    int maxIter = 100;
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));

    // Edge case where maximum number of iterations is reached
    x0 = 0.0;
    f0 = 0.0;
    x1 = 5.0;
    f1 = 25.0;
    assertFalse(solver.findRoot(function, checker, 5, x0, f0, x1, f1));

    // Exception handling scenario
    try {
        solver.findRoot(function, checker, maxIter, 1.0, 0.0, 2.0, 0.0);
        fail("Expected FunctionException");
    } catch (FunctionException e) {
        // Expected exception
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

}