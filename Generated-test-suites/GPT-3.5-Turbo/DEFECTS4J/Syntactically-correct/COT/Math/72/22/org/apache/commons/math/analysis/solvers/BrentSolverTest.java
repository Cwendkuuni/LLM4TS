import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() {
    // Create a test instance
    BrentSolver solver = new BrentSolver();
    
    // Define the function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    
    // Define the convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double y1, double x2, double y2) {
        if (Math.abs(x2 - x1) < 1.0e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NO_CONVERGENCE;
      }
    };
    
    // Define the initial interval
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 3;
    double f1 = function.valueAt(x1);
    
    // Find the root
    try {
      boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
      assertTrue(rootFound);
      assertEquals(Math.sqrt(2), solver.getRoot(), 1.0e-6);
    } catch (FunctionException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testGetRoot() {
    // Create a test instance
    BrentSolver solver = new BrentSolver();
    
    // Set root value
    double root = 5.0;
    solver.root = root;
    
    // Check getRoot method
    assertEquals(root, solver.getRoot(), 0.0);
  }
  
}