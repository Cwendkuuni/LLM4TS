import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    // Typical use case
    boolean rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return f0 * f1 > 0 ? ConvergenceChecker.NONE : f0 > f1 ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    }, 100, 1, -3, 3, 7);
    
    assertTrue(rootFound);
    
    // Edge case where function has multiple roots in interval
    rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return 0;
      }
    }, new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return ConvergenceChecker.LOW;
      }
    }, 100, -1, -1, 1, 1);
    
    assertTrue(rootFound);
    
    // Error scenario where function never converges
    rootFound = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x;
      }
    }, new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return ConvergenceChecker.NONE;
      }
    }, 100, -1, -1, 1, 1);
    
    assertFalse(rootFound);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return f0 * f1 > 0 ? ConvergenceChecker.NONE : f0 > f1 ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    }, 100, 1, -3, 3, 7);
    
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

}