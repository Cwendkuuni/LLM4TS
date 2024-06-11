import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    // Test constructor with double[]
    @Test
    public void testConstructor_doubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        assertNotNull(w);
        assertArrayEquals(weight, w.getWeight().getData()[0], 0.0);
    }
    
    // Test constructor with RealMatrix
    @Test
    public void testConstructor_RealMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        Array2DRowRealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
        assertNotNull(w);
        assertArrayEquals(data, w.getWeight().getData(), 0.0);
    }
    
    // Test constructor with non-square matrix
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_NonSquareMatrixException() {
        double[][] data = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        Array2DRowRealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
    }
}