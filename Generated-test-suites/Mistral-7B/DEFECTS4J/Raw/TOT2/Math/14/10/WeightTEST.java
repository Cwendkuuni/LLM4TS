import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class WeightTest {
    @Test
    public void testWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 1, weight);
        RealMatrix actual = weightObj.getWeight();
        assertNotNull(actual);
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testWeightMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightObj = new Array2DRowRealMatrix(weight);
        Weight weightInstance = new Weight(weightObj);
        RealMatrix expected = weightObj.copy();
        RealMatrix actual = weightInstance.getWeight();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightDiagonalNullInput() {
        double[] weight = null;
        Weight weightObj = new Weight(weight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightMatrixNullInput() {
        RealMatrix weightObj = null;
        Weight weightInstance = new Weight(weightObj);
    }

    @Test
    public void testWeightDiagonalZeroEntries() {
        double[] weight = {};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(0, 0);
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightDiagonalNegativeEntries() {
        double[] weight = {-1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 1, weight);
        RealMatrix actual = weightObj.getWeight();
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixNonSquare() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}, {1.1, 2.1}};
        RealMatrix weightObj = new Array2DRowRealMatrix(weight);
        Weight weightInstance = new Weight(weightObj);
    }

    @Test
    public void testWeightDiagonalOddLength() {
        double[] weight = {1.0, 2.0, 3.0, 4.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(4, 1, weight);
        RealMatrix actual = weightObj.getWeight();
        assertArrayEquals(expected.getData(), actual.getData());
    }
}