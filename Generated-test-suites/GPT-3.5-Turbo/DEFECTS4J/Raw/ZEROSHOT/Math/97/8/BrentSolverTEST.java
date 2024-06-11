import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    double result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return NO_CONVERGENCE;
        }
      }
    }, 100, 1, -3, 4, 8);
    assertEquals(2.0, result, 1e-6);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return NO_CONVERGENCE;
        }
      }
    }, 100, 1, -3, 4, 8);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

}