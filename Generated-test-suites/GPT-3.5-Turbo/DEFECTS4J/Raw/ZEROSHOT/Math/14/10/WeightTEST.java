import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() {
        double[] diag = {1.0, 2.0, 3.0};
        weight = new Weight(diag);
    }

    @Test
    public void testDiagonalMatrixConstructor() {
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });

        RealMatrix actual = weight.getWeight();

        assertEquals(expected, actual);
    }

    @Test
    public void testNonSquareMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0}
        });

        assertThrows(NonSquareMatrixException.class, () -> new Weight(matrix));
    }

    @Test
    public void testCopyConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        Weight newWeight = new Weight(matrix);

        RealMatrix expected = matrix.copy();
        RealMatrix actual = newWeight.getWeight();

        assertEquals(expected, actual);
        assertNotSame(matrix, actual);
    }
}