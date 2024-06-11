import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        return NONE;
      }
    };
    BrentSolver solver1 = new BrentSolver();
    boolean rootFound1 = solver1.findRoot(function1, checker1, 100, 0, -4, 5, 21);
    assertTrue(rootFound1);
    assertEquals(2, solver1.getRoot(), 1e-6);

    // test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        return NONE;
      }
    };
    BrentSolver solver2 = new BrentSolver();
    boolean rootFound2 = solver2.findRoot(function2, checker2, 100, 0, 0, 1, -0.1585290151921035);
    assertTrue(rootFound2);
    assertEquals(0, solver2.getRoot(), 1e-6);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.root = 3.141592653589793;
    assertEquals(3.141592653589793, solver.getRoot(), 0);
  }

}