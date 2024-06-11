import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private static final double EPSILON = 1e-8;
  private BrentSolver solver;
  private ConvergenceChecker checker;
  private RationalFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < EPSILON && Math.abs(f2) < EPSILON) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    function = new RationalFunction("2.0 * x^2 - 4.0 * x + 2.0");
  }

  @Test
  public void testFindRoot() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), EPSILON);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws Exception {
    RationalFunction function = new RationalFunction("0.0 * x");
    solver.findRoot(function, checker, 100, -1.0, Double.NaN, 1.0, Double.NaN);
  }

  @Test
  public void testFindRootFailed() throws Exception {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = Math.sqrt(2.0);

    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRootConvergenceChecker() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;
    ConvergenceChecker convergedChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < EPSILON && Math.abs(f2) < EPSILON) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }

      @Override
      public int converged(double x) {
        double f = function.valueAt(x);
        if (Math.abs(f) < EPSILON) {
          return ConvergenceChecker.ROOT;
        }
        return ConvergenceChecker.NONE;
      }
    };

    assertTrue(solver.findRoot(function, convergedChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), EPSILON);
  }

  @Test
  public void testFindRootMaxIter() throws Exception {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    assertFalse(solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test
  public void testFindRootOutOfRange() throws Exception {
    double x0 = Double.MIN_VALUE;
    double x1 = Double.MAX_VALUE;
    double root = 0.0;

    assertFalse(solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN));
  }
}