import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstructor() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3, new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });
 RealMatrix actualWeightMatrix = weight.getWeight();
 assertArrayEquals("Weight matrix is not as expected.", expectedWeightMatrix.getData(), actualWeightMatrix.getData(), 0.0);
 }

 @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
 public void testWeightNonSquareConstructorThrowsException() {
 new Weight(new RealMatrix(new double[][] {{1.0, 2.0}, {2.0, 3.0}}));
 }

 @Test
 public void testWeightCopyConstructor() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3, new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });
 Weight weight = new Weight(weightMatrix);
 RealMatrix expectedWeightMatrix = weightMatrix.copy();
 RealMatrix actualWeightMatrix = weight.getWeight();
 assertArrayEquals("Weight matrix is not as expected.", expectedWeightMatrix.getData(), actualWeightMatrix.getData(), 0.0);
 }

 @Test
 public void testWeightGetWeight() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3, new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });
 RealMatrix actualWeightMatrix = weight.getWeight();
 assertArrayEquals("Weight matrix is not as expected.", expectedWeightMatrix.getData(), actualWeightMatrix.getData(), 0.0);
 }

 @Test
 public void testWeightToString() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 String expectedWeightToString = "Weight{weightMatrix=WeightMatrix{data=[[1.0], [0.0], [0.0]], rowDimension=3, columnDimension=3}}";
 String actualWeightToString = weight.toString();
 assertEquals("toString method does not return the expected value.", expectedWeightToString, actualWeightToString);
 }

}