import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {
  
  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double solution = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 16;
      }
    }, 0, 5);
    assertEquals(4, solution, 1e-6);
  
    solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
    solution = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 16;
      }
    }, 0, 5);
    assertEquals(4, solution, 1e-6);
    
    solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
    solution = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 16;
      }
    }, 0, 5);
    assertEquals(4, solution, 1e-6);
  }
  
  @Test
  public void testSolveWithAllowedSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double solution = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 16;
      }
    }, 0, 5, AllowedSolution.ANY_SIDE);
    assertEquals(4, solution, 1e-6);
    
    solution = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 16;
      }
    }, 0, 5, AllowedSolution.LEFT_SIDE);
    assertEquals(4, solution, 1e-6);
    
    solution = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 16;
      }
    }, 0, 5, AllowedSolution.RIGHT_SIDE);
    assertEquals(4, solution, 1e-6);
    
    solution = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 16;
      }
    }, 0, 5, AllowedSolution.BELOW_SIDE);
    assertEquals(4, solution, 1e-6);
    
    solution = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 16;
      }
    }, 0, 5, AllowedSolution.ABOVE_SIDE);
    assertEquals(4, solution, 1e-6);
  }
  
}