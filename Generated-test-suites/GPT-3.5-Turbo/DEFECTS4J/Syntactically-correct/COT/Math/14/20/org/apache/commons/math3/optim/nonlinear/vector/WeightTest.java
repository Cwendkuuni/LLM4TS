import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;

import static org.junit.Assert.*;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() {
        double[] weightArr = {1.0, 2.0, 3.0};
        weight = new Weight(weightArr);
    }

    @Test
    public void testConstructor_doubleArray() {
        double[] weightArr = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightArr);

        RealMatrix result = weight.getWeight();

        assertEquals(result.getRowDimension(), 3);
        assertEquals(result.getColumnDimension(), 3);
        assertEquals(result.getEntry(0, 0), 1.0, 0.0);
        assertEquals(result.getEntry(1, 1), 2.0, 0.0);
        assertEquals(result.getEntry(2, 2), 3.0, 0.0);
    }

    @Test
    public void testConstructor_RealMatrix() {
        double[][] weightArr = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightArr);
        Weight weight = new Weight(weightMatrix);

        RealMatrix result = weight.getWeight();

        assertEquals(result.getRowDimension(), 3);
        assertEquals(result.getColumnDimension(), 3);
        assertEquals(result.getEntry(0, 0), 1.0, 0.0);
        assertEquals(result.getEntry(1, 1), 2.0, 0.0);
        assertEquals(result.getEntry(2, 2), 3.0, 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_InvalidMatrix() {
        double[][] weightArr = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightArr);
        Weight weight = new Weight(weightMatrix);
    }

    @Test
    public void testGetWeight() {
        RealMatrix result = weight.getWeight();

        assertEquals(result.getRowDimension(), 3);
        assertEquals(result.getColumnDimension(), 3);
        assertEquals(result.getEntry(0, 0), 1.0, 0.0);
        assertEquals(result.getEntry(1, 1), 2.0, 0.0);
        assertEquals(result.getEntry(2, 2), 3.0, 0.0);
    }
}