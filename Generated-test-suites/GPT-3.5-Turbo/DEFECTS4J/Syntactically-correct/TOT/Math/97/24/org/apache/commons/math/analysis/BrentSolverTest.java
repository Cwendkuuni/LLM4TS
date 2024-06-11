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
  public void testFindRoot_typicalCase() {
    double x0 = -5.0;
    double f0 = Math.pow(x0, 2) - 4;
    double x1 = 5.0;
    double f1 = Math.pow(x1, 2) - 4;
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 100;
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_edgeCase() {
    double x0 = -100.0;
    double f0 = Math.pow(x0, 3) - 10;
    double x1 = 100.0;
    double f1 = Math.pow(x1, 3) - 10;
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 3) - 10;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 1000;
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(2.1544348, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_errorScenario() {
    double x0 = -5.0;
    double f0 = Math.pow(x0, 2) - 4;
    double x1 = 5.0;
    double f1 = Math.pow(x1, 2) - 4;
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Error evaluating function");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 100;
    try {
      boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      fail("Exception should be thrown");
    } catch (FunctionException e) {
      // expected behavior
    }
  }

}