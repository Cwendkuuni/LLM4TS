import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.spaceroots.mantissa.functions.test.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleRoot_WithinBoundaries() throws Exception {
    ComputableFunction function = new DummyFunction(0.5);
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0);

    assertTrue(result);
    assertTrue(Math.abs(solver.getRoot() - 1.5) < 0.0001);
  }

  @Test
  public void testFindRoot_SimpleRoot_OutsideBoundaries() throws Exception {
    ComputableFunction function = new DummyFunction(0.5);
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, 5.0, 4.0, 3.0, 2.0);

    assertFalse(result);
  }

  @Test
  public void testFindRoot_SimpleRoot_ConvergenceCheck() throws Exception {
    ComputableFunction function = new DummyFunction(0.5);
    ConvergenceChecker checker = new ConvergenceChecker.ConvergenceChecker();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -2.0, 2.0, 3.0, 4.0);

    assertTrue(result);
    assertTrue(Math.abs(solver.getRoot() - 0.0) < 0.0001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithException() throws Exception {
    ComputableFunction function = new DummyFunctionWithException();
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, 0.0, 1.0, 2.0, 3.0);
  }

  @Test
  public void testFindRoot_FunctionWithMultipleRoots() throws Exception {
    ComputableFunction function = new DummyFunctionWithMultipleRoots();
    ConvergenceChecker checker = new ConvergenceChecker.SimpleChecker();

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, 100, -3.0, 3.0, 2.0, 4.0);

    assertTrue(result);
    assertTrue(Math.abs(solver.getRoot() - 1.0) < 0.0001);
    assertTrue(Math.abs(solver.getRoot() - 2.0) < 0.0001);
  }

}

class DummyFunction implements ComputableFunction {
  private double a;

  public DummyFunction(double a) {
    this.a = a;
  }

  @Override
  public double valueAt(double x) {
    return a * x * x;
  }
}

class DummyFunctionWithException implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    if (x > 1.0) {
      throw new FunctionException("Function exception.");
    }
    return Math.sqrt(x);
  }
}

class DummyFunctionWithMultipleRoots implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return x * x * x - 3.0 * x * x + 2.0;
  }
}