import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    //Test case for findRoot method
    BrentSolver solver = new BrentSolver();
    double x0 = -0.5;
    double f0 = 1.0;
    double x1 = 0.5;
    double f1 = -1.0;
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NO_CONVERGENCE;
        }
      }
    };
    
    try {
      boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
      assertTrue(rootFound);
      assertEquals(1.0, solver.getRoot(), 1e-6);
    } catch (FunctionException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testGetRoot() {
    //Test case for getRoot method
    BrentSolver solver = new BrentSolver();
    double root = 2.0;
    solver.root = root;
    assertEquals(root, solver.getRoot(), 0.0);
  }

  // Add more test cases for other methods as needed

}