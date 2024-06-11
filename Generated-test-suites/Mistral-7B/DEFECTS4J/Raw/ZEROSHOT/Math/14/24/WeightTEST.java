import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixWriter;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstruction() {
 double[] weightDiagonal = new double[] { 1.0, 2.0, 3.0 };
 Weight weightDiagonalInstance = new Weight(weightDiagonal);
 RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });

 assertEquals(expectedWeight, weightDiagonalInstance.getWeight());
 }

 @Test
 public void testWeightSquareMatrixConstruction() {
 RealMatrix weightSquareMatrix = new Array2DRowRealMatrix(3, 3, new double[][] {
 {1.0, 1.0, 1.0},
 {1.0, 1.0, 1.0},
 {1.0, 1.0, 1.0}
 });
 Weight weightSquareMatrixInstance = new Weight(weightSquareMatrix);
 RealMatrix expectedWeight = weightSquareMatrix.copy();

 assertEquals(expectedWeight, weightSquareMatrixInstance.getWeight());
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testWeightNonSquareMatrixConstruction() {
 RealMatrix weightNonSquareMatrix = new Array2DRowRealMatrix(2, 3, new double[][] {
 {1.0, 1.0},
 {1.0, 1.0, 1.0}
 });

 Weight weightNonSquareMatrixInstance = new Weight(weightNonSquareMatrix);
 }

 @Test
 public void testWeightGetWeight() {
 Weight weightInstance = new Weight(new Array2DRowRealMatrix(3, 3, new double[][] {
 {1.0, 1.0, 1.0},
 {1.0, 1.0, 1.0},
 {1.0, 1.0, 1.0}
 }));
 RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, new double[][] {
 {1.0, 1.0, 1.0},
 {1.0, 1.0, 1.0},
 {1.0, 1.0, 1.0}
 });

 RealMatrix actualWeight = weightInstance.getWeight();

 assertEquals(expectedWeight, actualWeight);
 }
}