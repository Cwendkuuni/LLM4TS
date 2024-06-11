import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private SimpleFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 2.0, 2.0);
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        if (Math.abs(x - xPrev) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f) < 1e-8) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
  }

  @Test
  public void testFindRoot_simpleFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 100;

    boolean found = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, found);
    assertEquals(Math.sqrt(2.0), solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_invalidInterval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 1.0;
    int maxIter = 100;

    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_invalidFunction() throws FunctionException {
    BrentSolver invalidSolver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        throw new RuntimeException();
      }
    };

    invalidSolver.findRoot(null, checker, 100, 1.0, 1.0, 2.0, 1.0);
  }

}