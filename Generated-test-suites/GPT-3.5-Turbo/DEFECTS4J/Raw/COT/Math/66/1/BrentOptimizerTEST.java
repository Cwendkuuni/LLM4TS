import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer();
    // Test case 1: Testing with a basic univariate function
    UnivariateRealFunction function = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4 * x + 3;
      }
    };
    double min = 0.0;
    double max = 5.0;
    double expectedOptimum = 1.0;
    double epsilon = 1e-6;
    double actualOptimum = optimizer.optimize(function, GoalType.MINIMIZE, min, max);
    assertEquals(expectedOptimum, actualOptimum, epsilon);

    // Test case 2: Testing with a function that has multiple local minima
    UnivariateRealFunction function2 = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x * x - 6 * x * x + 11 * x - 6;
      }
    };
    double min2 = -5.0;
    double max2 = 5.0;
    double expectedOptimum2 = 1.0;
    double actualOptimum2 = optimizer.optimize(function2, GoalType.MINIMIZE, min2, max2);
    assertEquals(expectedOptimum2, actualOptimum2, epsilon);

    // Test case 3: Testing with a function that has no minimum within the given range
    UnivariateRealFunction function3 = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x + 1;
      }
    };
    double min3 = -5.0;
    double max3 = 5.0;
    double expectedOptimum3 = Double.NaN;
    double actualOptimum3 = optimizer.optimize(function3, GoalType.MINIMIZE, min3, max3);
    assertEquals(expectedOptimum3, actualOptimum3, epsilon);

    // Test case 4: Testing with a function that has a maximum instead of a minimum
    UnivariateRealFunction function4 = new UnivariateRealFunction() {
      public double value(double x) {
        return -x * (x - 2);
      }
    };
    double min4 = 0.0;
    double max4 = 5.0;
    double expectedOptimum4 = 2.0;
    double actualOptimum4 = optimizer.optimize(function4, GoalType.MINIMIZE, min4, max4);
    assertEquals(expectedOptimum4, actualOptimum4, epsilon);
  }

  // Additional test cases for edge cases and exception handling scenarios
  @Test
  public void testEdgeCases() {
    // Test case 5: Testing when the function is constant within the given range
    UnivariateRealFunction function5 = new UnivariateRealFunction() {
      public double value(double x) {
        return 1.0;
      }
    };
    double min5 = 0.0;
    double max5 = 1.0;
    double expectedOptimum5 = 0.0;
    double actualOptimum5 = optimizer.optimize(function5, GoalType.MINIMIZE, min5, max5);
    assertEquals(expectedOptimum5, actualOptimum5, epsilon);

    // Test case 6: Testing when the function is linear within the given range
    UnivariateRealFunction function6 = new UnivariateRealFunction() {
      public double value(double x) {
        return -2.0 * x + 3.0;
      }
    };
    double min6 = 0.0;
    double max6 = 1.0;
    double expectedOptimum6 = 0.5;
    double actualOptimum6 = optimizer.optimize(function6, GoalType.MINIMIZE, min6, max6);
    assertEquals(expectedOptimum6, actualOptimum6, epsilon);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testNegativeEpsilon() {
    UnivariateRealFunction function = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4 * x + 3;
      }
    };
    double min = 0.0;
    double max = 5.0;
    double epsilon = -1e-6;
    double actualOptimum = optimizer.optimize(function, GoalType.MINIMIZE, min, max, epsilon);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testNegativeTolerance() {
    UnivariateRealFunction function = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4 * x + 3;
      }
    };
    double min = 0.0;
    double max = 5.0;
    double epsilon = 1e-6;
    double tolerance = -1e-6;
    double actualOptimum = optimizer.optimize(function, GoalType.MINIMIZE, min, max, epsilon, tolerance);
  }
}