import static org.junit.Assert.*;
import org.junit.Test;

public class PowellOptimizerTest {
  
  @Test
  public void testDoOptimize() {
    // Test case 1
    PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
    double[] startPoint1 = {1.0, 2.0};
    optimizer.setStartPoint(startPoint1);
    double[] result1 = optimizer.doOptimize().getPoint();
    double[] expected1 = {1.0, 2.0};
    assertArrayEquals(expected1, result1, 1e-6);

    // Test case 2
    PowellOptimizer optimizer2 = new PowellOptimizer(1e-6, 1e-6);
    double[] startPoint2 = {-1.0, -2.0};
    optimizer2.setStartPoint(startPoint2);
    double[] result2 = optimizer2.doOptimize().getPoint();
    double[] expected2 = {-1.0, -2.0};
    assertArrayEquals(expected2, result2, 1e-6);
  }
  
  @Test
  public void testNewPointAndDirection() {
    PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
    double[] p = {1.0, 2.0};
    double[] d = {2.0, 1.0};
    double[][] result = optimizer.newPointAndDirection(p, d, 0.5);
    double[] expected1 = {2.0, 3.0};
    double[] expected2 = {1.0, 0.5};
    assertArrayEquals(expected1, result[0], 1e-6);
    assertArrayEquals(expected2, result[1], 1e-6);
  }
  
  @Test
  public void testSearch() {
    PowellOptimizer opt = new PowellOptimizer(1e-6, 1e-6);
    LineSearch lineSearch = opt.new LineSearch(1e-6, 1e-6);
    double[] p = {1.0, 2.0};
    double[] d = {2.0, 1.0};
    UnivariatePointValuePair result = lineSearch.search(p, d);

    // Expected result obtained from manual calculations
    double expectedValue = 2.5;
    double expectedPoint = 0.25;
    assertEquals(expectedValue, result.getValue(), 1e-6);
    assertEquals(expectedPoint, result.getPoint(), 1e-6);
  }
}