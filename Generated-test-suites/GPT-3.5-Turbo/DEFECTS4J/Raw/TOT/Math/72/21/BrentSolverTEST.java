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
  public void testFindRoot_typicalCase() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.0001) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.0001) {
          return HIGH;
        } else {
          return UNCONVERGED;
        }
      }
    };

    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    boolean found = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertTrue(found);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_edgeCase() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.0001) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.0001) {
          return HIGH;
        } else {
          return UNCONVERGED;
        }
      }
    };

    double x0 = Math.PI / 2;
    double f0 = function.valueAt(x0);
    double x1 = 5 * Math.PI / 2;
    double f1 = function.valueAt(x1);

    boolean found = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertTrue(found);
    assertEquals(Math.PI, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRoot_errorCase() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Function evaluation failed");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.0001) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.0001) {
          return HIGH;
        } else {
          return UNCONVERGED;
        }
      }
    };

    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    boolean found = false;
    try {
      found = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    } catch (FunctionException e) {
      assertEquals("Function evaluation failed", e.getMessage());
    }

    assertFalse(found);
  }

}