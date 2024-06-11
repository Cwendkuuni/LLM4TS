import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        if (Math.abs(f0) < 1e-6) {
          return LOW;
        } else if (Math.abs(f1) < 1e-6) {
          return HIGH;
        } else {
          return NEITHER;
        }
      }
    };

    try {
      assertTrue(solver.findRoot(function, checker, 100, 1, -3, 2, 0));
      assertEquals(2.0, solver.getRoot(), 1e-6);
    } catch (FunctionException e) {
      fail("Exception thrown while finding root");
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test
  public void testGetEpsilon() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Math.pow(2.0, -52), solver.epsilon, 0.0);
  }

}