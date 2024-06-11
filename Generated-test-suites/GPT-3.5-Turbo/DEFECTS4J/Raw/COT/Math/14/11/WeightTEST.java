
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;  

public class WeightTest {

  @Test
  public void testWeight_ConstructorWithArray() {
    double[] weightValues = {1, 2, 3, 4, 5};
    Weight weight = new Weight(weightValues);
    RealMatrix expectedMatrix = MatrixUtils.createRealDiagonalMatrix(weightValues);
    RealMatrix actualMatrix = weight.getWeight();
    Assert.assertEquals(expectedMatrix, actualMatrix);
  }
  
  @Test(expected = NonSquareMatrixException.class)
  public void testWeight_ConstructorWithMatrix() {
    double[][] matrixValues = {{1, 2}, {3, 4}};
    RealMatrix weightMatrix = MatrixUtils.createRealMatrix(matrixValues);
    new Weight(weightMatrix);
  }
  
}
