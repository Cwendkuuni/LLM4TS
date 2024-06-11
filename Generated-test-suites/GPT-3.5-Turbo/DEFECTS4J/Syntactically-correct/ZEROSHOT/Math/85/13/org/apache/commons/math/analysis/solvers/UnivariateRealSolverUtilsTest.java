import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.solvers.*;

public class UnivariateRealSolverUtilsTest {
  
  @Test
  public void testSolveWithoutAccuracy() {
    // Arrange
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x - 5;
      }
    };
    double x0 = 0;
    double x1 = 10;
    double expected = 5;
    
    // Act
    double result = UnivariateRealSolverUtils.solve(f, x0, x1);
    
    // Assert
    Assert.assertEquals(expected, result, 0.00001);
  }
  
  @Test
  public void testSolveWithAccuracy() {
    // Arrange
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x - 5;
      }
    };
    double x0 = 0;
    double x1 = 10;
    double absoluteAccuracy = 0.00001;
    double expected = 5;
    
    // Act
    double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    
    // Assert
    Assert.assertEquals(expected, result, absoluteAccuracy);
  }
  
  @Test(expected = ConvergenceException.class)
  public void testSolveWithInvalidInterval() {
    // Arrange
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x - 5;
      }
    };
    double x0 = 10;
    double x1 = 0;
    
    // Act
    UnivariateRealSolverUtils.solve(f, x0, x1);
  }
  
  @Test
  public void testBracket() {
    // Arrange
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x - 5;
      }
    };
    double initial = 2;
    double lowerBound = 0;
    double upperBound = 10;
    
    // Act
    double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    
    // Assert
    Assert.assertEquals(initial - 1, result[0], 0.00001);
    Assert.assertEquals(initial + 1, result[1], 0.00001);
  }
  
  @Test(expected = ConvergenceException.class)
  public void testBracketWithInvalidParameters() {
    // Arrange
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x - 5;
      }
    };
    double initial = 2;
    double lowerBound = 10;
    double upperBound = 0;
    
    // Act
    UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
  }
  
  @Test
  public void testMidpoint() {
    // Arrange
    double a = 0;
    double b = 10;
    double expected = 5;
    
    // Act
    double result = UnivariateRealSolverUtils.midpoint(a, b);
    
    // Assert
    Assert.assertEquals(expected, result, 0.00001);
  }
}