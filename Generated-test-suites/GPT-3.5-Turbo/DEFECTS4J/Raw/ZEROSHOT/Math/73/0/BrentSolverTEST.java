import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) {
          return ConvergenceChecker.LOW;
        } else if (fHigh == 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    int maxIter = 100;
    double x0 = -3.0;
    double f0 = -5.0;
    double x1 = 3.0;
    double f1 = 7.0;

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double root = 3.0;
    solver.getRoot() = root;
    assertEquals(root, solver.getRoot(), 0.0);
  }
}