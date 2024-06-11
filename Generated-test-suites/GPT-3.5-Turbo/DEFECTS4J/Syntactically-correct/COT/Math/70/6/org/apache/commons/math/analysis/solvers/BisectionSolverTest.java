import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
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
  public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      
      public double value(double x) throws FunctionEvaluationException {
        return x - 2;
      }
      
    };
    
    double result = solver.solve(f, 0, 5);
    assertEquals(2.0, result, 1E-6);
  }
  
  @Test(expected = MaxIterationsExceededException.class)
  public void testSolveExceedMaxIterations() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      
      public double value(double x) throws FunctionEvaluationException {
        return x - 2;
      }
      
    };
    
    solver.solve(f, 0, 5, 100000); // too many iterations
  }
  
  @Test(expected = FunctionEvaluationException.class)
  public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      
      public double value(double x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException();
      }
      
    };
    
    solver.solve(f, 0, 5);
  }

}