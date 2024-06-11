import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.*;

import static org.junit.Assert.*;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() {
        double[] weights = {1.0, 2.0, 3.0};
        weight = new Weight(weights);
    }

    @Test
    public void testGetWeight() {
        double[][] expected = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix actual = weight.getWeight();
        assertArrayEquals(expected, actual.getData());
    }

    @Test
    public void testConstructorWithMatrix() {
        double[][] matrixData = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        Weight weight = new Weight(matrix);

        RealMatrix expectedMatrix = matrix.copy();
        RealMatrix actualMatrix = weight.getWeight();

        assertEquals(expectedMatrix, actualMatrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] matrixData = {{1.0, 0.0}, {0.0, 1.0}, {1.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        Weight weight = new Weight(matrix);
    }
}