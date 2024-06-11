import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.InvalidMatrixException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.util.FastMath;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposer;

    @Before
    public void setUp() {
        // Test matrix
        double[][] matrixData = {
            { 1, 0 },
            { 0, 2 }
        };

        // Create the test matrix
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(matrixData);

        // Create the decomposer
        decomposer = new RectangularCholeskyDecomposition(testMatrix, 1e-15);
    }

    @Test
    public void testGetRootMatrix() {
        // Get the root matrix
        RealMatrix rootMatrix = decomposer.getRootMatrix();

        // Check dimensions
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(1, rootMatrix.getColumnDimension());

        // Check values
        assertEquals(FastMath.sqrt(1), rootMatrix.getEntry(0, 0), 1e-15);
        assertEquals(FastMath.sqrt(2), rootMatrix.getEntry(1, 0), 1e-15);
    }

    @Test
    public void testGetRank() {
        // Get the rank
        int rank = decomposer.getRank();

        // Check rank
        assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidMatrix() {
        // Test matrix
        double[][] invalidMatrixData = {
            { 1, -1 },
            { -1, 1 }
        };

        // Create the invalid matrix
        RealMatrix invalidMatrix = MatrixUtils.createRealMatrix(invalidMatrixData);

        // Try to create a decomposer with the invalid matrix
        new RectangularCholeskyDecomposition(invalidMatrix, 1e-15);
    }
}