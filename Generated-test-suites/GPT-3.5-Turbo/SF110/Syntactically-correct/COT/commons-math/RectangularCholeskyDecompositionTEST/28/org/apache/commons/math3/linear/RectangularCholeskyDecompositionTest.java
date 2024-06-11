import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        try {
            this.decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Failed to create decomposition.");
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(3, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        double[][] matrixData = {{-4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition invalidDecomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException to be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(e.getMessage().contains("Matrix is not positive definite"));
        }
    }

}