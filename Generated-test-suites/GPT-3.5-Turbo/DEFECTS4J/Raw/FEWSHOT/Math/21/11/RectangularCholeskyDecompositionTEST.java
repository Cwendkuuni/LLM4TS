// Import necessary classes and methods
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.util.FastMath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecompose() {
        // Create test matrix
        double[][] testData = {
            {10.0, 0.0, 0.0},
            {0.0, 5.0, 0.0},
            {0.0, 0.0, 1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Perform decomposition
        double small = FastMath.pow(10, -8);
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);

        // Check the rank of the decomposed matrix
        int expectedRank = 3;
        int actualRank = decomposer.getRank();
        assertEquals(expectedRank, actualRank);

        // Check the root matrix of the decomposed matrix
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(testData);
        RealMatrix actualRootMatrix = decomposer.getRootMatrix();
        assertEquals(expectedRootMatrix, actualRootMatrix);
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        // Create test matrix with negative diagonal element
        double[][] testData = {
            {1.0, 0.0},
            {0.0, -1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Perform decomposition and check for exception
        double small = FastMath.pow(10, -8);
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);
            fail("NonPositiveDefiniteMatrixException was not thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception is expected
        }
    }
}