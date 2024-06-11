import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.WeightException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {
    @Test
    public void testWeightDiagonal() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightSquareMatrix() {
        RealMatrix weights = new Array2DRowRealMatrix(3, 3, new double[][]{{1.0, 0.1, 0.1}, {0.1, 2.0, 0.1}, {0.1, 0.1, 3.0}});
        Weight weight = new Weight(weights);
        RealMatrix expected = weights.copy();
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightDiagonalInvalidArrayLength() {
        double[] weights = new double[]{1.0};
        new Weight(weights);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightSquareMatrixFailure() {
        RealMatrix weights = new Array2DRowRealMatrix(3, 2, new double[][]{{1.0, 0.1}, {0.1, 2.0}, {0.1, 0.1}});
        new Weight(weights);
    }

    @Test(expected = NullPointerException.class)
    public void testWeightNullRealMatrix() {
        RealMatrix weights = null;
        new Weight(weights);
    }

    @Test
    public void testWeightNullCallback() {
        Weight weight = new Weight(RealMatrixUtils.identityMatrix(3));
        try {
            weight.getWeight(null);
        } catch (WeightException e) {
            fail("Weight should not throw a WeightException for a null Callback.");
        }
    }
}