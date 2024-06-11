import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testConstructor() {
    // Test valid arguments
    BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
    assertNotNull(optimizer);

    // Test invalid rel argument
    try {
      optimizer = new BrentOptimizer(-0.01, 0.001);
      fail("Expected NotStrictlyPositiveException");
    } catch (NotStrictlyPositiveException e) {
      // Expected exception
    }

    // Test invalid abs argument
    try {
      optimizer = new BrentOptimizer(0.01, -0.001);
      fail("Expected NotStrictlyPositiveException");
    } catch (NotStrictlyPositiveException e) {
      // Expected exception
    }

    // Test invalid rel and abs arguments
    try {
      optimizer = new BrentOptimizer(-0.01, -0.001);
      fail("Expected NotStrictlyPositiveException");
    } catch (NotStrictlyPositiveException e) {
      // Expected exception
    }
  }

  @Test
  public void testDoOptimize() {
    // Test MINIMIZE goal type

    // Test when function is a constant
    BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
    UnivariateFunction constantFunction = new UnivariateFunction() {
      public double value(double x) { return 5.0; }
    };
    UnivariatePointValuePair result = optimizer.optimize(100, constantFunction, GoalType.MINIMIZE);
    assertEquals(100.0, result.getPoint(), 0.001);

    // Test when function is a linear function
    optimizer = new BrentOptimizer(0.01, 0.001);
    UnivariateFunction linearFunction = new UnivariateFunction() {
      public double value(double x) { return 2.0 * x + 3.0; }
    };
    result = optimizer.optimize(100, linearFunction, GoalType.MINIMIZE);
    assertEquals(-1.5, result.getPoint(), 0.001);

    // Test when function is a quadratic function
    optimizer = new BrentOptimizer(0.01, 0.001);
    UnivariateFunction quadraticFunction = new UnivariateFunction() {
      public double value(double x) { return x * x - 4.0 * x + 4.0; }
    };
    result = optimizer.optimize(100, quadraticFunction, GoalType.MINIMIZE);
    assertEquals(2.0, result.getPoint(), 0.001);

    // Test MINIMIZE goal type with invalid arguments
    optimizer = new BrentOptimizer(0.01, 0.001);
    result = optimizer.optimize(100, null, GoalType.MINIMIZE);
    assertNull(result);

    // Test MAXIMIZE goal type
    optimizer = new BrentOptimizer(0.01, 0.001);
    result = optimizer.optimize(100, quadraticFunction, GoalType.MAXIMIZE);
    assertEquals(0.0, result.getPoint(), 0.001);

    // Test MAXIMIZE goal type with invalid arguments
    optimizer = new BrentOptimizer(0.01, 0.001);
    result = optimizer.optimize(100, null, GoalType.MAXIMIZE);
    assertNull(result);
  }

  /* Add more test cases for other methods */

}