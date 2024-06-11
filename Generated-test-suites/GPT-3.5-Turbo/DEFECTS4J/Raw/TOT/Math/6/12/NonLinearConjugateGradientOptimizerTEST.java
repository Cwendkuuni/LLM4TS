import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
  
  private NonLinearConjugateGradientOptimizer optimizer;
  private GoalType goal;
  private double[] startPoint;
  private int maxEvaluations;
  
  @Before
  public void setUp() {
    goal = GoalType.MINIMIZE;
    startPoint = new double[] {1.0, 2.0, 3.0};
    maxEvaluations = 1000;
    optimizer = new NonLinearConjugateGradientOptimizer(goal, startPoint, maxEvaluations);
  }
  
  // Test computeObjectiveValue()
  @Test
  public void testComputeObjectiveValue() {
    double[] variables = {1.0, 2.0, 3.0};
    double expected = 14.0;
    double result = optimizer.computeObjectiveValue(variables);
    assertEquals(expected, result, 0.0001);
  }
  
  // Test computeObjectiveGradient()
  @Test
  public void testComputeObjectiveGradient() {
    double[] variables = {1.0, 2.0, 3.0};
    double[] expected = {2.0, 2.0, 2.0};
    double[] result = optimizer.computeObjectiveGradient(variables);
    assertArrayEquals(expected, result, 0.0001);
  }
  
  // Test doOptimize()
  @Test
  public void testDoOptimize() {
    PointValuePair expected = new PointValuePair(new double[] {0.0, 1.0, 2.0}, 1.0);
    PointValuePair result = optimizer.doOptimize();
    assertArrayEquals(expected.getPoint(), result.getPoint(), 0.0001);
    assertEquals(expected.getValue(), result.getValue(), 0.0001);
  }
  
  // Test setInitialStep() with positive value
  @Test
  public void testSetInitialStepPositive() {
    optimizer.setInitialStep(2.0);
    assertEquals(2.0, optimizer.getInitialStep(), 0.0001);
  }
  
  // Test setInitialStep() with zero value
  @Test
  public void testSetInitialStepZero() {
    optimizer.setInitialStep(0.0);
    assertEquals(1.0, optimizer.getInitialStep(), 0.0001);
  }
  
  // Test setInitialStep() with negative value
  @Test
  public void testSetInitialStepNegative() {
    optimizer.setInitialStep(-1.0);
    assertEquals(1.0, optimizer.getInitialStep(), 0.0001);
  }
  
  // Test findUpperBound() when root is bracketed
  @Test
  public void testFindUpperBoundRootBracketed() {
    UnivariateFunction f = new UnivariateFunction() {
      public double value(double x) {
        return x * x - 1;
      }
    };
    double a = 0;
    double h = 0.5;
    double expected = 2.0;
    double result = optimizer.findUpperBound(f, a, h);
    assertEquals(expected, result, 0.0001);
  }
  
  // Test findUpperBound() when root cannot be bracketed
  @Test(expected = MathIllegalStateException.class)
  public void testFindUpperBoundRootNotBracketed() {
    UnivariateFunction f = new UnivariateFunction() {
      public double value(double x) {
        return x * x + 1;
      }
    };
    double a = 0;
    double h = 0.5;
    optimizer.findUpperBound(f, a, h);
  }
  
  // Test LineSearchFunction value()
  @Test
  public void testLineSearchFunctionValue() {
    double[] searchDirection = {1.0, 2.0, 3.0};
    NonLinearConjugateGradientOptimizer.LineSearchFunction function = optimizer.new LineSearchFunction(searchDirection);
    double x = 0.5;
    double expected = 14.5;
    double result = function.value(x);
    assertEquals(expected, result, 0.0001);
  }
  
}