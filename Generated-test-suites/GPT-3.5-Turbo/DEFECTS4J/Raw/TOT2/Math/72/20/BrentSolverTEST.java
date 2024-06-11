import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Typical use case
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4; // f(x) = x^2 - 4
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    boolean result = solver.findRoot(function, checker, 100, 1.0, -3.0, 2.0, 0.0);
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testGetRoot() {
    // Typical use case
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4; // f(x) = x^2 - 4
      }
    }, new ConvergenceChecker(), 100, 1.0, -3.0, 2.0, 0.0);
    assertEquals(2.0, solver.getRoot(), 1e-10);

    // Edge case: Root is at boundary
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x; // f(x) = x
      }
    }, new ConvergenceChecker(), 100, -10.0, -10.0, 10.0, 10.0);
    assertEquals(-10.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootException() {
    // Error scenario: Function throws an exception
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        throw new RuntimeException("Function exception");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    try {
      solver.findRoot(function, checker, 100, 1.0, -3.0, 2.0, 0.0);
      fail("Exception not thrown");
    } catch (FunctionException e) {
      // Expected exception
      assertTrue(true);
    }
  }

}