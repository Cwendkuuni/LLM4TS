import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WeightTest {

 @Test
 public void testDiagonalWeightCreation() {
 double[] weight = {1.0, 2.0, 3.0};
 Weight weightObj = new Weight(weight);
 RealMatrix expectedWeight = MatrixUtils.diagonal(new double[] {1.0, 2.0, 3.0});
 RealMatrix actualWeight = weightObj.getWeight();
 assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
 assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
 assert(expectedWeight.equals(actualWeight));
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightCreation_Exception() {
 double[][] weight = {{1.0, 1.1}, {1.2, 1.3, 1.4}};
 Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
 }

 @Test
 public void testSquareWeightCreation() {
 double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
 RealMatrix weightObj = new Array2DRowRealMatrix(weight);
 Weight weightInst = new Weight(weightObj);
 RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
 RealMatrix actualWeight = weightInst.getWeight();
 assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
 assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
 assert(expectedWeight.equals(actualWeight));
 }

 @Test
 public void testSingularValueDecomposition() {
 double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
 Weight weightInst = new Weight(new Array2DRowRealMatrix(weight));
 RealMatrix weightMatrix = weightInst.getWeight();
 SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrix);
 RealMatrix expectedV = MatrixUtils.identityMatrix(2);
 RealMatrix actualV = svd.getV();
 RealVector expectedSingularValues = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}}).getColumnVector(0);
 RealVector actualSingularValues = svd.getSingularValues();
 assertEquals(expectedV.getRowDimension(), actualV.getRowDimension());
 assertEquals(expectedV.getColumnDimension(), actualV.getColumnDimension());
 assert(expectedV.equals(actualV));
 assertArrayEquals(expectedSingularValues.toArray(), actualSingularValues.toArray());
 }

 @Test
 public void testGetWeight() {
 Weight weightInst = new Weight(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}}));
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
 RealMatrix actualWeight = weightInst.getWeight();
 assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
 assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
 assert(expectedWeight.equals(actualWeight));
 }
}