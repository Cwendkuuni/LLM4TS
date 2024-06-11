import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testDiagonalWeightMatrixConstructor() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        double[][] expectedMatrixData = {
            {1.0, 0.0, 0.0},
            {0.0, 2.0, 0.0},
            {0.0, 0.0, 3.0}
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedMatrixData);
        RealMatrix actualMatrix = weightObj.getWeight();
        assertTrue(actualMatrix.isEqual(expectedMatrix));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixConstructor() {
        double[][] weightData = {
            {1.0, 0.0, 0.0},
            {0.0, 2.0, 0.0}
        };
        RealMatrix weightMatrix = MatrixUtils.createRealMatrix(weightData);
        Weight weightObj = new Weight(weightMatrix);
    }

    @Test
    public void testCopyConstructor() {
        double[][] weightData = {
            {1.0, 0.0, 0.0},
            {0.0, 2.0, 0.0},
            {0.0, 0.0, 3.0}
        };
        RealMatrix weightMatrix = MatrixUtils.createRealMatrix(weightData);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix actualMatrix = weightObj.getWeight();
        assertTrue(actualMatrix.isEqual(weightMatrix));
        assertFalse(actualMatrix == weightMatrix);
    }
}