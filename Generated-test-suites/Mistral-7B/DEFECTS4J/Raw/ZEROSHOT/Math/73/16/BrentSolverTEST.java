import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver brentSolver;
  private ConvergenceChecker convergenceChecker;
  private RationalFunction function;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
    convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.LOW;
      }
    };
    function = new RationalFunction("x^3 - 2.0");
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = brentSolver.findRoot(function, convergenceChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? brentSolver.getRoot() : Double.NaN;
    assertEquals(-1.0, root, 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    ConvergenceChecker exceptionChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        throw new RuntimeException("Convergence checker exception");
      }
    };
    brentSolver.findRoot(function, exceptionChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testGetRoot() {
    double root = brentSolver.findRoot(function, convergenceChecker, 100, 1.0, function.valueAt(1.0), 3.0, function.valueAt(3.0)) ? brentSolver.getRoot() : Double.NaN;
    assertEquals(-1.0, root, 0.001);
  }

}