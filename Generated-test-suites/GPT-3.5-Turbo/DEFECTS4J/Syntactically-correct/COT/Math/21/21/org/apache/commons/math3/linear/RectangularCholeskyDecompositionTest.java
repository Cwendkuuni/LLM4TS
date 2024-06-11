
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RectangularCholeskyDecompositionTest {

    private static final double EPSILON = 1e-10;
    private RealMatrix positiveDefiniteMatrix;
    private RealMatrix nonPositiveDefiniteMatrix;

    @Before
    public void setUp() {
        // Create a positive definite matrix for testing
        // (e.g. [[4, 1], [1, 3]])
        positiveDefiniteMatrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 3}});

        // Create a non-positive definite matrix for testing
        // (e.g. [[-4, 2], [2, -3]])
        nonPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(new double[][]{{-4, 2}, {2, -3}});
    }

    @Test
    public void testConstructor_PositiveDefiniteMatrix() {
        RectangularCholeskyDecomposition decomposition =
                new RectangularCholeskyDecomposition(positiveDefiniteMatrix, EPSILON);

        // Check that the root matrix can be retrieved
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());

        // Check that the rank can be retrieved
        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrix() {
        new RectangularCholeskyDecomposition(nonPositiveDefiniteMatrix, EPSILON);
    }

    @Test
    public void testGetRootMatrix_PositiveDefiniteMatrix() {
        RectangularCholeskyDecomposition decomposition =
                new RectangularCholeskyDecomposition(positiveDefiniteMatrix, EPSILON);

        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank_PositiveDefiniteMatrix() {
        RectangularCholeskyDecomposition decomposition =
                new RectangularCholeskyDecomposition(positiveDefiniteMatrix, EPSILON);

        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }
}
