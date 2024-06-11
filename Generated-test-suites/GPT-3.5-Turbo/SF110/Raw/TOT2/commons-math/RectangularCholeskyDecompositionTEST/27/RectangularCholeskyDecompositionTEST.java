import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.FastMath;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor_withSmallValue_exceptionThrown() {
        try {
            RealMatrix matrix = new RealMatrix(new double[][]{{-2.0}});
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch(NonPositiveDefiniteMatrixException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void testConstructor_withNonPositiveMatrix_exceptionThrown() {
        try {
            RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch(NonPositiveDefiniteMatrixException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new RealMatrix(new double[][]{{4.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertNotNull(root);
        assertEquals(4.0, root.getEntry(0, 0), 0.0001);
        assertEquals(1.0, root.getEntry(0, 1), 0.0001);
        assertEquals(1.0, root.getEntry(1, 0), 0.0001);
        assertEquals(1.0, root.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }
}