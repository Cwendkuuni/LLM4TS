import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    
    double result = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    }, 1, 3);
    
    assertEquals(2.0, result, 1e-6);
  }
  
  private static class BaseSecantSolverTestImpl extends BaseSecantSolver {
  
    public BaseSecantSolverTestImpl(double absoluteAccuracy, BaseSecantSolver.Method method) {
      super(absoluteAccuracy, method);
    }
  
    public BaseSecantSolverTestImpl(double relativeAccuracy, double absoluteAccuracy, BaseSecantSolver.Method method) {
      super(relativeAccuracy, absoluteAccuracy, method);
    }
  
    public BaseSecantSolverTestImpl(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, BaseSecantSolver.Method method) {
      super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
    }
  }
}