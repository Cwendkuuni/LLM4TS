
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {

  @Test
  public void testSolve() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

    double expected = 2.0;
    double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(expected, result, 1e-6);
  }

  @Test
  public void testSolve_with_maxEval() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

    double expected = 2.0;
    double result = solver.solve(10, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(expected, result, 1e-6);
  }

  @Test
  public void testSolve_with_min_and_max_equal() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

    double expected = 2.0;
    double result = solver.solve(100, f, 2, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(expected, result, 1e-6);
  }

  @Test(expected = MathInternalError.class)
  public void testSolve_with_invalid_method() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, null);

    solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
  }
}
