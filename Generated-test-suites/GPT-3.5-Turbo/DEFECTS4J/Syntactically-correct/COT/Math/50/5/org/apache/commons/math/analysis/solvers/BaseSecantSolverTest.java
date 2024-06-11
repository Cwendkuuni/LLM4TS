import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

  @Test
  public void testSolve() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.pow(x, 2) - 2;
      }
    };
    
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    double result = solver.solve(100, f, 0, 2);
    Assert.assertEquals(Math.sqrt(2), result, 1e-6);
  }

  @Test
  public void testSolveMaxEval() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.pow(x, 2) - 4;
      }
    };
    
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    double result = solver.solve(5, f, 0, 2);
    Assert.assertEquals(Double.NaN, result, 1e-6);
  }

  // Edge cases
  @Test
  public void testSolveExactRoot() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };
    
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    double result = solver.solve(100, f, 1, 3);
    Assert.assertEquals(2, result, 1e-6);
  }

  // Exception handling
  @Test(expected = IllegalArgumentException.class)
  public void testSolveInvalidRange() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.pow(x, 2) - 2;
      }
    };
    
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    double result = solver.solve(100, f, 2, 0);
  }

}