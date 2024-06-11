import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WeightTest {

 @Test
 public void testDiagonalWeightConstruction() {
 double[] weights = new double[] {1.0, 2.0, 3.0};
 Weight weightObj = new Weight(weights);
 RealMatrix expectedWeight = RealMatrixUtils.diagonalMatrix(weights);
 RealMatrix actualWeight = weightObj.getWeight();

 assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
 assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
 assertArrayEquals(expectedWeight.getData(), actualWeight.getData());
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightConstruction() {
 double[][] weights = new double[][] {{1.0, 2.0}, {2.0, 3.0}};
 Weight weightObj = new Weight(weights);
 }

 @Test
 public void testWeightMatrixConstruction() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 2.0}});
 Weight weightObj = new Weight(weightMatrix);
 RealMatrix expectedWeight = weightMatrix.copy();
 RealMatrix actualWeight = weightObj.getWeight();

 assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
 assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
 assertArrayEquals(expectedWeight.getData(), actualWeight.getData());
 }
}