import static org.junit.Assert.*;
import org.junit.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) {
          return LOW;
        } else if (fHigh == 0) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 5;
    double f1 = function.valueAt(x1);

    BrentSolver solver = new BrentSolver();
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(foundRoot);
    assertEquals(2.0, solver.getRoot(), 0.0);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double root = solver.getRoot();
    assertTrue(Double.isNaN(root));
  }
}