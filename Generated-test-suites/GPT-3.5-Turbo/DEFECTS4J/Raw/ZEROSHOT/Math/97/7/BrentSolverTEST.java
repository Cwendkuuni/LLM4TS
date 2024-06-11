import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;
  private int maxIter;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new ComputableFunction() {
      public double valueAt(double x) {
        // TODO: Implement test function
        return 0.0;
      }
    };
    checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // TODO: Implement convergence checker
        return 0;
      }
    };
    maxIter = 1000;
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(rootFound);

    double root = solver.getRoot();
    // TODO: Implement assertion for root value
  }

}