import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {
  
  @Test
  public void testConstructor() {
    // Test with default constructor
    PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
    assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
    assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 0.0);
    
    // Test with specified constructor
    PowellOptimizer optimizer2 = new PowellOptimizer(0.001, 0.001, 0.01, 0.01);
    assertEquals(0.001, optimizer2.getRelativeThreshold(), 0.0);
    assertEquals(0.001, optimizer2.getAbsoluteThreshold(), 0.0);
    assertEquals(0.01, optimizer2.getLine().getRelativeThreshold(), 0.0);
    assertEquals(0.01, optimizer2.getLine().getAbsoluteThreshold(), 0.0);
  }
  
  @Test
  public void testComputeObjectiveValue() {
    // Create optimizer instance
    PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
    
    // Create test point
    double[] point = {1, 2, 3};
    
    // Compute objective value
    double objectiveValue = optimizer.computeObjectiveValue(point);
    
    // Check result
    assertEquals(14.0, objectiveValue, 0.0);
  }
  
  @Test
  public void testNewPointAndDirection() {
    // Create optimizer instance
    PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
    
    // Create test point and direction
    double[] point = {1, 2, 3};
    double[] direction = {4, 5, 6};
    
    // Call newPointAndDirection method
    double[][] result = optimizer.newPointAndDirection(point, direction, 0.5);
    
    // Check result
    assertArrayEquals(new double[]{3.0, 4.5, 6.0}, result[0], 0.0);
    assertArrayEquals(new double[]{2.0, 2.5, 3.0}, result[1], 0.0);
  }
  
  @Test
  public void testSearch() {
    // Create optimizer instance
    PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
    
    // Create test point and direction
    double[] point = {1, 2, 3};
    double[] direction = {4, 5, 6};
    
    // Call search method
    UnivariatePointValuePair result = optimizer.getLine().search(point, direction);
    
    // Check result
    assertEquals(1.0, result.getPoint(), 0.0);
    assertEquals(14.0, result.getValue(), 0.0);
  }
  
  @Test
  public void testDoOptimize() {
    // Create optimizer instance
    PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
    
    // Set start point
    optimizer.setStartPoint(new double[]{1, 2, 3});
    
    // Call doOptimize method
    PointValuePair result = optimizer.doOptimize();
    
    // Check result
    assertArrayEquals(new double[]{1.0, 2.0, 3.0}, result.getPoint(), 0.0);
    assertEquals(14.0, result.getValue(), 0.0);
  }
  
  @Test
  public void testPowellOptimizer() {
    // Test with default constructor
    PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
    assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
    assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 0.0);
    
    // Test with specified constructor
    PowellOptimizer optimizer2 = new PowellOptimizer(0.001, 0.001, 0.01, 0.01);
    assertEquals(0.001, optimizer2.getRelativeThreshold(), 0.0);
    assertEquals(0.001, optimizer2.getAbsoluteThreshold(), 0.0);
    assertEquals(0.01, optimizer2.getLine().getRelativeThreshold(), 0.0);
    assertEquals(0.01, optimizer2.getLine().getAbsoluteThreshold(), 0.0);
    
    // Test with specified constructor and convergence checker
    PowellOptimizer optimizer3 = new PowellOptimizer(0.001, 0.001, 0.01, 0.01, null);
    assertEquals(0.001, optimizer3.getRelativeThreshold(), 0.0);
    assertEquals(0.001, optimizer3.getAbsoluteThreshold(), 0.0);
    assertEquals(0.01, optimizer3.getLine().getRelativeThreshold(), 0.0);
    assertEquals(0.01, optimizer3.getLine().getAbsoluteThreshold(), 0.0);
  }
}