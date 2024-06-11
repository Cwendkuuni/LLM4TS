import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

  // Test case for Weight(double[] weight)
  @Test
  public void testWeightDoubleArray() {
    double[] weight = {1.0, 2.0, 3.0};
    Weight w = new Weight(weight);
    RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{
      {1.0, 0.0, 0.0},
      {0.0, 2.0, 0.0},
      {0.0, 0.0, 3.0}
    });
    assertArrayEquals(expectedMatrix.getData(), w.getWeight().getData());
  }

  // Test case for Weight(RealMatrix weight)
  @Test
  public void testWeightRealMatrix() {
    double[][] data = {
      {1.0, 0.0, 0.0},
      {0.0, 2.0, 0.0},
      {0.0, 0.0, 3.0}
    };
    RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
    Weight w = new Weight(weightMatrix);
    assertArrayEquals(weightMatrix.getData(), w.getWeight().getData());
  }

  // Test case for Weight(RealMatrix weight) with NonSquareMatrixException
  @Test(expected = NonSquareMatrixException.class)
  public void testWeightRealMatrixNonSquareException() {
    double[][] data = {
      {1.0, 0.0, 0.0},
      {0.0, 2.0, 0.0},
    };
    RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
    Weight w = new Weight(weightMatrix);
  }
}