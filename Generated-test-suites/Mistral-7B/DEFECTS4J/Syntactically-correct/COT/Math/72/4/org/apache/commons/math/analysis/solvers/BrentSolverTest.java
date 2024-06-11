import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private DummyFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xOld, double fOld, double xNew, double fNew) {
        return ConvergenceChecker.CONTINUE;
      }
    };
    function = new DummyFunction();
  }

  @Test
  public void testFindRoot_simple() {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergedLow() {
    double x0 = 1.0;
    double x1 = 1.5;
    double expectedRoot = 1.5;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergedHigh() {
    double x0 = 1.5;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_converged() {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    double x0 = 1.0;
    double x1 = 2.0;
    function = new DummyFunctionWithException();

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_maxIter() {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xOld, double fOld, double xNew, double fNew) {
        return ConvergenceChecker.CONTINUE;
      }
    };

    function = new DummyFunction();

    solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

}

class DummyFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return x * x - 2.0;
  }
}

class DummyFunctionWithException implements ComputableFunction {
  @Override
  public double valueAt(double x) throws FunctionException {
    throw new FunctionException("Test exception");
  }

}