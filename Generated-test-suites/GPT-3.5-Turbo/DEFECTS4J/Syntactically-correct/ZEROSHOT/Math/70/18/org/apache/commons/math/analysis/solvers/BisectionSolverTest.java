import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {
  
  // Test the solve method with valid inputs
  @Test
  public void testSolve_ValidInputs() throws MaxIterationsExceededException, FunctionEvaluationException {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      @Override
      public double value(double x) throws FunctionEvaluationException {
        // Define your function here
        return 1 - Math.pow(x, 2);
      }
    };
    double min = -1.0;
    double max = 1.0;

    double result = solver.solve(f, min, max);
    
    // Assert that the result is within the expected range
    Assert.assertTrue(result >= min && result <= max);
  }

  // Test the solve method with invalid inputs
  @Test(expected = MaxIterationsExceededException.class)
  public void testSolve_InvalidInputs() throws MaxIterationsExceededException, FunctionEvaluationException {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      @Override
      public double value(double x) throws FunctionEvaluationException {
        // Define your function here
        return 1 - Math.pow(x, 2);
      }
    };
    double min = 0.0;
    double max = 1.0;

    double result = solver.solve(f, min, max);
  }
}