import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {

    // Test case 1: x^2 - 1
    ComputableFunction function = new RationalFunction("x", 1, 1, 1, -1);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.5;
    double root = 1.0;
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x0 + 0.1, function.valueAt(x0 + 0.1));
    Assert.assertEquals(root, solver.getRoot(), 1e-6);

    // Test case 2: x^3 - 1
    function = new RationalFunction("x", 1, 1, 1, -3);
    checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.HIGH;
      }
    };

    solver = new BrentSolver();
    x0 = 1.5;
    root = 1.0;
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x0 + 0.1, function.valueAt(x0 + 0.1));
    Assert.assertEquals(root, solver.getRoot(), 1e-6);

    // Test case 3: sqrt(x)
    function = new RationalFunction("x", 1, 1, 0.5, 0);
    checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    solver = new BrentSolver();
    x0 = 2.0;
    root = 1.41421356;
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x0 - 0.1, function.valueAt(x0 - 0.1));
    Assert.assertEquals(root, solver.getRoot(), 1e-6);

    // Test case 4: log(x)
    function = new RationalFunction("x", 1, 1, 0, Math.log(2));
    checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.HIGH;
      }
    };

    solver = new BrentSolver();
    x0 = 3.0;
    root = 2.0;
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x0 - 0.1, function.valueAt(x0 - 0.1));
    Assert.assertEquals(root, solver.getRoot(), 1e-6);

    // Test case 5: log(x) with negative x
    function = new RationalFunction("x", 1, 1, 0, Math.log(2));
    checker = new ConvergenceChecker() {
      @Override
      public int convergenceTest(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    solver = new BrentSolver();
    x0 = -2.0;
    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x0 + 0.1, function.valueAt(x0 + 0.1));
      Assert.fail();
    } catch (FunctionException e) {
      // Expected
    }

  }

}