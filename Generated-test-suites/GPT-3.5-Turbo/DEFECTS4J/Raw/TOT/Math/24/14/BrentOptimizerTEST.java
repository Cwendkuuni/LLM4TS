import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

  private double EPSILON = 1e-6;

  @Test
  public void testDoOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(1e-15, 1e-15);

    // Test case 1: Example from Brent's book (f(x) = x^2 - 2*x + 1)
    double expectedMinimum = 1.0;
    double expectedValue = 0.0;

    Function f = new Function();
    UnivariatePointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, 0.0, 2.0);

    assertEquals(expectedMinimum, result.getPoint(), EPSILON);
    assertEquals(expectedValue, result.getValue(), EPSILON);

    // Test case 2: Constant function (f(x) = 1)
    double constantVal = 1.0;
    expectedValue = -constantVal;

    f = new ConstantFunction();
    result = optimizer.optimize(100, f, GoalType.MAXIMIZE, -10.0, 10.0);

    assertEquals(Double.NaN, result.getPoint(), EPSILON);
    assertEquals(expectedValue, result.getValue(), EPSILON);

    // Test case 3: Linear function (f(x) = 2x - 3)
    double expectedMin = 1.5;
    expectedValue = -(2 * expectedMin - 3);

    f = new LinearFunction();
    result = optimizer.optimize(100, f, GoalType.MAXIMIZE, -10.0, 10.0);

    assertEquals(expectedMin, result.getPoint(), EPSILON);
    assertEquals(expectedValue, result.getValue(), EPSILON);

    // Test case 4: Quadratic function (f(x) = x^2 - x)
    expectedMinimum = 0.5;
    expectedValue = expectedMinimum * expectedMinimum - expectedMinimum;

    f = new QuadraticFunction();
    result = optimizer.optimize(100, f, GoalType.MINIMIZE, 0.0, 2.0);

    assertEquals(expectedMinimum, result.getPoint(), EPSILON);
    assertEquals(expectedValue, result.getValue(), EPSILON);

    // Test case 5: Cubic function (f(x) = x^3 - 3x^2 + x)
    expectedMin = 2.0 / 3.0;
    expectedValue = Math.pow(expectedMin, 3) - 3 * Math.pow(expectedMin, 2) + expectedMin;

    f = new CubicFunction();
    result = optimizer.optimize(100, f, GoalType.MINIMIZE, 0.0, 2.0);

    assertEquals(expectedMin, result.getPoint(), EPSILON);
    assertEquals(expectedValue, result.getValue(), EPSILON);

    // Test case 6: Exponential function (f(x) = e^x)
    expectedMin = 0.0;
    expectedValue = Math.exp(expectedMin);

    f = new ExponentialFunction();
    result = optimizer.optimize(100, f, GoalType.MINIMIZE, -10.0, 10.0);

    assertEquals(expectedMin, result.getPoint(), EPSILON);
    assertEquals(expectedValue, result.getValue(), EPSILON);
  }

  class Function implements UnivariateFunction {
    public double value(double x) {
      return x * x - 2 * x + 1;
    }
  }

  class ConstantFunction implements UnivariateFunction {
    public double value(double x) {
      return 1;
    }
  }

  class LinearFunction implements UnivariateFunction {
    public double value(double x) {
      return 2 * x - 3;
    }
  }

  class QuadraticFunction implements UnivariateFunction {
    public double value(double x) {
      return x * x - x;
    }
  }

  class CubicFunction implements UnivariateFunction {
    public double value(double x) {
      return x * x * x - 3 * x * x + x;
    }
  }

  class ExponentialFunction implements UnivariateFunction {
    public double value(double x) {
      return Math.exp(x);
    }
  }
}