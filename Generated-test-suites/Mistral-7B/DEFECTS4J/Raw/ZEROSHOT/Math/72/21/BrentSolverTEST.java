import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.RootsChecker;
import org.spaceroots.mantissa.roots.BrentSolver;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private FunctionFactory functionFactory;
  private RootsChecker rootsChecker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    functionFactory = new FunctionFactory();
    rootsChecker = new RootsChecker();
  }

  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(x -> x * x - 4.0);
    double x0 = 2.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;

    boolean result = solver.findRoot(function, rootsChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
    assertTrue(result);
  }

  @Test
  public void testFindRootComplexFunction() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(Math::sin);
    double x0 = 0.0;
    double x1 = Math.PI / 2.0;
    double expectedRoot = Math.PI / 2.0;

    boolean result = solver.findRoot(function, rootsChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
    assertTrue(result);
  }

  @Test
  public void testFindRootFunctionWithNoRoots() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(x -> Math.pow(x, 2.0) - 1.0);
    double x0 = -1.5;
    double x1 = 1.5;
    boolean expectedResult = false;

    boolean result = solver.findRoot(function, rootsChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedResult, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionWithException() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction((x) -> { throw new ArithmeticException(); });
    double x0 = 0.0;
    double x1 = 1.0;
    solver.findRoot(function, rootsChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}