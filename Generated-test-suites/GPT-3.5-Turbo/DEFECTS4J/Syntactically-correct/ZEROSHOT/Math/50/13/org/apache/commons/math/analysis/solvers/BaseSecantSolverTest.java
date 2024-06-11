import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {
  
  @Test
  public void solve_ReturnsCorrectRoot() {
    // Given
    double absoluteAccuracy = 1e-6;
    BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
    BaseSecantSolver solver = new BaseSecantSolverImpl(absoluteAccuracy, method);
    UnivariateRealFunction f = new UnivariateRealFunctionImpl();
    double min = -10.0;
    double max = 10.0;
    double startValue = 0.0;
    BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
    double expectedRoot = 3.0;
    
    // When
    double actualRoot = solver.solve(100, f, min, max, startValue, allowedSolution);
    
    // Then
    assertEquals(expectedRoot, actualRoot, absoluteAccuracy);
  }
  
  @Test
  public void solve_ThrowsMathInternalError_WhenMethodIsInvalid() {
    // Given
    double absoluteAccuracy = 1e-6;
    BaseSecantSolver.Method method = BaseSecantSolver.Method.SECANT;
    BaseSecantSolver solver = new BaseSecantSolverImpl(absoluteAccuracy, method);
    UnivariateRealFunction f = new UnivariateRealFunctionImpl();
    double min = -10.0;
    double max = 10.0;
    double startValue = 0.0;
    BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
    
    // Then
    assertThrows(MathInternalError.class, () -> {
      // When
      solver.solve(100, f, min, max, startValue, allowedSolution);
    });
  }
  
  private static class BaseSecantSolverImpl extends BaseSecantSolver {
    
    protected BaseSecantSolverImpl(double absoluteAccuracy, Method method) {
      super(absoluteAccuracy, method);
    }
    
    @Override
    public double computeObjectiveValue(double x) {
      // Implementation for testing purposes
    }
    
    @Override
    public void verifyBracketing(double min, double max) {
      // Implementation for testing purposes
    }
    
  }
  
  private static class UnivariateRealFunctionImpl implements UnivariateRealFunction {
    
    @Override
    public double value(double x) {
      // Implementation for testing purposes
    }
    
  }
  
}