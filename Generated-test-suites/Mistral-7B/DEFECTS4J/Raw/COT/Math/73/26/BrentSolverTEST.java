import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionAdapter;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_Basic() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionAdapter(Math::pow, 2.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(xHigh - xLow) < 0.001) ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    double interval[] = {-2.0, 2.0};
    double root = Math.sqrt(2.0);
    Assert.assertTrue(solver.findRoot(function, checker, 100, interval[0], interval[1], 0.0, 0.0));
    Assert.assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionAdapter(Math::sqrt, -1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    double interval[] = {0.0, 1.0};
    Assert.assertFalse(solver.findRoot(function, checker, 100, interval[0], interval[1], 0.0, 0.0));
  }

  @Test
  public void testFindRoot_EdgeCases() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionAdapter(Math::sin, 0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    double interval1[] = {0.0, 1.0};
    Assert.assertFalse(solver.findRoot(function, checker, 100, interval1[0], interval1[1], 0.0, 0.0));

    double interval2[] = {Math.PI, Math.PI};
    Assert.assertFalse(solver.findRoot(function, checker, 100, interval2[0], interval2[1], 0.0, 0.0));

    double interval3[] = {-1000.0, 1000.0};
    Assert.assertTrue(solver.findRoot(function, checker, 100, interval3[0], interval3[1], 0.0, 0.0));
  }

  @Test
  public void testFindRoot_MultipleRoots() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionAdapter(x -> Math.pow(x, 3.0) - 2.0, 0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(xHigh - xLow) < 0.001) ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    double interval[] = {-1.5, 1.5};
    Assert.assertTrue(solver.findRoot(function, checker, 100, interval[0], interval[1], 0.0, 0.0));
    Assert.assertEquals(-1.0, solver.getRoot(), 0.001);

    Assert.assertTrue(solver.findRoot(function, checker, 100, interval[0], interval[1], 0.0, 0.0));
    Assert.assertEquals(1.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_ZeroFunction() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionAdapter(x -> 0.0, 0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    double interval[] = {-1.0, 1.0};
    Assert.assertFalse(solver.findRoot(function, checker, 100, interval[0], interval[1], 0.0, 0.0));
  }

  @Test
  public void testFindRoot_FunctionWithInfiniteRoot() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionAdapter(x -> 1.0 / x, 1.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    double interval[] = {1.0, 2.0};
    try {
      solver.findRoot(function, checker, 100, interval[0], interval[1], 0.0, 0.0);
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRoot_FunctionWithNegativeRoot() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionAdapter(x -> Math.pow(x, 2.0) + 1.0, 0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    double interval[] = {-1.0, 1.0};
    Assert.assertTrue(solver.findRoot(function, checker, 100, interval[0], interval[1], 0.0, 0.0));
    Assert.assertEquals(-1.0, solver.getRoot(), 0.001);
  }

}