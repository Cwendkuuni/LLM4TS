import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    // Test for Weight(double[] weight)
    @Test
    public void testWeightDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertArrayEquals(expected.getData(), w.getWeight().getData());
    }

    // Test for Weight(RealMatrix weight)
    @Test
    public void testWeightRealMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weightMatrix);
        assertArrayEquals(data, w.getWeight().getData());
    }

    // Test for Weight(RealMatrix weight) with non-square matrix
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightRealMatrixNonSquare() {
        double[][] data = {{1.0, 0.0}, {0.0, 2.0}, {3.0, 4.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weightMatrix);
    }
}