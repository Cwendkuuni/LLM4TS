import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {
  
  // Test cases for solve() method
  
  @Test
  public void testSolve(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction(){
      public double value(double x){ return x*x - 4; }
    };
    double solution = solver.solve(100, f, 0, 5);
    assertEquals(2.0, solution, 0.001);
  }
  
  @Test(expected=MathInternalError.class)
  public void testSolveException(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction(){
      public double value(double x){ return x*x - 4; }
    };
    double solution = solver.solve(100, f, 0, 5);
    assertEquals(2.0, solution, 0.001);
  }
  
  // Test cases for solve() method with allowedSolution parameter
  
  @Test
  public void testSolveAllowedSolution(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction(){
      public double value(double x){ return x*x - 4; }
    };
    double solution = solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(2.0, solution, 0.001);
  }
  
  @Test
  public void testSolveAllowedSolutionLeftSide(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction(){
      public double value(double x){ return x*x - 4; }
    };
    double solution = solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    assertEquals(2.0, solution, 0.001);
  }
  
  @Test
  public void testSolveAllowedSolutionRightSide(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction(){
      public double value(double x){ return x*x - 4; }
    };
    double solution = solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
    assertEquals(2.0, solution, 0.001);
  }

  @Test
  public void testSolveAllowedSolutionBelowSide(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction(){
      public double value(double x){ return x*x - 4; }
    };
    double solution = solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    assertEquals(2.0, solution, 0.001);
  }
  
  @Test
  public void testSolveAllowedSolutionAboveSide(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction(){
      public double value(double x){ return x*x - 4; }
    };
    double solution = solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    assertEquals(2.0, solution, 0.001);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testSolveAllowedSolutionInvalid(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction(){
      public double value(double x){ return x*x - 4; }
    };
    double solution = solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.INVALID);
    assertEquals(2.0, solution, 0.001);
  }
  
  // Additional test cases for doSolve() method
  
  @Test
  public void testDoSolve(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
    solver.setMin(-2);
    solver.setMax(2);
    solver.setFunctionValueAccuracy(0.0001);
    
    double result = solver.doSolve();
    assertEquals(0.0, result, 0.001);
  }
  
  @Test
  public void testDoSolve2(){
    BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
    solver.setMin(-2);
    solver.setMax(2);
    solver.setFunctionValueAccuracy(0.0001);
    
    double result = solver.doSolve();
    assertEquals(0.0, result, 0.001);
  }

}