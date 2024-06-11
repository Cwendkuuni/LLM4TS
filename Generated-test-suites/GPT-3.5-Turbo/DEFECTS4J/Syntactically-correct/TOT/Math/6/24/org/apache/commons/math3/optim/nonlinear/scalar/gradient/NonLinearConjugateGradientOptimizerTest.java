import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optimization.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

  private NonLinearConjugateGradientOptimizer optimizer;

  @Before
  public void setup() {
    optimizer = new NonLinearConjugateGradientOptimizer();
  }

  @Test
  public void testDoOptimize_WithEmptyStartPoint_ShouldThrowException() {
    // Arrange
    double[] startPoint = new double[]{};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> FastMath.pow(x, 2));

    // Act and Assert
    try {
      optimizer.optimize(objectiveFunction, startPoint);
      Assert.fail("Expected MathIllegalArgumentException to be thrown");
    } catch (MathIllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDoOptimize_WithStartPointOutOfRange_ShouldThrowException() {
    // Arrange
    double[] startPoint = new double[]{-100.0};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> FastMath.pow(x, 2));

    // Act and Assert
    try {
      optimizer.optimize(objectiveFunction, startPoint);
      Assert.fail("Expected MathIllegalArgumentException to be thrown");
    } catch (MathIllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDoOptimize_WithObjectiveFunctionReturningNaN_ShouldThrowException() {
    // Arrange
    double[] startPoint = new double[]{0.0};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> Double.NaN);

    // Act and Assert
    try {
      optimizer.optimize(objectiveFunction, startPoint);
      Assert.fail("Expected MathIllegalArgumentException to be thrown");
    } catch (MathIllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDoOptimize_WithObjectiveFunctionReturningInfinity_ShouldThrowException() {
    // Arrange
    double[] startPoint = new double[]{0.0};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> Double.POSITIVE_INFINITY);

    // Act and Assert
    try {
      optimizer.optimize(objectiveFunction, startPoint);
      Assert.fail("Expected MathIllegalArgumentException to be thrown");
    } catch (MathIllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDoOptimize_WithObjectiveFunctionReturningNegativeInfinity_ShouldThrowException() {
    // Arrange
    double[] startPoint = new double[]{0.0};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> Double.NEGATIVE_INFINITY);

    // Act and Assert
    try {
      optimizer.optimize(objectiveFunction, startPoint);
      Assert.fail("Expected MathIllegalArgumentException to be thrown");
    } catch (MathIllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDoOptimize_WithObjectiveFunctionReturningNonNumericValue_ShouldThrowException() {
    // Arrange
    double[] startPoint = new double[]{0.0};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> "invalid");

    // Act and Assert
    try {
      optimizer.optimize(objectiveFunction, startPoint);
      Assert.fail("Expected MathIllegalArgumentException to be thrown");
    } catch (MathIllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDoOptimize_WithConvergingObjectiveFunction_ShouldReturnOptimalPointValuePair() {
    // Arrange
    double[] startPoint = new double[]{0.0};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> FastMath.pow(x, 2));

    // Act
    PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);

    // Assert
    Assert.assertEquals(0.0, result.getPoint()[0], 0.0001);
    Assert.assertEquals(0.0, result.getValue(), 0.0001);
  }

  @Test
  public void testDoOptimize_WithDivergingObjectiveFunction_ShouldThrowException() {
    // Arrange
    double[] startPoint = new double[]{10.0};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> FastMath.pow(x, 2));

    // Act and Assert
    try {
      optimizer.optimize(objectiveFunction, startPoint);
      Assert.fail("Expected MathIllegalArgumentException to be thrown");
    } catch (MathIllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDoOptimize_WithQuadraticObjectiveFunction_ShouldReturnOptimalPointValuePair() {
    // Arrange
    double[] startPoint = new double[]{-10.0};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> FastMath.pow(x, 2));

    // Act
    PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);

    // Assert
    Assert.assertEquals(0.0, result.getPoint()[0], 0.001);
    Assert.assertEquals(0.0, result.getValue(), 0.001);
  }

  @Test
  public void testDoOptimize_WithObjectiveFunctionAndMinimizeGoal_ShouldReturnOptimalPointValuePair() {
    // Arrange
    double[] startPoint = new double[]{-10.0};
    UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> FastMath.pow(x - 5, 2));

    // Set goal as MINIMIZE
    optimizer.setGoalType(GoalType.MINIMIZE);

    // Act
    PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);

    // Assert
    Assert.assertEquals(5.0, result.getPoint()[0], 0.001);
    Assert.assertEquals(0.0, result.getValue(), 0.001);
  }

}