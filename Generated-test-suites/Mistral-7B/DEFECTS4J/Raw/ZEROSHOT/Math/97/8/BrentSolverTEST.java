import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionsTest;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private FunctionsTest function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new FunctionsTest();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
  }

  @Test
  public void testFindRootWithSimpleFunction() throws FunctionException {
    double a = 1.0;
    double b = 3.0;
    double expectedRoot = 2.0;
    double[] roots = {expectedRoot};

    solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b));

    assertTrue(solver.getRoot() != Double.NaN);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootWithFunctionWithTwoRoots() throws FunctionException {
    double a = -3.0;
    double b = 3.0;
    double[] expectedRoots = {-1.0, 1.0};

    solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b));

    assertNotNull(solver.getRoot());
    for (double root : expectedRoots) {
      assertTrue(new java.util.ArrayList<>(java.util.Arrays.asList(solver.getRoot())).contains(root));
    }
  }

  @Test
  public void testFindRootWithFunctionThatHasNoRoots() throws FunctionException {
    double a = -5.0;
    double b = 5.0;

    solver.findRoot(function, checker, 100, a, function.valueAt(a), b, function.valueAt(b));

    assertFalse(solver.getRoot() != Double.NaN);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionThatThrowsException() throws FunctionException {
    double a = 0.0;
    double b = 2.0;

    ConvergenceChecker checkerWithException = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        throw new FunctionException("Test exception");
      }
    };

    solver.findRoot(function, checkerWithException, 100, a, function.valueAt(a), b, function.valueAt(b));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver.findRoot(null, checker, 100, 0.0, 0.0, 0.0, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullChecker() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionsTest();

    solver.findRoot(function, null, 100, 0.0, 0.0, 0.0, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithMaxIterationLessThanOne() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new FunctionsTest();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
  public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver.findRoot(function, checker, -1, 0.0, 0.0, 0.0, 0.0);
  }
}