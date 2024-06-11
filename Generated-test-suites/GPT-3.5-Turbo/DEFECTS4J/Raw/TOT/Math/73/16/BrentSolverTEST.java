import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private static final double epsilon = Math.pow(2.0, -52);

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
  }

  @Test
  public void testFindRoot_ConvergedToLow() throws FunctionException {
    boolean result = solver.findRoot(function, checker, 100, -10, 96, 10, -24);
    Assert.assertTrue(result);
    Assert.assertEquals(2.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_ConvergedToHigh() throws FunctionException {
    boolean result = solver.findRoot(function, checker, 100, -30, 796, 10, -24);
    Assert.assertTrue(result);
    Assert.assertEquals(-2.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_ConvergedToBisection() throws FunctionException {
    boolean result = solver.findRoot(function, checker, 100, -10, 20, 10, 16);
    Assert.assertTrue(result);
    Assert.assertEquals(2.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_ExceededMaxIterations() throws FunctionException {
    boolean result = solver.findRoot(function, checker, 10, -10, 100, 10, -99);
    Assert.assertFalse(result);
    Assert.assertTrue(Double.isNaN(solver.getRoot()));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() throws FunctionException {
    ComputableFunction exceptionFunction = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Test exception");
      }
    };
    solver.findRoot(exceptionFunction, checker, 100, -10, 20, 10, 16);
  }
}