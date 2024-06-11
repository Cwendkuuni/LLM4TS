import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    // Test 1: function has a root in the interval [0, 1]
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);

    // Test 2: function has a root in the interval [-1, 0]
    x0 = -1.0;
    f0 = function.valueAt(x0);
    x1 = 0.0;
    f1 = function.valueAt(x1);
    result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  private ComputableFunction function = new ComputableFunction() {
    public double valueAt(double x) {
      return x * x - 4.0;
    }
  };

  private ConvergenceChecker checker = new ConvergenceChecker() {
    public int converged(double x1, double f1, double x2, double f2) {
      if (Math.abs(f1) < epsilon) {
        return ConvergenceChecker.LOW;
      } else if (Math.abs(f2) < epsilon) {
         return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.NONE;
      }
    }
  };

  private double expectedRoot = 2.0;
  private double epsilon = 1e-9;

}