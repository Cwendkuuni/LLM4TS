import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {
  
  @Test
  public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
    BisectionSolver solver = new BisectionSolver();
    
    // Test case 1: Zero at midpoint
    UnivariateRealFunction function1 = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x - 5;
      }
    };
    double result1 = solver.solve(function1, 0, 10);
    Assert.assertEquals(5.0, result1, 0.0001);
    
    // Test case 2: Zero in first half
    UnivariateRealFunction function2 = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x - 7;
      }
    };
    double result2 = solver.solve(function2, 1, 10);
    Assert.assertEquals(7.0, result2, 0.0001);
    
    // Test case 3: Zero in second half
    UnivariateRealFunction function3 = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x - 3;
      }
    };
    double result3 = solver.solve(function3, 0, 5);
    Assert.assertEquals(3.0, result3, 0.0001);
    
    // Test case 4: No zero in range
    UnivariateRealFunction function4 = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x + 2;
      }
    };
    try {
      double result4 = solver.solve(function4, 0, 5);
      Assert.fail("Expected MaxIterationsExceededException");
    } catch (MaxIterationsExceededException e) {
      // Test passed
    }
  }
}