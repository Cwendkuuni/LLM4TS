import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        double diff = Math.abs(x1 - x2);
        return (diff < 1e-9) ? ConvergenceChecker.LOW : (Math.abs(f1) < 1e-9) ? ConvergenceChecker.HIGH : ConvergenceChecker.ITERATION;
      }
    };
    function = FunctionFactory.sine();
  }

  @Test
  public void testSolveEmptyInterval() {
    try {
      double x0 = Double.NaN;
      double x1 = Double.NaN;
      solver.findRoot(function, checker, 10, x0, Double.NaN, x1, Double.NaN);
      Assert.fail();
    } catch (FunctionException e) {
      // Expected
    }
  }

  @Test
  public void testSolveZeroFunction() {
    try {
      double x0 = 0.0;
      double x1 = 0.0;
      boolean result = solver.findRoot(function, checker, 10, x0, 0.0, x1, 0.0);
      Assert.assertTrue(result);
      Assert.assertEquals(0.0, solver.getRoot(), 1e-9);
    } catch (FunctionException e) {
      Assert.fail();
    }
  }

  @Test
  public void testSolveFunctionWithOneRoot() {
    try {
      double x0 = -3.0;
      double x1 = 3.0;
      boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      Assert.assertTrue(result);
      Assert.assertNotEquals(Double.NaN, solver.getRoot());
    } catch (FunctionException e) {
      Assert.fail();
    }
  }

  @Test
  public void testSolveFunctionWithTwoRoots() {
    try {
      double x0 = -3.0;
      double x1 = 3.0;
      RationalFunction function2 = new RationalFunction("1", "x^2 - 4");
      boolean result = solver.findRoot(function2, checker, 10, x0, function2.valueAt(x0), x1, function2.valueAt(x1));
      Assert.assertTrue(result);
      Assert.assertNotEquals(Double.NaN, solver.getRoot());
      double root1 = solver.getRoot();
      solver.reset();
      boolean result2 = solver.findRoot(function2, checker, 10, x0, function2.valueAt(x0), x1, function2.valueAt(x1));
      Assert.assertTrue(result2);
      Assert.assertNotEquals(root1, solver.getRoot());
    } catch (FunctionException e) {
      Assert.fail();
    }
  }

  @Test
  public void testSolveFunctionWithNoRoots() {
    try {
      double x0 = -3.0;
      double x1 = 3.0;
      RationalFunction function3 = new RationalFunction("1", "x^2 + 4");
      boolean result = solver.findRoot(function3, checker, 10, x0, function3.valueAt(x0), x1, function3.valueAt(x1));
      Assert.assertFalse(result);
    } catch (FunctionException e) {
      // Expected
    }
  }

  @Test
  public void testSolveFunctionWithNegativeFunctionValues() {
    try {
      double x0 = -3.0;
      double x1 = 3.0;
      RationalFunction function4 = new RationalFunction("1", "x^2 - 5");
      boolean result = solver.findRoot(function4, checker, 10, x0, function4.valueAt(x0), x1, function4.valueAt(x1));
      Assert.assertFalse(result);
    } catch (FunctionException e) {
      // Expected
    }
  }
}