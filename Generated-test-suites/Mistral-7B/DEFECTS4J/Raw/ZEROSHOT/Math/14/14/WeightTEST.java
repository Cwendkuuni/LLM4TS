import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixTestCase;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WeightTest {

 @Test
 public void testDiagonalWeightMatrixCreation() {
 RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0}, {3.0}});
 Weight weight = new Weight(new double[]{1.0, 2.0, 3.0});
 RealMatrix actualMatrix = weight.getWeight();
 RealMatrixTestCase.assertEquals(expectedMatrix, actualMatrix);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightMatrixCreation() {
 Weight weight = new Weight(new double[]{1.0, 2.0});
 }

 @Test
 public void testWeightMatrixCreation() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 1.0}});
 Weight weight = new Weight(weightMatrix);
 RealMatrix actualMatrix = weight.getWeight();
 RealMatrixTestCase.assertEquals(weightMatrix, actualMatrix);
 }

 @Test
 public void testGetWeightNullWeightMatrix() {
 Weight weight = new Weight(null);
 assertNull(weight.getWeight());
 }
}