import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expected = RealMatrixUtils.diagonalMatrix(new double[] {1.0, 2.0, 3.0});
 RealMatrix actual = weight.getWeight();
 assertEquals(expected, actual);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testWeightNonSquareConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0, 4.0});
 }

 @Test
 public void testWeightMatrixConstruction() {
 RealMatrix weight = RealMatrixUtils.identityMatrix(3);
 Weight weightObj = new Weight(weight);
 RealMatrix expected = weight.copy();
 RealMatrix actual = weightObj.getWeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testWeightMatrixMultiplication() {
 RealMatrix weight = RealMatrixUtils.identityMatrix(3);
 Weight weightObj = new Weight(weight);
 RealMatrix vector = RealMatrixUtils.createColumnMatrix(new double[] {1.0, 2.0, 3.0});
 RealMatrix expected = weight.multiply(vector);
 RealMatrix actual = weightObj.getWeight().multiply(vector);
 assertEquals(expected, actual);
 }
}