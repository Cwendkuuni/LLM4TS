import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();

    // Create a dummy function
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 4*x + 3;
      }
    };

    // Create a dummy convergence checker
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        return 0;
      }
    };

    // Call findRoot method and check the result
    boolean result = solver.findRoot(function, checker, 100, 0, -1, 2, 3);
    assertTrue(result);
    assertEquals(1.0, solver.getRoot(), 0.00001);
  }
  
  @Test
  public void testGetRoot() {
    // Create instance of BrentSolver
    BrentSolver solver = new BrentSolver();
    
    // Set the root value
    solver.root = 2.5;
    
    // Call getRoot method and check the result
    assertEquals(2.5, solver.getRoot(), 0.00001);
  }

}