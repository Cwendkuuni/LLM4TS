import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;
        }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            return (Math.abs(fLow) < 1e-6) ? ConvergenceChecker.LOW :
                (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH :
                ConvergenceChecker.NONE;
        }
    };
    int maxIter1 = 1000;
    double x01 = -5;
    double f01 = function1.valueAt(x01);
    double x11 = 5;
    double f11 = function1.valueAt(x11);
    assertTrue(solver.findRoot(function1, checker1, maxIter1, x01, f01, x11, f11));
    assertEquals(2.0, solver.getRoot(), 1e-6);

    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
        public double valueAt(double x) {
            return Math.sin(x) - 0.5;
        }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            return (Math.abs(fLow) < 1e-6) ? ConvergenceChecker.LOW :
                (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH :
                ConvergenceChecker.NONE;
        }
    };
    int maxIter2 = 1000;
    double x02 = 0;
    double f02 = function2.valueAt(x02);
    double x12 = 2;
    double f12 = function2.valueAt(x12);
    assertTrue(solver.findRoot(function2, checker2, maxIter2, x02, f02, x12, f12));
    assertEquals(0.5235987755982988, solver.getRoot(), 1e-6);

  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double root = 2.5;
    solver.setRoot(root);
    assertEquals(root, solver.getRoot(), 1e-15);
  }

}