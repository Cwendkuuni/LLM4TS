import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double f0 = computeFunctionValue(x0);
    double x1 = 1.0;
    double f1 = computeFunctionValue(x1);
    double maxIter = 100;
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return computeFunctionValue(x);
      }
    };
    BrentSolver.ConvergenceChecker checker = new BrentSolver.ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) throws FunctionException {
        // implementation of convergence checker
      }
    };
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assert rootFound;
    assert Math.abs(solver.getRoot() - expectedRoot) < epsilon;
  }

  private double computeFunctionValue(double x) throws FunctionException {
    // implementation of function computation
  }

}