import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

  private MultiDirectional optimizer;

  @Before
  public void setUp() {
    optimizer = new MultiDirectional();
  }

  @Test
  public void testDefaultConstructor() {
    assertNotNull(optimizer);
    assertEquals(2.0, optimizer.khi, 0);
    assertEquals(0.5, optimizer.gamma, 0);
  }

  @Test
  public void testCustomConstructor() {
    MultiDirectional customOptimizer = new MultiDirectional(3.5, 0.25);
    assertNotNull(customOptimizer);
    assertEquals(3.5, customOptimizer.khi, 0);
    assertEquals(0.25, customOptimizer.gamma, 0);
  }

  @Test
  public void testIterateSimplex() {
    // Test a simple scenario with a convex function
    // Expected result: converge to optimal solution
    double[] startPoint = {1.0, 1.0, 1.0};
    optimizer.setStartPoint(startPoint);
    Optimizer.Result result = optimizer.optimize();
    assertTrue(result.hasConverged());
    assertEquals(0.0, result.getOptimumValue(), 1e-10);
    double[] expectedOptimalPoint = {0.0, 0.0, 0.0};
    assertArrayEquals(expectedOptimalPoint, result.getOptimumPoint(), 1e-10);
  }

  @Test
  public void testEvaluateNewSimplex() {
    // Test evaluating a new simplex with linear coefficients
    // Expected result: transform the simplex correctly
    double[] originPoint = {1.0, 1.0, 1.0};
    double coefficient = 0.5;
    RealPointValuePair[] original = new RealPointValuePair[4];
    original[0] = new RealPointValuePair(originPoint, 0.0, false);
    original[1] = new RealPointValuePair(new double[]{2.0, 2.0, 2.0}, 1.0, false);
    original[2] = new RealPointValuePair(new double[]{3.0, 3.0, 3.0}, 2.0, false);
    original[3] = new RealPointValuePair(new double[]{4.0, 4.0, 4.0}, 3.0, false);
    RealPointValuePair result = optimizer.evaluateNewSimplex(original, coefficient);
    double[] expectedTransformedPoint = {0.5, 0.5, 0.5};
    assertArrayEquals(expectedTransformedPoint, result.getPointRef(), 1e-10);
  }

  // Add more test cases for each method

}