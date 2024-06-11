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
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        return (Math.abs(f1) < 1e-5) ? LOW : UNCONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(2, solver.getRoot(), 1e-5);
  }

  // Additional test case for different function and checker

  @Test
  public void testFindRoot2() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        return (Math.abs(f2) < 1e-5) ? HIGH : UNCONVERGED;
      }
    };
    int maxIter = 200;
    double x0 = -Math.PI;
    double f0 = function.valueAt(x0);
    double x1 = Math.PI;
    double f1 = function.valueAt(x1);
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(Math.PI, solver.getRoot(), 1e-5);
  }

}