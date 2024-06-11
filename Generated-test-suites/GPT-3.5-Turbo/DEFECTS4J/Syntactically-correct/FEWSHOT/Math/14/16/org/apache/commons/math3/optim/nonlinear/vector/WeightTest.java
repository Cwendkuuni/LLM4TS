import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {
    @Test
    public void testConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        Array2DRowRealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertTrue(w.getWeight().equals(expected));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithMatrixThrowsExceptionForNonSquareMatrix() {
        double[][] weight = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(weight);
        new Weight(matrix);
    }

    @Test
    public void testConstructorWithMatrix() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(weight);
        Weight w = new Weight(matrix);
        assertTrue(w.getWeight().equals(matrix));
    }
}