import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ConstantFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver brentSolver;
  private ConvergenceChecker convergenceChecker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
    convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < Math.abs(f2)) {
          return LOW;
        } else if (Math.abs(f2) < Double.MIN_VALUE) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    function = new ConstantFunction(1.0);
  }

  @Test
  public void testFindRootWithSimpleFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;
    boolean expectedResult = true;

    double root = brentSolver.findRoot(function, convergenceChecker, maxIter, x0, 1.0, x1, 0.0);

    assertEquals(expectedResult, root);
    assertEquals(1.5, brentSolver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionThrowingException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;
    ComputableFunction functionWithException = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x == 1.5) {
          throw new FunctionException("Test exception");
        }
        return Math.sin(x);
      }
    };

    brentSolver.findRoot(functionWithException, convergenceChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullFunction() {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;

    brentSolver.findRoot(null, convergenceChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullConvergenceChecker() {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;

    brentSolver.findRoot(function, null, maxIter, x0, Math.sin(x0), x1, Math.sin(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithNullChecker() {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;
    ConvergenceChecker nullChecker = null;

    brentSolver.findRoot(function, nullChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithInvalidMaxIter() {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = -1;

    brentSolver.findRoot(function, convergenceChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithInvalidX0() {
    double x0 = -1.0;
    double x1 = 0.0;
    int maxIter = 10;

    brentSolver.findRoot(function, convergenceChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRootWithInvalidX1() {
    double x0 = 0.0;
    double x1 = -1.0;
    int maxIter = 10;

    brentSolver.findRoot(function, convergenceChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1));
  }

  @Test
  public void testFindRootWithZeroWidthInterval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 1.0;
    int maxIter = 10;
    boolean expectedResult = false;

    boolean result = brentSolver.findRoot(function, convergenceChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1));

    assertEquals(expectedResult, result);
  }

  @Test
  public void testFindRootWithFunctionWithRootInsideInterval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;
    boolean expectedResult = true;

    double root = brentSolver.findRoot(function, convergenceChecker, maxIter, x0, 0.0, x1, 0.0).getRoot();

    assertEquals(expectedResult, root > 0);
    assertEquals(Math.sqrt(2.0), root, 0.01);
  }

  @Test
  public void testFindRootWithFunctionWithRootOutsideInterval() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 10;
    boolean expectedResult = true;

    double root = brentSolver.findRoot(function, convergenceChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1)).getRoot();

    assertEquals(expectedResult, root > 0);
    assertEquals(1.2, root, 0.01);
  }

  @Test
  public void testFindRootWithFunctionCrossingZeroInsideInterval() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    int maxIter = 10;
    boolean expectedResult = true;

    double root = brentSolver.findRoot(function, convergenceChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1)).getRoot();

    assertEquals(expectedResult, root > 0);
    assertEquals(0.0, root, 0.01);
  }

  @Test
  public void testFindRootWithFunctionCrossingZeroOutsideInterval() throws FunctionException {
    double x0 = -1.0;
    double x1 = 0.0;
    int maxIter = 10;
    boolean expectedResult = false;

    boolean result = brentSolver.findRoot(function, convergenceChecker, maxIter, x0, Math.sin(x0), x1, Math.sin(x1));

    assertEquals(expectedResult, result);
  }

}