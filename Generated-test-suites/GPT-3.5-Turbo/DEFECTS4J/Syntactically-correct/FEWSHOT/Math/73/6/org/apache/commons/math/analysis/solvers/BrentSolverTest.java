import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    
    // Create a simple linear function for testing
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return 2*x + 1;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    
    // Test for a root between -10 and 10
    assertTrue(solver.findRoot(function, checker, 100, -10, function.valueAt(-10), 10, function.valueAt(10)));
    
    // Check if the root is correct
    assertEquals(-0.5, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.root = -0.5;
    
    double result = solver.getRoot();
    assertEquals(-0.5, result, 0.0);
  }
}