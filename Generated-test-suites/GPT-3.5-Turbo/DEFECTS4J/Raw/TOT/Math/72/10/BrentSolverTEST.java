import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRootWithValidParameters() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.001) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };

    BrentSolver solver = new BrentSolver();

    boolean result = solver.findRoot(function, checker, 100, -10, 96, 10, -36);

    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithInvalidParameters() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x < 0) {
          throw new FunctionException("Invalid input");
        }
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.001) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };

    BrentSolver solver = new BrentSolver();

    boolean result = solver.findRoot(function, checker, 100, -10, 96, -4, -4);

    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithNumberOutOfBounds() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.001) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };

    BrentSolver solver = new BrentSolver();

    boolean result = solver.findRoot(function, checker, 100, -Double.MAX_VALUE, Double.MAX_VALUE, -4, -4);

    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), 0.001);
  }

}