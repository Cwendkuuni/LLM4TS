import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

  @Test
  public void testSolve_ExactRoot() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x;
      }
    };

    double result = solver.solve(100, null, 1, 2, 1.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);

    Assert.assertEquals(1.0, result, 0.000001);

  }

  @Test
  public void testSolve_IntervalWithinAccuracies() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x * x - 4;
      }
    };

    double result = solver.solve(100, null, 1, 3, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);

    Assert.assertEquals(2.0, result, 0.000001);

  }

  @Test
  public void testSolve_FunctionValueTooSmall() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x * x - 4;
      }
    };

    double result = solver.solve(100, null, 1, 3, 1.9, BaseSecantSolver.AllowedSolution.ANY_SIDE);

    Assert.assertEquals(2.0, result, 0.000001);

  }

  @Test
  public void testSolve_LeftSide() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x * x - 4;
      }
    };

    double result = solver.solve(100, null, 1, 3, 1.9, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

    Assert.assertEquals(2.0, result, 0.000001);

  }

  @Test
  public void testSolve_RightSide() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x * x - 4;
      }
    };

    double result = solver.solve(100, null, 1, 3, 1.9, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

    Assert.assertEquals(1.7320508075688772, result, 0.000001);

  }

  @Test(expected = MathInternalError.class)
  public void testSolve_InvalidMethod() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, null) {
      protected double computeObjectiveValue(double x) {
        return x;
      }
    };

    double result = solver.solve(100, null, 1, 2, 1.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);

    Assert.assertEquals(1.0, result, 0.000001);

  }

}