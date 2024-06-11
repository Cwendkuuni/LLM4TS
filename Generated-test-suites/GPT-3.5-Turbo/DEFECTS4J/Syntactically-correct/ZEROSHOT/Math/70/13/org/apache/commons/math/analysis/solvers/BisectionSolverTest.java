import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

  @Test
  public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
    // Test case 1
    UnivariateRealFunction function1 = new UnivariateRealFunction() {
      public double value(double x) {
        return (x * x) - 4;
      }
    };
    BisectionSolver solver1 = new BisectionSolver(function1);
    double result1 = solver1.solve(0, 2);
    // Compare the result with the expected value
    assertEquals(2.000, result1, 0.001);

    // Test case 2
    UnivariateRealFunction function2 = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.cos(x) - x;
      }
    };
    BisectionSolver solver2 = new BisectionSolver(function2);
    double result2 = solver2.solve(0, 1);
    // Compare the result with the expected value
    assertEquals(0.739, result2, 0.001);
  }
}