import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    // Test case 1
    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = -1.0;

    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 2.0;
      }
    };

    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.001) {
          return HIGH;
        } else {
          return CONTINUE;
        }
      }
    };
    
    int maxIter1 = 100;
    boolean result1 = solver.findRoot(function1, checker1, maxIter1, x0, f0, x1, f1);
    double expectedRoot1 = Math.sqrt(2.0);
    double actualRoot1 = solver.getRoot();
    assertTrue(result1);
    assertEquals(expectedRoot1, actualRoot1, 0.001);

    // Test case 2
    double x2 = 0.0;
    double f2 = -1.0;
    double x3 = 1.0;
    double f3 = 1.0;

    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1.0;
      }
    };

    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 0.001) {
          return LOW;
        } else if (Math.abs(fHigh) < 0.001) {
          return HIGH;
        } else {
          return CONTINUE;
        }
      }
    };
    
    int maxIter2 = 100;
    boolean result2 = solver.findRoot(function2, checker2, maxIter2, x2, f2, x3, f3);
    double expectedRoot2 = 1.0;
    double actualRoot2 = solver.getRoot();
    assertTrue(result2);
    assertEquals(expectedRoot2, actualRoot2, 0.001);
  }
}