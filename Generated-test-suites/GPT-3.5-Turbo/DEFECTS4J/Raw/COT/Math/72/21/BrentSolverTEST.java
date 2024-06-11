
import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() {
    
    // Test case 1: function with linear convergence
    ComputableFunction function1 = new LinearConvergence();
    ConvergenceChecker checker1 = new LinearChecker();
    BrentSolver solver1 = new BrentSolver();
    boolean found1 = solver1.findRoot(function1, checker1, 100, 0, -1, 2, 2);
    assertTrue(found1);
    assertEquals(1.0, solver1.getRoot(), 0);
    
    // Test case 2: function with quadratic convergence
    ComputableFunction function2 = new QuadraticConvergence();
    ConvergenceChecker checker2 = new QuadraticChecker();
    BrentSolver solver2 = new BrentSolver();
    boolean found2 = solver2.findRoot(function2, checker2, 100, 0, -1, 2, 2);
    assertTrue(found2);
    assertEquals(1.0, solver2.getRoot(), 0);
    
    // Test case 3: function with no convergence
    ComputableFunction function3 = new NoConvergence();
    ConvergenceChecker checker3 = new NoConvergenceChecker();
    BrentSolver solver3 = new BrentSolver();
    boolean found3 = solver3.findRoot(function3, checker3, 100, 0, -1, 2, 2);
    assertFalse(found3);
    
  }
  
  @Test
  public void testGetRoot() {
    
    // Test case 1: root is NaN
    BrentSolver solver1 = new BrentSolver();
    double root1 = solver1.getRoot();
    assertTrue(Double.isNaN(root1));
    
    // Test case 2: root is set to a value
    BrentSolver solver2 = new BrentSolver();
    double expectedRoot2 = 1.0;
    solver2.root = expectedRoot2;
    double root2 = solver2.getRoot();
    assertEquals(expectedRoot2, root2, 0);
    
  }
  
}
