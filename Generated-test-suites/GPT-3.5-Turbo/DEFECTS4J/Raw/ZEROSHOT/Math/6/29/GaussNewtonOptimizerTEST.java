import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
  
  @Test
  public void testDoOptimize() {
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
    
    // Test case 1
    double[] targetValues1 = {1.0, 2.0, 3.0, 4.0};
    double[] startPoint1 = {0.0, 0.0, 0.0};
    RealMatrix weightMatrix1 = new Array2DRowRealMatrix(4, 4);
    double[] expectedResult1 = {0.0, 0.0, 0.0};
    
    optimizer.setTarget(targetValues1);
    optimizer.setStartPoint(startPoint1);
    optimizer.setWeight(weightMatrix1);
    
    PointVectorValuePair result1 = optimizer.doOptimize();
    
    assertArrayEquals(expectedResult1, result1.getPoint(), 0.0001);
    
    // Test case 2
    double[] targetValues2 = {3.0, 5.0, 7.0, 9.0};
    double[] startPoint2 = {1.0, 1.0, 1.0};
    RealMatrix weightMatrix2 = new Array2DRowRealMatrix(4, 4);
    double[] expectedResult2 = {1.0, 2.0, 3.0};
    
    optimizer.setTarget(targetValues2);
    optimizer.setStartPoint(startPoint2);
    optimizer.setWeight(weightMatrix2);
    
    PointVectorValuePair result2 = optimizer.doOptimize();
    
    assertArrayEquals(expectedResult2, result2.getPoint(), 0.0001);
  }
}