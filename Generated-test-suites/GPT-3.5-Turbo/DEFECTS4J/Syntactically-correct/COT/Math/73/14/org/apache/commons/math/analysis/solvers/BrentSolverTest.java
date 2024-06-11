import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5; // example function, root is 5
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) return ConvergenceChecker.LOW;
        if (fHigh == 0) return ConvergenceChecker.HIGH;
        return ConvergenceChecker.NONE;
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertEquals(true, rootFound);
    assertEquals(5, solver.getRoot(), 0);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double expectedResult = 5;
    solver.root = expectedResult;
    double result = solver.getRoot();
    assertEquals(expectedResult, result, 0);
  }

}