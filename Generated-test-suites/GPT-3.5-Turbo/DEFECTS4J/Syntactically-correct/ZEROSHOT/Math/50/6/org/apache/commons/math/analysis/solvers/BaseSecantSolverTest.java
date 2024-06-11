import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
  
  @Test
  public void testSolve() {
    double absoluteAccuracy = 1e-6;
    double relativeAccuracy = 1e-12;
    double functionValueAccuracy = 1e-6;
    BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, relativeAccuracy, functionValueAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
    
    // Test case 1
    int maxEval1 = 100;
    UnivariateRealFunction f1 = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 1;
      }
    };
    double min1 = 0;
    double max1 = 2;
    double startValue1 = 1.5;
    BaseSecantSolver.AllowedSolution allowedSolution1 = BaseSecantSolver.AllowedSolution.ANY_SIDE;
    
    double result1 = solver.solve(maxEval1, f1, min1, max1, startValue1, allowedSolution1);
    
    assertEquals(1, result1, absoluteAccuracy);
    
    // Test case 2
    int maxEval2 = 100;
    UnivariateRealFunction f2 = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 2;
      }
    };
    double min2 = 0;
    double max2 = 3;
    double startValue2 = 1.5;
    BaseSecantSolver.AllowedSolution allowedSolution2 = BaseSecantSolver.AllowedSolution.ANY_SIDE;
    
    double result2 = solver.solve(maxEval2, f2, min2, max2, startValue2, allowedSolution2);
    
    assertEquals(Math.sqrt(2), result2, absoluteAccuracy);
  }
}