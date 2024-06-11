import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstructor() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });

 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test
 public void testWeightMatrixConstructor() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.5},
 {0.5, 2.0}
 });
 Weight weight = new Weight(weightMatrix);
 RealMatrix expectedWeight = weightMatrix.copy();

 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testWeightMatrixConstructorWithNonSquareMatrix() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.5},
 {0.5}
 });
 Weight weight = new Weight(weightMatrix);
 }

 @Test
 public void testSingularValueDecomposition() {
 Weight weight = new Weight(new double[] {1.0, 1.0, 1.0, 1.0});
 RealMatrix weightMatrix = weight.getWeight();
 SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrix);
 RealMatrix expectedDiagonal = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0, 0.0},
 {0.0, 1.0, 0.0, 0.0},
 {0.0, 0.0, 1.0, 0.0},
 {0.0, 0.0, 0.0, 1.0}
 });

 RealMatrix actualDiagonal = svd.getDiagonal();
 assertEquals(expectedDiagonal, actualDiagonal);
 }

 @Test
 public void testCopyWeightMatrix() {
 Weight weight = new Weight(new double[] {1.0, 1.0, 1.0, 1.0});
 RealMatrix expectedWeight = weight.getWeight();
 RealMatrix actualWeight = new Weight(weight.getWeight()).getWeight();

 assertEquals(expectedWeight, actualWeight);
 }
}