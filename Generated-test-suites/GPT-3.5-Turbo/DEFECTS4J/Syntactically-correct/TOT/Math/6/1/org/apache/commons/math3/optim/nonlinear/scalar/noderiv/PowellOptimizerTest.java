import org.junit.Assert;
import org.junit.Test;

public class PowellOptimizerTest {

  @Test
  public void testDoOptimize_typicalCase() {
    // Arrange
    double[] startPoint = {1, 2, 3};
    double relTolerance = 1e-5;
    double absTolerance = 1e-8;
    PowellOptimizer optimizer = new PowellOptimizer(relTolerance, absTolerance);

    // Act
    PointValuePair result = optimizer.doOptimize();

    // Assert
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getPoint());
    Assert.assertNotNull(result.getValue());
    Assert.assertArrayEquals(result.getPoint(), new double[]{}, absTolerance);
  }

  @Test
  public void testDoOptimize_edgeCase() {
    // Arrange
    double[] startPoint = {0, 0, 0};
    double relTolerance = Double.MIN_VALUE;
    double absTolerance = Double.MIN_VALUE;
    PowellOptimizer optimizer = new PowellOptimizer(relTolerance, absTolerance);

    // Act
    PointValuePair result = optimizer.doOptimize();

    // Assert
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getPoint());
    Assert.assertNotNull(result.getValue());
    Assert.assertArrayEquals(result.getPoint(), new double[]{}, absTolerance);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testPowellOptimizer_invalidAbsThreshold() {
    // Arrange
    double relTolerance = 1e-5;
    double absTolerance = -1;
    
    // Act
    PowellOptimizer optimizer = new PowellOptimizer(relTolerance, absTolerance); 

    // Assert (expected exception)
  }

}