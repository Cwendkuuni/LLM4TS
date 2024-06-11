import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.RootsFinder;
import org.spaceroots.mantissa.functions.scalar.computable.LinearFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {

    // Testing the root finding algorithm with a simple linear function
    LinearFunction function = new LinearFunction(2.0, -3.0);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    RootsFinder rootsFinder = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;

    Assert.assertTrue(rootsFinder.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertEquals(expectedRoot, rootsFinder.getRoot(), 1e-5);

    // Testing the root finding algorithm with a more complex function
    FunctionUtils function2 = new FunctionUtils("x^3 - 2*x^2 + x + 1");
    checker = new ConvergenceChecker.Default();
    rootsFinder = new BrentSolver();

    x0 = 1.0;
    x1 = 2.0;
    expectedRoot = 1.0;

    Assert.assertTrue(rootsFinder.findRoot(function2, checker, 100, x0, function2.valueAt(x0), x1, function2.valueAt(x1)));
    Assert.assertEquals(expectedRoot, rootsFinder.getRoot(), 1e-5);

    // Testing the root finding algorithm with a function having multiple roots
    FunctionUtils function3 = new FunctionUtils("(x-2)*(x+1)*(x-3)");
    checker = new ConvergenceChecker.Default();
    rootsFinder = new BrentSolver();

    x0 = 0.0;
    x1 = 4.0;
    double[] expectedRoots = {2.0, 1.0, 3.0};

    int count = 0;
    double root;
    while (rootsFinder.findRoot(function3, checker, 100, x0, function3.valueAt(x0), x1, function3.valueAt(x1))) {
      root = rootsFinder.getRoot();
      Assert.assertArrayEquals(expectedRoots[count], root, 1e-5);
      count++;
      if (count < expectedRoots.length) {
        x0 = root;
        x1 = Math.max(x0, x1);
      }
    }

    Assert.assertEquals(expectedRoots.length, count);

  }

}