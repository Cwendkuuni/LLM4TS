import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot_BasicFunction() {
    ComputableFunction function = new FunctionUtils.SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 0.0;
    double root = Math.PI / 2.0;

    boolean result = solver.findRoot(function, checker, 100, x0, FunctionUtils.sine(x0), x1, FunctionUtils.sine(x1));

    assertEquals(root, solver.getRoot(), 0.001);
    assertTrue(result);
  }

  @Test
  public void testFindRoot_FunctionWithTwoRoots() {
    ComputableFunction function = new FunctionUtils.QuadraticFunction(-1.0, 4.0, 4.0);
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = -5.0;
    double x1 = 5.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    // The Brent algorithm should find only one root, but since we don't know which one, we can only check if one root is found
    assertTrue(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithException() {
    ComputableFunction function = new FunctionWithException();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 0.0;

    solver.findRoot(function, checker, 100, x0, FunctionUtils.sine(x0), x1, FunctionUtils.sine(x1));
  }

  @Test
  public void testFindRoot_MaxIterations() {
    ComputableFunction function = new FunctionUtils.SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 0.0;
    double root = Math.PI / 2.0;

    boolean result = solver.findRoot(function, checker, 0, x0, FunctionUtils.sine(x0), x1, FunctionUtils.sine(x1));

    assertFalse(result);
  }

}

class FunctionWithException implements ComputableFunction {
  @Override
  public double valueAt(double x) throws FunctionException {
    if (x > 0) {
      throw new FunctionException("FunctionException occurred!");
    }
    return Math.sin(x);
  }
}