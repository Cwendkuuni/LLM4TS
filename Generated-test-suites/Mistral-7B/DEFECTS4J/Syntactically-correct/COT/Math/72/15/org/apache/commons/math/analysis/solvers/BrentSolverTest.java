import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_basic() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };
    DummyFunction function = new DummyFunction();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    Assert.assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };
    DummyFunctionException function = new DummyFunctionException();

    double x0 = 1.0;
    double x1 = 2.0;

    Assert.assertFalse(solver.findRoot(function, checker, 10, x0, Double.NaN, x1, Double.NaN));
  }

  @Test
  public void testFindRoot_converged_low() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };
    DummyFunction function = new DummyFunction();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    Assert.assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_converged_high() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.HIGH;
      }
    };
    DummyFunction function = new DummyFunction();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    Assert.assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_tolerance() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };
    DummyFunction function = new DummyFunction();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    Assert.assertTrue(solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(root, solver.getRoot(), 2 * epsilon);
  }

}

class DummyFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return x * x - 2.0;
  }
}

class DummyFunctionException implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    throw new RuntimeException();
  }
}