import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    // Test case for constructor with double array input
    @Test
    public void testConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix weightMatrix = w.getWeight();
        double[] expected = {1.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 3.0};
        assertArrayEquals(expected, weightMatrix.getColumnPackedCopy(), 0.0);
    }

    // Test case for constructor with RealMatrix input
    @Test
    public void testConstructorWithRealMatrix() {
        double[][] array = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weight = new Array2DRowRealMatrix(array);
        Weight w = new Weight(weight);
        RealMatrix weightMatrix = w.getWeight();
        assertTrue(weightMatrix.equals(weight));
    }

    // Test case for constructor with non-square matrix
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] array = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix weight = new Array2DRowRealMatrix(array);
        Weight w = new Weight(weight);
    }
}