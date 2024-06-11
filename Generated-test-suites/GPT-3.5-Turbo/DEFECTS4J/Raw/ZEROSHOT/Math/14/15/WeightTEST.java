import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.*;

public class WeightTest {

  @Test
  public void testWeightConstructor1() {
    double[] weight = {1.0, 2.0, 3.0};
    Weight w = new Weight(weight);
    RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                  {0.0, 2.0, 0.0},
                                                                  {0.0, 0.0, 3.0}});
    assertTrue(expected.equals(w.getWeight()));
  }

  @Test
  public void testWeightConstructor2() {
    double[][] matrixData = {{1.0, 0.0}, {0.0, 1.0}};
    RealMatrix weightMatrix = new Array2DRowRealMatrix(matrixData);
    Weight w = new Weight(weightMatrix);
    assertTrue(weightMatrix.equals(w.getWeight()));
  }

  @Test(expected = NonSquareMatrixException.class)
  public void testWeightConstructor2_NonSquareMatrixException() {
    double[][] matrixData = {{1.0, 0.0}, {1.0, 0.0}, {0.0, 1.0}};
    RealMatrix weightMatrix = new Array2DRowRealMatrix(matrixData);
    Weight w = new Weight(weightMatrix);
  }

}