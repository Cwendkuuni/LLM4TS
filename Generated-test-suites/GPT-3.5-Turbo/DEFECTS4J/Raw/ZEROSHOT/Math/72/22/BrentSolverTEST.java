import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {
  
  private BrentSolver brentSolver;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
  }

  @Test
  public void testFindRootValidInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        }
        else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        else {
          return NONE;
        }
      }
    };

    boolean rootFound = brentSolver.findRoot(function, checker, 100, -2, -3, 2, 3);

    assertTrue(rootFound);
    assertEquals(1.0, brentSolver.getRoot(), 1e-6);
  }
  
  @Test
  public void testFindRootInvalidInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        }
        else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        else {
          return NONE;
        }
      }
    };

    boolean rootFound = brentSolver.findRoot(function, checker, 100, -2, 3, 2, 3);

    assertFalse(rootFound);
    assertTrue(Double.isNaN(brentSolver.getRoot()));
  }
  
  @Test
  public void testFindRootZeroTolerance() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        }
        else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        else {
          return NONE;
        }
      }
    };

    boolean rootFound = brentSolver.findRoot(function, checker, 100, -2, -3, 2, 3);

    assertTrue(rootFound);
    assertEquals(1.0, brentSolver.getRoot(), 0);
  }
  
}