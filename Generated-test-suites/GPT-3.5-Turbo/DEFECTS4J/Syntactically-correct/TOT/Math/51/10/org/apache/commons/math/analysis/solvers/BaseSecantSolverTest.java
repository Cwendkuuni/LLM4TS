import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {
  
  @Test
  public void testSolve_WhenRootIsInAllowedSolution_ReturnsRoot() {
    // Arrange
    BaseSecantSolver solver = new ConcreteSecantSolver();
    
    // Act
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) { return x - 5; }
    }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    
    // Assert
    Assert.assertEquals(5.0, result, 1e-6);
  }
  
  @Test(expected = MathInternalError.class)
  public void testSolve_WhenMethodIsUnknown_ThrowsMathInternalError() {
    // Arrange
    BaseSecantSolver solver = new UnknownMethodSecantSolver();
    
    // Act
    solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) { return x - 5; }
    }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    
    // Should throw MathInternalError
  }
  
  @Test
  public void testSolve_WhenAccuracyIsSet_ReturnsRootWithAccuracy() {
    // Arrange
    BaseSecantSolver solver = new ConcreteSecantSolver();
    solver.setAbsoluteAccuracy(1e-10);
    
    // Act
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) { return x - 5; }
    }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    
    // Assert
    Assert.assertEquals(5.0, result, 1e-10);
  }
  
  private class ConcreteSecantSolver extends BaseSecantSolver {
    
    public ConcreteSecantSolver() {
      super(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
    }
    
    public double computeObjectiveValue(double x) {
      return x - 5;
    }
  }
  
  private class UnknownMethodSecantSolver extends BaseSecantSolver {
    
    public UnknownMethodSecantSolver() {
      super(DEFAULT_ABSOLUTE_ACCURACY, Method.UNKNOWN);
    }
    
    public double computeObjectiveValue(double x) {
      return x - 5;
    }
  }
}