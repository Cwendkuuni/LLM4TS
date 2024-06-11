import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WeightTest {

 @Test
 public void testDiagonalWeightConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });

 assertEquals(expectedWeight, weight.getWeight());
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonDiagonalWeightConstructionThrowsException() {
 Weight weight = new Weight(new double[][] {
 {1.0, 2.0},
 {2.0, 3.0}
 });
 }

 @Test
 public void testWeightMatrixCopy() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });

 RealMatrix actualWeight = weight.getWeight().copy();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test
 public void testWeightMatrixMultiplyVector() {
 Weight weight = new Weight(new double[] {1.0, 1.0, 1.0});
 RealVector vector = new Array2DRowRealVector(new double[] {1.0, 2.0, 3.0});
 RealVector result = weight.getWeight().operate(vector);

 double[] expectedResult = new double[] {1.0, 1.0, 1.0};
 assertArrayEquals(expectedResult, result.toArray());
 }

 @Test
 public void testSingularValueDecomposition() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0, 3.0, 3.0});
 RealMatrix weightMatrix = weight.getWeight();
 SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrix);

 assertEquals(weightMatrix, svd.getU().multiply(svd.getS()).multiply(svd.getVT()));
 }
}