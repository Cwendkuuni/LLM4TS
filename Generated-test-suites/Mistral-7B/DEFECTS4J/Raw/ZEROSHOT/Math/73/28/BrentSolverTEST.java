import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Tests for the BrentSolver class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private RationalFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xLow, double fLow) {
        return ConvergenceChecker.ITERATIONS;
      }
    };
    function = new RationalFunction(1, new RationalFunction(2, new RationalFunction(3, new RationalFunction(4))));
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = -2.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    double root = solver.getRoot();
    assertTrue(Math.abs(function.valueAt(root)) < 1e-10);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFailure() throws FunctionException {
    double x0 = 0.0;
    double x1 = 0.0;
    function = new RationalFunction(1, new RationalFunction(0));
    solver.findRoot(function, checker, 100, x0, 0.0, x1, 0.0);
  }

}