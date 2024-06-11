import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
  
  @Test
  public void testSolve_ExactRoot() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x;
      }
    };
    double result = solver.solve(100, null, -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(0, result, 1e-6);
  }
  
  @Test
  public void testSolve_UnderApproximation() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x + 0.5;
      }
    };
    double result = solver.solve(100, null, -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(-0.5, result, 1e-6);
  }
  
  @Test
  public void testSolve_OverApproximation() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x - 0.5;
      }
    };
    double result = solver.solve(100, null, -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(0.5, result, 1e-6);
  }
  
  @Test
  public void testSolve_BelowSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x - 0.5;
      }
    };
    double result = solver.solve(100, null, -1, 1, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    assertEquals(0.5, result, 1e-6);
  }
  
  @Test
  public void testSolve_AboveSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x + 0.5;
      }
    };
    double result = solver.solve(100, null, -1, 1, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    assertEquals(-0.5, result, 1e-6);
  }
  
  @Test
  public void testSolve_LeftSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x - 0.5;
      }
    };
    double result = solver.solve(100, null, -1, 1, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    assertEquals(-0.5, result, 1e-6);
  }
  
  @Test
  public void testSolve_RightSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return x + 0.5;
      }
    };
    double result = solver.solve(100, null, -1, 1, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
    assertEquals(0.5, result, 1e-6);
  }
  
}