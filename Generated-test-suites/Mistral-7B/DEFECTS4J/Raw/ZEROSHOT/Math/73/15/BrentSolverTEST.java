import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {

    // Test case 1: x^2 - 1 = 0
    ComputableFunction function = new RationalFunction("x", 1, 1, new RationalFunction("1", 1));
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(Math.sqrt(1.0), solver.getRoot(), epsilon);

    // Test case 2: sin(x) - x = 0
    function = new RationalFunction("sin(x)", 1, 0, new RationalFunction("x", 1));
    checker = new ConvergenceChecker.Default();

    x0 = 0.5;
    x1 = 1.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(Math.PI / 2.0, solver.getRoot(), epsilon);

    // Test case 3: f(x) = x^3 - 2
    function = new RationalFunction("x", 1, 0, new RationalFunction("x", 1, 1, new RationalFunction("2", 1)));
    checker = new ConvergenceChecker.Default();

    x0 = 1.0;
    x1 = 2.0;

    Assert.assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    Assert.assertNotEquals(Double.NaN, solver.getRoot());
    Assert.assertEquals(-Math.sqrt(2.0), solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testBrentSolverWithException() throws Exception {

    // Test case 4: f(x) = x / 0
    ComputableFunction function = new RationalFunction("x", 1, 0, null);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrentSolverWithNullFunction() throws Exception {

    // Test case 5: null function
    ComputableFunction function = null;
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrentSolverWithNullChecker() throws Exception {

    // Test case 6: null checker
    ComputableFunction function = new RationalFunction("x", 1, 1, new RationalFunction("1", 1));
    ConvergenceChecker checker = null;
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrentSolverWithNegativeMaxIter() throws Exception {

    // Test case 7: negative max iterations
    ComputableFunction function = new RationalFunction("x", 1, 1, new RationalFunction("1", 1));
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, -10, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrentSolverWithNegativeInterval() throws Exception {

    // Test case 8: negative interval bounds
    ComputableFunction function = new RationalFunction("x", 1, 1, new RationalFunction("1", 1));
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = -2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}