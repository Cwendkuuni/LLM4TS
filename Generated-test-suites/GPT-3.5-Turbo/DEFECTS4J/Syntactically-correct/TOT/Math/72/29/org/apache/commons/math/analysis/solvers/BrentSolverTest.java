import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot_typicalCase() {
    // typical case
    double lowerBound = 0.0;
    double fLowerBound = -1.0;
    double upperBound = 2.0;
    double fUpperBound = 1.0;

    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.01) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return HIGH;
        } else {
          return UNDECIDED;
        }
      }
    };

    boolean result = solver.findRoot(function, checker, 10, lowerBound, fLowerBound, upperBound, fUpperBound);

    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRoot_edgeCases() {
    // edge case 1: lower and upper bounds are the same
    double equalBound = 0.0;
    double fEqualBound = 0.0;

    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1.0;
      }
    };

    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.01) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return HIGH;
        } else {
          return UNDECIDED;
        }
      }
    };

    boolean result1 = solver.findRoot(function1, checker1, 10, equalBound, fEqualBound, equalBound, fEqualBound);

    assertFalse(result1);
    assertTrue(Double.isNaN(solver.getRoot()));

    // edge case 2: negative abscissa range
    double negativeBound = -5.0;
    double fNegativeBound = 25.0;

    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 25.0;
      }
    };

    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.01) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return HIGH;
        } else {
          return UNDECIDED;
        }
      }
    };

    boolean result2 = solver.findRoot(function2, checker2, 10, negativeBound, fNegativeBound, equalBound, fEqualBound);

    assertTrue(result2);
    assertEquals(-5.0, solver.getRoot(), 0.01);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_errorScenario() throws FunctionException {
    // error scenario: function throws exception
    double lowerBound = 0.0;
    double fLowerBound = -1.0;
    double upperBound = 2.0;
    double fUpperBound = 1.0;

    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function evaluation failed");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.01) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return HIGH;
        } else {
          return UNDECIDED;
        }
      }
    };

    solver.findRoot(function, checker, 10, lowerBound, fLowerBound, upperBound, fUpperBound);
  }

}