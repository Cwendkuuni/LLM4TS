import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {
  
  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 4;
      }
    }, 0, 5);
    
    Assert.assertEquals(2, result, 1e-6);
  }
  
  @Test
  public void testSolve_WithMethod() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
    
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 9;
      }
    }, 0, 5);
    
    Assert.assertEquals(3, result, 1e-6);
  }
  
  @Test
  public void testSolve_WithStartValue() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
    
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 16;
      }
    }, 0, 5, 1);
    
    Assert.assertEquals(4, result, 1e-6);
  }
  
  @Test
  public void testSolve_WithMaxEval() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    
    double result = solver.solve(5, new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 25;
      }
    }, 0, 5);
    
    Assert.assertEquals(5, result, 1e-6);
  }
  
  @Test
  public void testSolve_WithAllowedSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 36;
      }
    }, 0, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
    
    Assert.assertEquals(5, result, 1e-6);
  }
  
  @Test
  public void testSolve_WithBracketing() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 49;
      }
    }, 0, 50);
    
    Assert.assertEquals(7, result, 1e-6);
  }
  
  @Test
  public void testSolve_WithFunctionValueAccuracy() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    solver.setFunctionValueAccuracy(1e-12);
    
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 64;
      }
    }, 0, 10);
    
    Assert.assertEquals(8, result, 1e-12);
  }
  
  @Test
  public void testSolve_WithAbsoluteAccuracy() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    solver.setAbsoluteAccuracy(1e-12);
    
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 81;
      }
    }, 0, 10);
    
    Assert.assertEquals(9, result, 1e-12);
  }
  
  @Test
  public void testSolve_WithRelativeAccuracy() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    solver.setRelativeAccuracy(1e-12);
    
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 100;
      }
    }, 0, 10);
    
    Assert.assertEquals(10, result, 1e-12);
  }
  
  @Test
  public void testDoSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    
    solver.setMin(-10);
    solver.setMax(10);
    
    double result = solver.doSolve();
    
    Assert.assertEquals(0, result, 1e-6);
  }
}