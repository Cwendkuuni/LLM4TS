import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testBrentSolverConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestConvergenceChecker();
    
    boolean result = solver.findRoot(function, checker, 100, 0, 0, 1, 1);
    
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 1e-15);
  }

  @Test
  public void testFindRootWithNegativeValues() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestConvergenceChecker();
    
    boolean result = solver.findRoot(function, checker, 100, -1, -1, 0, 0);
    
    assertTrue(result);
    assertEquals(-1.0, solver.getRoot(), 1e-15);
  }

  @Test
  public void testFindRootWithLargeValues() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestConvergenceChecker();
    
    boolean result = solver.findRoot(function, checker, 100, 1000000, 1000000, 1000001, 1000001);
    
    assertTrue(result);
    assertEquals(1000000.0, solver.getRoot(), 1e-15);
  }

  @Test
  public void testFindRootWithMaxIterationsReached() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestConvergenceChecker();
    
    boolean result = solver.findRoot(function, checker, 1, 0, 0, 1, 1);
    
    assertFalse(result);
  }

  private static class TestFunction implements ComputableFunction {
    public double valueAt(double x) {
      return 2 * x - 1;
    }
  }

  private static class TestConvergenceChecker implements ConvergenceChecker {
    public int converged(double x0, double f0, double x1, double f1) {
      if (f0 > 0 && f1 < 0) {
        return ConvergenceChecker.LOW;
      } else if (f0 < 0 && f1 > 0) {
        return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.KEEP_GOING;
      }
    }
  }

}