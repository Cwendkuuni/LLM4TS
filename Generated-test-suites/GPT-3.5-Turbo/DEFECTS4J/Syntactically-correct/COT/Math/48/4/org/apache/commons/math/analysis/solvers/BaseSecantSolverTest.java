import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {
  
  @Test
  public void testSolve_basicCase() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double solution = solver.solve(100, f, 0, 10);
    assertEquals(2, solution, 1e-6);
  }
  
  @Test
  public void testSolve_edgeCase() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double solution = solver.solve(100, f, 0, 10);
    assertEquals(5, solution, 1e-6);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSolve_invalidBounds() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    solver.solve(100, f, 10, 0);
  }
  
  @Test(expected = ConvergenceException.class)
  public void testSolve_noSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x + 4;
      }
    };
    solver.solve(100, f, 0, 10);
  }
}