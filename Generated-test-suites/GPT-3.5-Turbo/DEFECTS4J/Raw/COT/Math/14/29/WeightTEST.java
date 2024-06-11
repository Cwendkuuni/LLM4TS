import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {
  
  @Test
  public void testWeightArray() {
    double[] weight = {1.0, 2.0, 3.0};
    Weight w = new Weight(weight);
    RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
    Assert.assertEquals(expected, w.getWeight());
  }
  
  @Test
  public void testWeightRealMatrix() {
    double[][] matrixData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
    RealMatrix weight = new Array2DRowRealMatrix(matrixData);
    Weight w = new Weight(weight);
    Assert.assertEquals(weight, w.getWeight());
  }
  
  @Test(expected = NonSquareMatrixException.class)
  public void testNonSquareMatrix() {
    double[][] matrixData = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
    RealMatrix weight = new Array2DRowRealMatrix(matrixData);
    Weight w = new Weight(weight);
  }
  
}