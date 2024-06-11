import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

  // Test class implementing ComputableFunction for testing purposes
  private class TestFunction implements ComputableFunction {

    private double root;

    public TestFunction(double root) {
      this.root = root;
    }

    public double valueAt(double x) {
      return x - root;
    }
  }

  // Test class implementing ConvergenceChecker for testing purposes
  private class TestConvergenceChecker implements ConvergenceChecker {

    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (Math.abs(fLow) < epsilon) {
        return LOW;
      } else if (Math.abs(fHigh) < epsilon) {
        return HIGH;
      } else {
        return NONE;
      }
    }
  }

  // Test if a root can be found in a simple interval
  @Test
  public void testFindRoot_IntervalWithRoot() throws FunctionException {
    double root = 2.0;
    double x0 = 1.0;
    double f0 = -1.0;
    double x1 = 3.0;
    double f1 = 1.0;

    BrentSolver solver = new BrentSolver();
    boolean foundRoot = solver.findRoot(new TestFunction(root),
                                        new TestConvergenceChecker(),
                                        100, x0, f0, x1, f1);
    Assert.assertTrue(foundRoot);
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  // Test if no root is found in an interval without root
  @Test
  public void testFindRoot_IntervalWithoutRoot() throws FunctionException {
    double x0 = 1.0;
    double f0 = -1.0;
    double x1 = 3.0;
    double f1 = -1.0;

    BrentSolver solver = new BrentSolver();
    boolean foundRoot = solver.findRoot(new TestFunction(2.0),
                                        new TestConvergenceChecker(),
                                        100, x0, f0, x1, f1);
    Assert.assertFalse(foundRoot);
  }

  // Test if the maximum number of iterations is respected
  @Test
  public void testFindRoot_MaxIterations() throws FunctionException {
    double root = 2.0;
    double x0 = 1.0;
    double f0 = -1.0;
    double x1 = 3.0;
    double f1 = 1.0;

    BrentSolver solver = new BrentSolver();
    boolean foundRoot = solver.findRoot(new TestFunction(root),
                                        new TestConvergenceChecker(),
                                        1, x0, f0, x1, f1);
    Assert.assertFalse(foundRoot);
  }

}