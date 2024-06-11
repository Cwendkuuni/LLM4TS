import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();

    // Positive function (typical use case)
    TestFunction function1 = new TestFunction(1.0);
    assertTrue(solver.findRoot(function1, 1.0e-6, 100, 0.0, -1.0, 1.0, 1.0));

    // Negative function (typical use case)
    TestFunction function2 = new TestFunction(-1.0);
    assertTrue(solver.findRoot(function2, 1.0e-6, 100, -1.0, 1.0, 0.0, 1.0));

    // Function converges fast (edge case)
    TestFunction function3 = new TestFunction(0.5);
    assertTrue(solver.findRoot(function3, 1.0e-6, 100, -1.0, -0.5, 0.0, 0.5));

    // Function does not converge (error scenario)
    TestFunction function4 = new TestFunction(Double.NaN);
    assertFalse(solver.findRoot(function4, 1.0e-6, 100, -1.0, -0.5, 0.0, 0.5));
  }

  private static class TestFunction implements ComputableFunction {
    private double constant;

    public TestFunction(double constant) {
      this.constant = constant;
    }

    public double valueAt(double x) {
      return constant * x;
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();

    TestFunction function = new TestFunction(2.0);
    solver.findRoot(function, 1.0e-6, 100, -1.0, -2.0, 1.0, 2.0);

    assertEquals(1.0, solver.getRoot(), 1.0e-6);
  }

}