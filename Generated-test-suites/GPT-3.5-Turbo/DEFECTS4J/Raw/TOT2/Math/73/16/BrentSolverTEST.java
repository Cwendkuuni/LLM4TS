import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test with a simple linear function
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(x -> x - 5, (x1, f1, x2, f2) -> Math.abs(f1) < 1e-6 ? ConvergenceChecker.LOW : ConvergenceChecker.KEEP_ITERATING, 100, 0.0, -5.0, 10.0, -5.0);
    assertTrue(rootFound);
    assertEquals(5.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testGetRoot() {
    // Test when no root has been found yet
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRootEdgeCases() {
    // Test when the initial values are very close to the root
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(x -> x - 100, (x1, f1, x2, f2) -> Math.abs(f1) < 1e-6 ? ConvergenceChecker.LOW : ConvergenceChecker.KEEP_ITERATING, 100, 99.9, -0.1, 100.1, 0.1);
    assertTrue(rootFound);
    assertEquals(100.0, solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() {
    // Test when the function throws an exception
    BrentSolver solver = new BrentSolver();
    solver.findRoot(x -> {
      throw new FunctionException("Test Exception");
    }, (x1, f1, x2, f2) -> ConvergenceChecker.LOW, 100, -1.0, -1.0, 1.0, 1.0);
  }

}