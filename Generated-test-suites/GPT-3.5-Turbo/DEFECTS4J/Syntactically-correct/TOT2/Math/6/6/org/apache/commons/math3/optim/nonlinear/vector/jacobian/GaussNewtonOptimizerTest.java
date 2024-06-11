import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {
  
  @Test
  public void testDoOptimize() {
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
    PointVectorValuePair result = optimizer.doOptimize();
    
    assertNotNull(result);
    assertNotNull(result.getPoint());
    assertNotNull(result.getValue());
  }
  
  @Test
  public void testDoOptimizeWithNullChecker() {
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
    optimizer.setConvergenceChecker(null);
    
    try {
      optimizer.doOptimize();
      fail("Expected NullArgumentException");
    } catch (NullArgumentException e) {
      // Expected
    }
  }
  
  @Test
  public void testDoOptimizeWithSingularMatrix() {
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
    
    // Set up singular matrix condition
    optimizer.setTarget(new double[]{1.0, 1.0});
    optimizer.setWeight(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}}));
    optimizer.setStartPoint(new double[]{1.0, 1.0});
    
    try {
      optimizer.doOptimize();
      fail("Expected ConvergenceException");
    } catch (ConvergenceException e) {
      // Expected
    }
  }
}