import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
  
  // Test solve method with default solver
  @Test
  public void testSolveWithDefaultSolver() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };
    
    double x0 = 0.0;
    double x1 = 1.0;
    
    double result = UnivariateRealSolverUtils.solve(f, x0, x1);
    
    assertEquals(Math.PI/2, result, 0.00001);
  }
  
  // Test solve method with specified absolute accuracy
  @Test
  public void testSolveWithSpecifiedAccuracy() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.cos(x);
      }
    };
    
    double x0 = 0.0;
    double x1 = Math.PI/4;
    double absoluteAccuracy = 0.0001;
    
    double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    
    assertEquals(Math.PI/4, result, 0.0001);
  }
  
  // Test bracket method
  @Test
  public void testBracket() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.pow(x, 2) - 4*x + 3;
      }
    };
    
    double initial = 0.5;
    double lowerBound = -1.0;
    double upperBound = 2.0;
    
    double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    
    assertEquals(-0.5, result[0], 0.0001);
    assertEquals(1.5, result[1], 0.0001);
  }
  
  // Test bracket method with maximum iterations specified
  @Test
  public void testBracketWithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };
    
    double initial = 0.5;
    double lowerBound = -1.0;
    double upperBound = 2.0;
    int maximumIterations = 10;
    
    double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    
    assertEquals(-0.5, result[0], 0.0001);
    assertEquals(1.5, result[1], 0.0001);
  }
  
  // Test midpoint method
  @Test
  public void testMidpoint() {
    double a = 1.0;
    double b = 2.0;
    
    double result = UnivariateRealSolverUtils.midpoint(a, b);
    
    assertEquals(1.5, result, 0.0001);
  }

}