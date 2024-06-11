import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class WeightTest {

  @Test
  public void testConstructorWithArrayOfWeightValues() {
    double[] weights = {1.0, 2.0, 3.0};
    Weight weight = new Weight(weights);
    RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
      {1.0, 0.0, 0.0},
      {0.0, 2.0, 0.0},
      {0.0, 0.0, 3.0}
    });
    Assert.assertEquals(expected, weight.getWeight());
  }

  @Test
  public void testConstructorWithNonSquareMatrix() {
    double[][] weights = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
    RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
    try {
      Weight weight = new Weight(weightMatrix);
      Assert.fail("Expected NonSquareMatrixException");
    } catch(NonSquareMatrixException e) {
      // Expected exception
    }
  }

  @Test
  public void testConstructorWithSquareMatrix() {
    double[][] weights = {{1.0, 2.0}, {3.0, 4.0}};
    RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
    Weight weight = new Weight(weightMatrix);
    Assert.assertEquals(weightMatrix, weight.getWeight());
  }

}