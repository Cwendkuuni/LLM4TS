import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {
  
  private BaseSecantSolver solver;
  
  @Before
  public void setUp() {
    solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
  }
  
  @Test
  public void testSolve_ExactRoot_ReturnsRoot() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    
    double root = solver.solve(100, f, 4, 6);
    
    assertEquals(5.0, root, 0.0);
  }
  
  @Test
  public void testSolve_ApproximateRoot_ReturnsRoot() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    
    double root = solver.solve(100, f, 1, 3);
    
    assertEquals(2.0, root, 1e-6);
  }
  
  @Test(expected = RuntimeException.class)
  public void testSolve_FunctionNotBracketed_ThrowsException() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x + 5;
      }
    };
    
    double root = solver.solve(100, f, 1, 3);
  }
}