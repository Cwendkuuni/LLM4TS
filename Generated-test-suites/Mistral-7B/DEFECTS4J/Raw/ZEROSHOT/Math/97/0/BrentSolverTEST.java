import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new FunctionUnderTest();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < 0.001) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 0.001);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    ComputableFunction functionWithException = new FunctionWithException();

    solver.findRoot(functionWithException, checker, 100, x0, functionWithException.valueAt(x0), x1, functionWithException.valueAt(x1));
  }

  @Test
  public void testFindRootWithDivergentFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    ComputableFunction functionWithDivergentRoot = new FunctionWithDivergentRoot();

    boolean result = solver.findRoot(functionWithDivergentRoot, checker, 100, x0, functionWithDivergentRoot.valueAt(x0), x1, functionWithDivergentRoot.valueAt(x1));
    assertEquals(false, result);
  }

}

class FunctionUnderTest implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return x * x - 4;
  }
}

class FunctionWithException implements ComputableFunction {
  @Override
  public double valueAt(double x) throws FunctionException {
    if (x > 2) {
      throw new FunctionException("Function exception");
    }
    return x * x - 4;
  }
}

class FunctionWithDivergentRoot implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.log(Math.abs(x - 3));
  }
}