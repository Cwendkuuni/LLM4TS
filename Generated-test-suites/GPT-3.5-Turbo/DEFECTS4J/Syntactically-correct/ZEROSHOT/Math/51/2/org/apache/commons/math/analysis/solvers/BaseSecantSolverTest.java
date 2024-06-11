import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

  @Test
  public void testSolve() {
    // Create dummy implementation of UnivariateRealFunction
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return 3 * x - 5;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    double result = solver.solve(100, f, 0, 10);

    assertEquals(1.6666666666666667, result, 1e-6);
  }

  @Test
  public void testSolveWithMaxEval() {
    // Create dummy implementation of UnivariateRealFunction
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return -2 * x + 7;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    double result = solver.solve(5, f, 0, 10);

    assertEquals(3.5, result, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolution() {
    // Create dummy implementation of UnivariateRealFunction
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    double result = solver.solve(100, f, 0, 10, AllowedSolution.RIGHT_SIDE);

    assertEquals(2.0000000004656613, result, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionAndStartValue() {
    // Create dummy implementation of UnivariateRealFunction
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    double result = solver.solve(100, f, 0, 10, 1, AllowedSolution.RIGHT_SIDE);

    assertEquals(2.0000000004656613, result, 1e-6);
  }
}