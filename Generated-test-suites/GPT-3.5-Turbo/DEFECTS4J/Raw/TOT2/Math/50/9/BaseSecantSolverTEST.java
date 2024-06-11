import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {
  
  @Test
  public void testSolve_ExactRoot() {
    TestSecantSolver solver = new TestSecantSolver();
    double result = solver.solve(100, new TestFunction(), 0, 10, AllowedSolution.ANY_SIDE);
    assertEquals(5.0, result, 1e-6);
  }
  
  @Test
  public void testSolve_NoRoot() {
    TestSecantSolver solver = new TestSecantSolver();
    double result = solver.solve(100, new TestFunction(), -5, 5, AllowedSolution.ANY_SIDE);
    assertEquals(Double.NaN, result, 1e-6);
  }
  
  @Test
  public void testSolve_FunctionValueAccuracy() {
    TestSecantSolver solver = new TestSecantSolver();
    double result = solver.solve(100, new TestFunction(), 1, 10, AllowedSolution.ANY_SIDE);
    assertEquals(2.0, result, 1e-6);
  }
  
  @Test
  public void testSolve_InvalidInput() {
    TestSecantSolver solver = new TestSecantSolver();
    double result = solver.solve(100, new TestFunction(), 10, 1, AllowedSolution.ANY_SIDE);
    assertEquals(Double.NaN, result, 1e-6);
  }
  
  @Test
  public void testAllowedSolution_AnySide() {
    TestSecantSolver solver = new TestSecantSolver();
    double result = solver.solve(100, new TestFunction(), 1, 5, AllowedSolution.ANY_SIDE);
    assertEquals(3.0, result, 1e-6);
  }
  
  @Test
  public void testAllowedSolution_LeftSide() {
    TestSecantSolver solver = new TestSecantSolver();
    double result = solver.solve(100, new TestFunction(), 1, 5, AllowedSolution.LEFT_SIDE);
    assertEquals(3.0, result, 1e-6);
  }
  
  @Test
  public void testAllowedSolution_RightSide() {
    TestSecantSolver solver = new TestSecantSolver();
    double result = solver.solve(100, new TestFunction(), 1, 5, AllowedSolution.RIGHT_SIDE);
    assertEquals(3.0, result, 1e-6);
  }
  
  @Test
  public void testAllowedSolution_BelowSide() {
    TestSecantSolver solver = new TestSecantSolver();
    double result = solver.solve(100, new TestFunction(), 1, 5, AllowedSolution.BELOW_SIDE);
    assertEquals(3.0, result, 1e-6);
  }
  
  @Test
  public void testAllowedSolution_AboveSide() {
    TestSecantSolver solver = new TestSecantSolver();
    double result = solver.solve(100, new TestFunction(), 1, 5, AllowedSolution.ABOVE_SIDE);
    assertEquals(3.0, result, 1e-6);
  }
}