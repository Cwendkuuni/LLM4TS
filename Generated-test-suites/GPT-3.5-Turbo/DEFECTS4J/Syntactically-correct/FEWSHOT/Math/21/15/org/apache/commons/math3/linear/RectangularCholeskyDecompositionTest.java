import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setup() {
        double[][] data = {
            {2.0, -1.0, 0.0, 0.0},
            {-1.0, 2.0, -1.0, 0.0},
            {0.0, -1.0, 2.0, -1.0},
            {0.0, 0.0, -1.0, 2.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-15);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Failed to decompose matrix: " + e.getMessage());
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(4, rootMatrix.getRowDimension());
        assertEquals(4, rootMatrix.getColumnDimension());
        assertTrue(rootMatrix.getEntry(0, 0) > 0);
        assertTrue(rootMatrix.getEntry(1, 1) > 0);
        assertTrue(rootMatrix.getEntry(2, 2) > 0);
        assertTrue(rootMatrix.getEntry(3, 3) > 0);
    }

    @Test
    public void testGetRank() {
        assertEquals(4, decomposition.getRank());
    }
}