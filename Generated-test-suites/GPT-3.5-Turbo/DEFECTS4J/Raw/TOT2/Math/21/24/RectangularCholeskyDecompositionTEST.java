import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RectangularCholeskyDecompositionTest {

    private RealMatrix testMatrix;

    @Before
    public void setUp() {
        double[][] matrixData = {{4, 1, 1}, {1, 3, 2}, {1, 2, 3}};
        testMatrix = MatrixUtils.createRealMatrix(matrixData);
    }

    @Test
    public void testRectangularCholeskyDecompositionValidInput() {
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-10);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            // Assert that the root matrix is not null
            assertNotNull(rootMatrix);

            // Assert that the rank is calculated correctly
            assertEquals(3, rank);

        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRectangularCholeskyDecompositionInvalidInput() {
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 0.1);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    // Add more test cases for other methods if needed

}