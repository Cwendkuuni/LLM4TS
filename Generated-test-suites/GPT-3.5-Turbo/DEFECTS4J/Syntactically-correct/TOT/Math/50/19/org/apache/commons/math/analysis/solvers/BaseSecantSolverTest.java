import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

  @Test
  public void testSolve_DefaultConstructors() {
    // Setup
    double relativeAccuracy = 1e-6;
    double absoluteAccuracy = 1e-6;
    double functionValueAccuracy = 1e-15;
    BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
    BaseSecantSolver solver = new BaseSecantSolverImpl(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);

    // Test
    double result = solver.solve(100, function, 1.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    
    // Verify
    assertEquals(expectedResult, result, 1e-6);
  }
  
  @Test
  public void testSolve_CustomConstructors() {
    // Setup
    double relativeAccuracy = 1e-8;
    double absoluteAccuracy = 1e-10;
    double functionValueAccuracy = 1e-12;
    BaseSecantSolver.Method method = BaseSecantSolver.Method.ILLINOIS;
    BaseSecantSolver solver = new BaseSecantSolverImpl(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);

    // Test
    double result = solver.solve(100, function, 1.0, 2.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    
    // Verify
    assertEquals(expectedResult, result, 1e-8);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testSolve_InvalidMaxEval() {
    // Setup
    double relativeAccuracy = 1e-8;
    double absoluteAccuracy = 1e-10;
    double functionValueAccuracy = 1e-12;
    BaseSecantSolver.Method method = BaseSecantSolver.Method.PEGASUS;
    BaseSecantSolver solver = new BaseSecantSolverImpl(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);

    // Test
    double result = solver.solve(0, function, 1.0, 2.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
  }
  
  private UnivariateRealFunction function = new UnivariateRealFunction() {
    public double value(double x) {
      return x * x - 4.0;
    }
  };
  
  private double expectedResult = 2.0;
  
  private class BaseSecantSolverImpl extends BaseSecantSolver {
    public BaseSecantSolverImpl(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, Method method) {
      super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
    }
    
    protected double computeObjectiveValue(double x) {
      return function.value(x);
    }
  }

}