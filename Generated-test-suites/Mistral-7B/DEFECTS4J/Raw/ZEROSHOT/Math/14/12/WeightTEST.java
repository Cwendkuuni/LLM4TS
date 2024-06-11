import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}});
 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test
 public void testWeightMatrixConstruction() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5},
 {0.5, 2.0}});
 Weight weight = new Weight(weightMatrix);
 RealMatrix expectedWeight = weightMatrix;
 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testWeightMatrixNonSquareConstruction() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5},
 {0.5, 2.0},
 {0.5, 3.0}});
 Weight weight = new Weight(weightMatrix);
 fail("Weight matrix construction should throw NonSquareMatrixException");
 }

 @Test
 public void testGettingWeight() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}});
 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);

 weight = new Weight(new Array2DRowRealMatrix(new double[][]{{1.0, 0.5},
 {0.5, 2.0}}));
 expectedWeight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5},
 {0.5, 2.0}});
 actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }
}