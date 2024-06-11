import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeightTest {

    @Test
    public void testDiagonalWeightMatrixCreation() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightMatrixCreation() {
        double[][] weights = new double[][]{{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weights);
        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight.copy();
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    // Test for getWeight method
    @Test
    public void testGetWeight() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
        assertNotNull(actual);
    }
}