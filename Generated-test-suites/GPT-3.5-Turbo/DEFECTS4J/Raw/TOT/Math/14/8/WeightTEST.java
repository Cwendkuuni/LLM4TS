import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testDiagonalWeightMatrix() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actual = w.getWeight();

        assertEquals(expected, actual);
        assertFalse(expected == actual);
    }

    @Test
    public void testSquareWeightMatrix() {
        double[][] weightArray = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix weightMatrix = MatrixUtils.createRealMatrix(weightArray);
        Weight w = new Weight(weightMatrix);

        RealMatrix expected = new Array2DRowRealMatrix(weightArray);
        RealMatrix actual = w.getWeight();

        assertEquals(expected, actual);
        assertFalse(expected == actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMatrix() {
        double[][] weightArray = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix weightMatrix = MatrixUtils.createRealMatrix(weightArray);
        Weight w = new Weight(weightMatrix);
    }
}