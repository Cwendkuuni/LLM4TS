import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleCase() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceStatus(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = -1.0;
    brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertEquals(root, brentSolver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Function Error");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceStatus(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_boundaryCases() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceStatus(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver brentSolver = new BrentSolver();

    // test with x0 = x1
    double x0 = 0.0;
    double x1 = 0.0;
    brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertEquals(x0, brentSolver.getRoot(), epsilon);

    // test with function value identical at x0 and x1
    x0 = 0.5;
    x1 = 0.5;
    brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertEquals(x0, brentSolver.getRoot(), epsilon);

    // test with x0 = min and x1 = max of the interval
    x0 = Double.MIN_VALUE;
    x1 = Double.MAX_VALUE;
    brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.fail(); // the method should not find a root in this case
  }

  @Test
  public void testFindRoot_convergenceCheck() {
    ComputableFunction function = new ComputableFunction() {
      @Override
  // A function that converges slowly to a root
      public double valueAt(double x) {
        return Math.pow(Math.E, -x) - Math.log(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceStatus(double x1, double f1, double x2, double f2) {
        // A checker that considers a root is found when |f1| < 1e-6
        return Math.abs(f1) < 1e-6 ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver brentSolver = new BrentSolver();
    double x0 = 0.1;
    double x1 = 1.0;
    brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertEquals(brentSolver.getRoot(), Math.E, epsilon);
  }

}