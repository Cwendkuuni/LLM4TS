import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.WeightException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class WeightTest {

    @Test
    public void testWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, weight);
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 2.0}
        });
        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight;
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyWeightArray() {
        assertThrows(IllegalArgumentException.class, () -> new Weight(new double[0]));
    }

    @Test
    public void testEmptyWeightMatrix() {
        assertThrows(WeightException.class, () -> new Weight(RealMatrixUtils.createEmptyMatrix(3, 3)));
    }

    @Test
    public void testNonSquareWeightMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 2.0},
            {0.0, 3.0}
        });
        assertThrows(WeightException.class, () -> new Weight(weight));
    }

    @Test
    public void testNegativeWeights() {
        double[] weight = {-1.0, 2.0, -3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, weight);
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = WeightException.class)
    public void testNegativeWeightsException() {
        double[] weight = {-1.0, 2.0, -3.0};
        Weight weightObj = new Weight(weight);
        weightObj.setEntry(0, 0, -2.0);
    }
}