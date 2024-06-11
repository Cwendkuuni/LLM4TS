import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixTestCase;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class WeightTest {

 @Test
 public void testDiagonalWeightMatrixCreation() {
 RealMatrix weightMatrix;
 double[] weight = {1.0, 2.0, 3.0};
 Weight weightObj = new Weight(weight);
 weightMatrix = weightObj.getWeight();

 assertEquals(3, weightMatrix.getRowDimension());
 assertEquals(3, weightMatrix.getColumnDimension());

 for (int i = 0; i < weight.length; i++) {
 assertEquals(weight[i], weightMatrix.getEntry(i, i), 0.0);
 }
 }

 @Test
 public void testNonSquareWeightMatrixCreation() {
 RealMatrix weightMatrix;
 Weight weightObj;

 double[] weightDiagonal = {1.0, 2.0};
 weightObj = new Weight(weightDiagonal);
 weightMatrix = weightObj.getWeight();

 double[][] weightOffDiagonal = {{0.5, 0.1}, {0.2, 0.6}};
 RealMatrix expectedWeight = new Array2DRowRealMatrix(weightDiagonal, weightOffDiagonal);

 assertEquals(expectedWeight, weightMatrix);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightMatrixCreationException() {
 Weight weightObj = new Weight(new Array2DRowRealMatrix(new double[][]{
 {1.0, 2.0},
 {3.0, 4.0}
 }));
 }

 @Test
 public void testWeightMatrixCopy() {
 RealMatrix originalWeightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
 Weight weightObj = new Weight(originalWeightMatrix);
 RealMatrix copyWeightMatrix = weightObj.getWeight();

 assertEquals(originalWeightMatrix, copyWeightMatrix);
 }
}