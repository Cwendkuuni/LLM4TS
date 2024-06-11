import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    double root = solver.getRoot();
    
    // Create a sample function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    
    // Create a sample convergence checker for testing
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-6) {
          return LOW;
        } else if (Math.abs(f2) < 1e-6) {
          return HIGH;
        } else {
          return NOT_CONVERGED;
        }
      }
    };
    
    boolean foundRoot = solver.findRoot(function, checker, 100, 0, -4, 4, 12);
    assertTrue(foundRoot);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

}