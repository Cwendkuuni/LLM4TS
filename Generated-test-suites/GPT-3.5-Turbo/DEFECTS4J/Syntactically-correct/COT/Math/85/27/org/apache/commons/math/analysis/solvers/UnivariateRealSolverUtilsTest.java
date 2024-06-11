import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.solvers.FunctionEvaluationException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
  
  @Test
  public void testSolve() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 4;
      }
    };
    double x0 = 1;
    double x1 = 3;
    double result = UnivariateRealSolverUtils.solve(f, x0, x1);

    // Check if result is close to the actual answer
    double expected = 2.0;
    double epsilon = 0.01;
    assertEquals(expected, result, epsilon);
  }

  @Test
  public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 4;
      }
    };
    double x0 = 1;
    double x1 = 3;
    double accuracy = 0.001;
    double result = UnivariateRealSolverUtils.solve(f, x0, x1, accuracy);
    
    // Check if result is close to the actual answer
    double expected = 2.0;
    double epsilon = 0.01;
    assertEquals(expected, result, epsilon);
  }

  @Test
  public void testBracket() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x*x - 4;
      }
    };
    double initial = 2;
    double lowerBound = 0;
    double upperBound = 4;
    int maximumIterations = 100;
    double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);

    // Check if the result brackets a root
    double a = result[0];
    double b = result[1];
    assertTrue(f.value(a) * f.value(b) <= 0);
  }

  @Test
  public void testMidpoint() {
    double a = 1;
    double b = 3;
    double result = UnivariateRealSolverUtils.midpoint(a, b);
    
    // Check if the result is exactly the midpoint
    double expected = 2.0;
    assertEquals(expected, result, 0.0);
  }

}