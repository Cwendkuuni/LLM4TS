import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Creating instance of BrentSolver class
    BrentSolver solver = new BrentSolver();
    
    // Define dummy function and convergence checker
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4; // Square root of 4 is 2
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) { // Customize convergence criteria
          return ConvergenceChecker.LOW;
        } else if (fLow * fHigh < 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    // Testing scenario with known root
    assertTrue(solver.findRoot(function, checker, 100, 1.0, -3.0, 3.0, 7.0));
    assertEquals(2.0, solver.getRoot(), 1e-6); // Root should be around 2.0
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Testing default root value
    assertEquals(Double.NaN, solver.getRoot(), 1e-6);
  }

}