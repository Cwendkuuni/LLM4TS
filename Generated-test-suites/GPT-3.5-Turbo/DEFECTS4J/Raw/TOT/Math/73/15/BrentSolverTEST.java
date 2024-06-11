import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot_typicalCase() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunctionImpl();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();

    boolean rootFound = solver.findRoot(function, checker, 100, 1.0, -1.0, 2.0, 2.0);

    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRoot_edgeCase() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunctionImpl();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();

    boolean rootFound = solver.findRoot(function, checker, 100, Double.MIN_VALUE, -1.0, 0.0, 0.0);

    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRoot_errorCase() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunctionImpl();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();

    try {
      boolean rootFound = solver.findRoot(function, checker, 100, 1.0, Double.POSITIVE_INFINITY, 2.0, 2.0);
      fail("FunctionException was expected but not thrown");
    } catch (FunctionException e) {
      assertTrue(true);
    }
  }

  private class ComputableFunctionImpl implements ComputableFunction {

    public double valueAt(double x) {
      // Implement the function here
      return x * x - 1;
    }
  }

  private class ConvergenceCheckerImpl implements ConvergenceChecker {

    public static final int LOW = 0;
    public static final int HIGH = 1;

    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (Math.abs(fLow) < 1e-12) {
        return LOW;
      } else if (Math.abs(fHigh) < 1e-12) {
        return HIGH;
      } else {
        return -1;
      }
    }
  }
}