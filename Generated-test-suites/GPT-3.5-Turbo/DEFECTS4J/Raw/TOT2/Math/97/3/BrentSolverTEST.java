import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - 2) < 0.001) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(xHigh - 2) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    try {
      boolean rootFound = solver.findRoot(function, checker, 100, 1, -3, 3, 7);
      assertTrue(rootFound);
      assertEquals(2, solver.getRoot(), 0.001);
    } catch (FunctionException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    // Assuming root is NaN before finding the root
    assertTrue(Double.isNaN(solver.getRoot()));

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };

    try {
      solver.findRoot(function, checker, 100, 1, -3, 3, 7);
      assertEquals(2, solver.getRoot(), 0.001);
    } catch (FunctionException e) {
      e.printStackTrace();
    }
  }

}