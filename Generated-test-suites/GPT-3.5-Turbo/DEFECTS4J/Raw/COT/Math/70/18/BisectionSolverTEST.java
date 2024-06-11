import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {
  
  @Test
  public void testSolve() {
    BisectionSolver solver = new BisectionSolver();
    // Basic test case
    double result = solver.solve(-1.0, 1.0);
    assertEquals(0.0, result, 1E-6);
    // Test case where the function has no root in the given interval
    try {
      result = solver.solve(1.0, 2.0);
      fail("Expected FunctionEvaluationException");
    } catch (FunctionEvaluationException e) {
      // expected exception
    }
    // Test case where the function has multiple roots in the interval
    result = solver.solve(-2.0, 2.0);
    assertEquals(0.0, result, 1E-6);
    // Test case where max iterations are exceeded
    try {
      result = solver.solve(-1.0, 1.0);
      fail("Expected MaxIterationsExceededException");
    } catch (MaxIterationsExceededException e) {
      // expected exception
    }
  }
  
  @Test
  public void testSolveWithInitial() {
    BisectionSolver solver = new BisectionSolver();
    // Basic test case
    double result = solver.solve(-1.0, 1.0, 0.0);
    assertEquals(0.0, result, 1E-6);
    // Test case where the function has no root in the given interval
    try {
      result = solver.solve(1.0, 2.0, 0.0);
      fail("Expected FunctionEvaluationException");
    } catch (FunctionEvaluationException e) {
      // expected exception
    }
    // Test case where the function has multiple roots in the interval
    result = solver.solve(-2.0, 2.0, 0.0);
    assertEquals(0.0, result, 1E-6);
    // Test case where max iterations are exceeded
    try {
      result = solver.solve(-1.0, 1.0, 0.0);
      fail("Expected MaxIterationsExceededException");
    } catch (MaxIterationsExceededException e) {
      // expected exception
    }
  }
}