
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightTest {

    // Test for Weight(double[] weight)
    @Test
    public void testConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        Array2DRowRealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertArrayEquals(expected.getData(), w.getWeight().getData());
    }

    // Test for Weight(RealMatrix weight)
    @Test
    public void testConstructorWithMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 2.0}};
        Array2DRowRealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weightMatrix);
        assertArrayEquals(weightMatrix.getData(), w.getWeight().getData());
    }

    // Test for Weight(RealMatrix weight) with non-square matrix
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        Array2DRowRealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weightMatrix);
    }

}
