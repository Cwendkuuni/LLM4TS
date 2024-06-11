import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class WeightTest {

    @Test
    public void testWeightConstructorWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix actualMatrix = weight.getWeight();

        double[][] expectedArray = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedArray);

        assertTrue(expectedMatrix.equals(actualMatrix));
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        double[][] matrixData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        Weight weight = new Weight(matrix);
        RealMatrix actualMatrix = weight.getWeight();

        assertTrue(matrix.equals(actualMatrix));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] matrixData = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        Weight weight = new Weight(matrix);
    }

    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix actualMatrix = weight.getWeight();

        double[][] expectedArray = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedArray);

        assertTrue(expectedMatrix.equals(actualMatrix));
    }
}