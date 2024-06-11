import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testDiagonalWeightConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expected = RealMatrixUtils.diagonalMatrix(new double[] {1.0, 2.0, 3.0});
 RealMatrix actual = weight.getWeight();
 assertEquals(expected, actual);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0});
 }

 @Test
 public void testWeightMatrixCopy() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expected = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
 RealMatrix actual = weight.getWeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testWeightMatrixCopyFromAnotherWeight() {
 Weight weight1 = new Weight(new double[] {1.0, 2.0, 3.0});
 Weight weight2 = new Weight(weight1.getWeight());
 RealMatrix expected = weight1.getWeight();
 RealMatrix actual = weight2.getWeight();
 assertEquals(expected, actual);
 }
}