import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import static org.junit.Assert.*;

public class WeightTest {

    // Test case for constructor with double array
    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix result = w.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertEquals(expected, result);
    }

    // Test case for constructor with invalid square matrix
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithInvalidMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 2);
        new Weight(weight);
    }

    // Test case for getWeight method
    @Test
    public void testGetWeight() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix result = w.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertEquals(expected, result);
    }

}