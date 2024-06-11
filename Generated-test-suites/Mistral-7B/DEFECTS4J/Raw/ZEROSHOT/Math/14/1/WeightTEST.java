import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testDiagonalWeightConstructor() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });
 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightConstructor() {
 new Weight(new double[] {1.0, 2.0});
 }

 @Test
 public void testWeightConstructor() {
 RealMatrix weight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0},
 {0.0, 2.0}
 });
 Weight actualWeight = new Weight(weight);
 RealMatrix expectedWeight = weight.copy();
 RealMatrix actualDiagonal = actualWeight.getWeight();
 RealMatrix expectedDiagonal = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0},
 {0.0, 2.0}
 });
 assertEquals(expectedDiagonal, actualDiagonal);
 assertEquals(expectedWeight, actualWeight.getWeight());
 }

 @Test
 public void testGetWeight() {
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
 public void testSingularValueDecomposition() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix weightMatrix = weight.getWeight();
 SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrix);
 RealMatrix expectedDiagonal = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });
 RealMatrix actualDiagonal = svd.getDiagonalMatrix();
 assertEquals(expectedDiagonal, actualDiagonal);
 }

 @Test
 public void testEquals() {
 Weight weight1 = new Weight(new double[] {1.0, 2.0, 3.0});
 Weight weight2 = new Weight(new double[] {1.0, 2.0, 3.0});
 Weight weight3 = new Weight(new double[] {1.1, 2.1, 3.1});
 assertTrue(weight1.equals(weight1));
 assertTrue(weight1.equals(weight2));
 assertFalse(weight1.equals(weight3));
 }

 @Test
 public void testHashCode() {
 Weight weight1 = new Weight(new double[] {1.0, 2.0, 3.0});
 Weight weight2 = new Weight(new double[] {1.0, 2.0, 3.0});
 Weight weight3 = new Weight(new double[] {1.1, 2.1, 3.1});
 int hashCode1 = weight1.hashCode();
 int hashCode2 = weight2.hashCode();
 int hashCode3 = weight3.hashCode();
 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 }
}