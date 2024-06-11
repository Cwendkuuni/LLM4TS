import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optim.InitialGuess;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WeightTest {

 @Test
 public void testDiagonalWeightConstruction() {
 double[] weights = {1.0, 2.0, 3.0};
 Weight weightInstance = new Weight(weights);
 RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3);
 expectedWeightMatrix.setEntry(0, 0, 1.0);
 expectedWeightMatrix.setEntry(1, 1, 2.0);
 expectedWeightMatrix.setEntry(2, 2, 3.0);

 assertEquals("Weight matrix is not as expected.", expectedWeightMatrix, weightInstance.getWeight());
 }

 @Test
 public void testDiagonalWeightSettersAndGetters() {
 double[] weights = {1.0, 2.0, 3.0};
 Weight weightInstance = new Weight(weights);

 double weightValue = 0.5;
 weightInstance.setWeight(2, 2, weightValue);

 RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3);
 expectedWeightMatrix.setEntry(0, 0, 1.0);
 expectedWeightMatrix.setEntry(1, 1, 2.5);
 expectedWeightMatrix.setEntry(2, 2, 3.0);

 assertEquals("Weight matrix is not as expected after setting a value.", expectedWeightMatrix, weightInstance.getWeight());
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightConstruction() {
 double[][] weights = {{1.0, 2.0}, {3.0, 4.0, 5.0}};
 RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
 Weight weightInstance = new Weight(weightMatrix);
 }

 @Test
 public void testWeightSettersAndGetters() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3);
 weightMatrix.setEntry(0, 0, 1.0);
 weightMatrix.setEntry(1, 1, 2.0);
 weightMatrix.setEntry(2, 2, 3.0);
 weightMatrix.setEntry(1, 2, 0.5);
 Weight weightInstance = new Weight(weightMatrix);

 RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3);
 expectedWeightMatrix.setEntry(0, 0, 1.0);
 expectedWeightMatrix.setEntry(1, 1, 2.0);
 expectedWeightMatrix.setEntry(1, 2, 0.5);
 expectedWeightMatrix.setEntry(2, 2, 3.0);

 assertEquals("Weight matrix is not as expected.", expectedWeightMatrix, weightInstance.getWeight());
 }

 @Test
 public void testGetInitialGuess() {
 Weight weightInstance = new Weight(new Array2DRowRealMatrix(3, 3));
 assertNull("Weight should be null as it is an empty object.", weightInstance.getInitialGuess());
 }
}