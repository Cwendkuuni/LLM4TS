import org.junit.Test;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Typical use case
        double[][] matrixData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        // Verify that the root matrix has been correctly initialized
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][] {
            {2, 0, 0},
            {6, 1, 0},
            {-8, 5, 3}
        });
        assertEquals(expectedRoot, decomposition.getRootMatrix());

        // Verify that the rank has been correctly calculated
        assertEquals(3, decomposition.getRank());

        // Edge case
        double[][] edgeMatrixData = {
            {1, 0, 0},
            {0, 2, 0},
            {0, 0, 0}
        };
        RealMatrix edgeMatrix = MatrixUtils.createRealMatrix(edgeMatrixData);
        RectangularCholeskyDecomposition edgeDecomposition = new RectangularCholeskyDecomposition(edgeMatrix, 1e-10);
        
        // Verify that the root matrix has been correctly initialized
        RealMatrix expectedEdgeRoot = MatrixUtils.createRealMatrix(new double[][] {
            {1, 0, 0},
            {0, Math.sqrt(2), 0},
            {0, 0, 0}
        });
        assertEquals(expectedEdgeRoot, edgeDecomposition.getRootMatrix());

        // Verify that the rank has been correctly calculated
        assertEquals(3, edgeDecomposition.getRank());

        // Error scenario - Non positive definite matrix
        double[][] errorMatrixData = {
            {1, 0, 0},
            {0, -1, 0},
            {0, 0, 1}
        };
        RealMatrix errorMatrix = MatrixUtils.createRealMatrix(errorMatrixData);
        try {
            new RectangularCholeskyDecomposition(errorMatrix, 1e-10);
            fail("Expected exception NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}