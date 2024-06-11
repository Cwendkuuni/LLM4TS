import org.junit.Test;

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
        if (Math.abs(x2 - x1) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testGetRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.001);

    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(x2 - x1) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.001);
  }
}