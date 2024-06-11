import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_typical() {
    BrentSolver solver = new BrentSolver();
    double x0 = 2;
    double f0 = 10;
    double x1 = 5;
    double f1 = -4;

    boolean rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return 2 * x - 5;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    }, 100, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(2.5, solver.getRoot(), 0.001);
  }
  
  @Test
  public void testFindRoot_edge() {
    BrentSolver solver = new BrentSolver();
    double x0 = Double.MIN_VALUE;
    double f0 = -1000000;
    double x1 = Double.MAX_VALUE;
    double f1 = 1000000;

    boolean rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return Double.MAX_VALUE;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    }, 100, x0, f0, x1, f1);

    assertFalse(rootFound);
  }
  
  @Test
  public void testFindRoot_error() {
    BrentSolver solver = new BrentSolver();
    double x0 = -5;
    double f0 = 10;
    double x1 = 5;
    double f1 = -10;

    try {
      boolean rootFound = solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
          throw new FunctionException("Function cannot be evaluated");
        }
      }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
          if (Math.abs(fLow) < 0.001) {
            return ConvergenceChecker.LOW;
          } else {
            return ConvergenceChecker.NOT_CONVERGED;
          }
        }
      }, 100, x0, f0, x1, f1);
      fail("FunctionException not thrown");
    } catch (FunctionException e) {
      // Exception expected
    }
  }
}