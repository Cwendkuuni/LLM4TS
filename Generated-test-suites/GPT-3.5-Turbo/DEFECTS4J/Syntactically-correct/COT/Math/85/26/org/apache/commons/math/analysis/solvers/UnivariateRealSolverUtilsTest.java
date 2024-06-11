import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.solvers.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

  @Test
  public void testSolve() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4; // Function that has a root at x = 2
      }
    };
    double x0 = 1.5;
    double x1 = 2.5;
    
    double result = UnivariateRealSolverUtils.solve(f, x0, x1);
    
    assertEquals("testSolve failed", 2.0, result, 0.0001);
  }
  
  @Test
  public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double x0 = 1.5;
    double x1 = 2.5;
    double absoluteAccuracy = 0.01;
    
    double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    
    assertEquals("testSolveWithAccuracy failed", 2.0, result, absoluteAccuracy);
  }
  
  @Test
  public void testBracket() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double initial = 2.5;
    double lowerBound = 1.5;
    double upperBound = 3.5;
    int maximumIterations = 100;
    
    double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    
    assertEquals("testBracket failed - a value incorrect", 2.0, result[0], 0.0001);
    assertEquals("testBracket failed - b value incorrect", 3.0, result[1], 0.0001);
  }
  
  @Test
  public void testMidpoint() {
    double a = -2;
    double b = 2;
    
    double result = UnivariateRealSolverUtils.midpoint(a, b);
    
    assertEquals("testMidpoint failed", 0.0, result, 0.0001);
  }

}