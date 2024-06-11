import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    
    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 2.0;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return IN_PROGRESS;
        }
      }
    };
    int maxIter1 = 100;
    double x0_1 = 1.0;
    double f0_1 = -1.0;
    double x1_1 = 3.0;
    double f1_1 = 1.0;
    boolean result1 = solver.findRoot(function1, checker1, maxIter1, x0_1, f0_1, x1_1, f1_1);
    double root1 = solver.getRoot();
    assertEquals(true, result1);
    assertEquals(2.0, root1, 1e-6);
    
    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else {
          return IN_PROGRESS;
        }
      }
    };
    int maxIter2 = 100;
    double x0_2 = 0.0;
    double f0_2 = 0.0;
    double x1_2 = Math.PI;
    double f1_2 = 0.0;
    boolean result2 = solver.findRoot(function2, checker2, maxIter2, x0_2, f0_2, x1_2, f1_2);
    double root2 = solver.getRoot();
    assertEquals(true, result2);
    assertEquals(0.0, root2, 1e-6);
  }

}