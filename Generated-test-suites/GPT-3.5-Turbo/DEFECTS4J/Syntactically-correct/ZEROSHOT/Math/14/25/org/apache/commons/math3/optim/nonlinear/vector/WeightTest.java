import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.*;

public class WeightTest {
    private Weight weight;

    @Before
    public void setUp() {
        double[] diagonal = {1.0, 2.0, 3.0};
        weight = new Weight(diagonal);
    }

    @Test
    public void testGetWeight() {
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                     {0.0, 2.0, 0.0},
                                                                     {0.0, 0.0, 3.0}});
        RealMatrix actual = weight.getWeight();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        for (int i = 0; i < expected.getRowDimension(); i++) {
            for (int j = 0; j < expected.getColumnDimension(); j++) {
                assertEquals(expected.getEntry(i, j), actual.getEntry(i, j), 0.0);
            }
        }
    }

    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                          {0.0, 2.0}});
        Weight weight = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix.copy();
        RealMatrix actual = weight.getWeight();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        for (int i = 0; i < expected.getRowDimension(); i++) {
            for (int j = 0; j < expected.getColumnDimension(); j++) {
                assertEquals(expected.getEntry(i, j), actual.getEntry(i, j), 0.0);
            }
        }
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                          {0.0, 2.0},
                                                                          {0.0, 0.0}});
        new Weight(weightMatrix);
    }
}