import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
  
  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 1;
      }
    };
    double result = solver.solve(100, f, 0, 2, AllowedSolution.ANY_SIDE);
    assertEquals(1.0, result, DEFAULT_ABSOLUTE_ACCURACY);
    
    solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);
    f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 2;
      }
    };
    result = solver.solve(100, f, 0, 2, AllowedSolution.RIGHT_SIDE);
    assertEquals(Math.sqrt(2), result, DEFAULT_ABSOLUTE_ACCURACY);
    
    solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.PEGASUS);
    f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    result = solver.solve(100, f, 0, 3, AllowedSolution.LEFT_SIDE);
    assertEquals(2.0, result, DEFAULT_ABSOLUTE_ACCURACY);
  }
  
  @Test
  public void testSolveWithDifferentAccuracies() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-7, 1e-8, Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x) + x;
      }
    };
    double result = solver.solve(100, f, 0, 2, AllowedSolution.ANY_SIDE);
    assertEquals(-1.9345634, result, 1e-6);
    
    solver = new BaseSecantSolver(1e-8, 1e-9, 1e-10, Method.ILLINOIS);
    f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.cos(x) - 1;
      }
    };
    result = solver.solve(100, f, 0, 2, AllowedSolution.RIGHT_SIDE);
    assertEquals(Math.PI, result, 1e-8);
    
    solver = new BaseSecantSolver(1e-10, 1e-11, 1e-12, Method.PEGASUS);
    f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.exp(x) - 2;
      }
    };
    result = solver.solve(100, f, 0, 3, AllowedSolution.LEFT_SIDE);
    assertEquals(Math.log(2), result, 1e-10);
  }
}