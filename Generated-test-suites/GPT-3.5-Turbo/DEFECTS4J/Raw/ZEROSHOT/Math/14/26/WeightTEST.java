import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeightTest {

    private Weight weight;

    @Before
    public void setup() {
        double[] diagonal = { 1.0, 2.0, 3.0 };
        weight = new Weight(diagonal);
    }

    @After
    public void tearDown() {
        weight = null;
    }

    @Test
    public void testConstructorWithDoubleArray() {
        double[] diagonal = { 1.0, 2.0, 3.0 };
        Weight weight = new Weight(diagonal);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 },
                { 0.0, 2.0, 0.0 }, { 0.0, 0.0, 3.0 } });

        assertArrayEquals(expectedMatrix.getData(), weight.getWeight().getData());
    }

    @Test
    public void testConstructorWithRealMatrix() {
        double[][] data = { { 1.0, 0.0, 0.0 }, { 0.0, 2.0, 0.0 }, { 0.0, 0.0, 3.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        Weight weight = new Weight(matrix);

        assertArrayEquals(matrix.getData(), weight.getWeight().getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithInvalidRealMatrix() {
        double[][] data = { { 1.0, 0.0 }, { 0.0, 2.0 }, { 0.0, 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        new Weight(matrix);
    }
}