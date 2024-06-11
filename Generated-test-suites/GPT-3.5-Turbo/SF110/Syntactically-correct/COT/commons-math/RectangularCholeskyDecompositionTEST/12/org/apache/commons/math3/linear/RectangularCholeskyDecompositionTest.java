
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] matrixData = {
                {9, 1, 1},
                {1, 5, 1},
                {1, 2, 4}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{3.0, 0.1767766952966369}, {0.3333333333333333, 2.0191482288485516}, {0.3333333333333333, 0.9897433186107874}});
        assertEquals(expected, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        assertEquals(2, decomposition.getRank());
    }
}
