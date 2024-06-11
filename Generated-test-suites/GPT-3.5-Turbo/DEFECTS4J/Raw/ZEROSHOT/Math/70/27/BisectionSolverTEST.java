import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {
  
  private BisectionSolver solver;
  
  @Before
  public void setUp() {
    solver = new BisectionSolver();
  }
  
  @Test
  public void testSolve_WithValidFunction_ReturnsCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    
    double result = solver.solve(f, 0, 10);
    
    assertEquals(Math.sqrt(4), result, 1e-6);
  }
  
  @Test(expected = MaxIterationsExceededException.class)
  public void testSolve_WithInvalidFunction_ThrowsMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x;
      }
    };
    
    solver.solve(f, 0, 10);
  }
  
  @Test
  public void testSolve_WithNegativeInterval_ReturnsCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    
    double result = solver.solve(f, -10, 0);
    
    assertEquals(-Math.sqrt(4), result, 1e-6);
  }
  
  @Test
  public void testSolve_WithZeroInterval_ReturnsCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    
    double result = solver.solve(f, -2, 2);
    
    assertEquals(Math.sqrt(4), result, 1e-6);
  }
  
  @Test(expected = MaxIterationsExceededException.class)
  public void testSolve_WithInvalidZeroInterval_ThrowsMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    
    solver.solve(f, -1, 1);
  }
  
  @Test(expected = MaxIterationsExceededException.class)
  public void testSolve_WithInvalidFunctionAndInterval_ThrowsMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException();
      }
    };
    
    solver.solve(f, 0, 10);
  }
  
}