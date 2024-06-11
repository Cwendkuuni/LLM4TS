import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.Functions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver.
 */
public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test with a simple function, x^2 - 4
    ComputableFunction function = new Functions.Power(2.0).negate().add(new Functions.Constant(4.0));
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    try {
      assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
      assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      fail("FunctionException occurred: " + e.getMessage());
    }
  }

  @Test
  public void testFindRoot_FunctionWithRootAtZero() {
    // Test with a function with root at zero, x^2 + x - 1
    ComputableFunction function = new Functions.Power(2.0).add(new Functions.Variable("x")).subtract(new Functions.Constant(1.0));
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;
    double root = 0.0;

    try {
      assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
      assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      fail("FunctionException occurred: " + e.getMessage());
    }
  }

  @Test
  public void testFindRoot_FunctionWithRootOutsideInterval() {
    // Test with a function where the root is outside the interval
    ComputableFunction function = new Functions.Power(2.0).negate();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;
    double root = 3.0;

    try {
      assertFalse(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
      // root should not be set
      assertNaN(solver.getRoot());
    } catch (FunctionException e) {
      fail("FunctionException occurred: " + e.getMessage());
    }
  }

  @Test
  public void testFindRoot_FunctionWithRootAtEndOfInterval() {
    // Test with a function where the root is at an end of the interval
    ComputableFunction function = new Functions.Power(2.0).subtract(new Functions.Constant(4.0.sqrt()));
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 2.0;
    double x1 = 2.0;
    double root = 2.0;

    try {
      assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
      assertEquals(root, solver.getRoot(), epsilon);
    } catch (FunctionException e) {
      fail("FunctionException occurred: " + e.getMessage());
    }
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithSingularity() {
    // Test with a function with a singularity
    ComputableFunction function = new Functions.Power(2.0).negate().divide(new Functions.Variable("x"));
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Exception expected");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithInfiniteValue() {
    // Test with a function with infinite value
    ComputableFunction function = new Functions.Variable("x").pow(2).divide(new Functions.Variable("x").subtract(1));
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Exception expected");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRoot_FunctionWithMultipleRoots() {
    // Test with a function having multiple roots
    ComputableFunction function = new Functions.Sin(new Functions.Variable("x"));
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = Math.PI;

    try {
      assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
      double root = solver.getRoot();
      assertTrue(Math.abs(Math.sin(root) < epsilon));
    } catch (FunctionException e) {
      fail("FunctionException occurred: " + e.getMessage());
    }
  }

  private double epsilon = Math.pow(2.0, -52);

  private double assertNaN(Double value) {
    assertEquals(Double.NaN, value);
    return value;
  }

}