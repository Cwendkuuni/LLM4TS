import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WeightTest {

 @Test
 public void testDiagonalWeightConstruction() {
 double[] weights = {1.0, 2.0, 3.0};
 Weight weightObj = new Weight(weights);
 RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
 RealMatrix actual = weightObj.getWeight();
 assertEquals(expected, actual);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonDiagonalWeightConstructionThrowsException() {
 double[][] nonSquareWeights = { {1.0, 2.0}, {3.0, 4.0} };
 Weight weightObj = new Weight(new Array2DRowRealMatrix(nonSquareWeights));
 }

 @Test
 public void testWeightMatrixCopy() {
 double[] weights = {1.0, 2.0, 3.0};
 Weight weightObj = new Weight(weights);
 RealMatrix expected = new Array2DRowRealMatrix(weights);
 RealMatrix actual = weightObj.getWeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testWeightWithNullInput() {
 assertNull(new Weight(null));
 }
}