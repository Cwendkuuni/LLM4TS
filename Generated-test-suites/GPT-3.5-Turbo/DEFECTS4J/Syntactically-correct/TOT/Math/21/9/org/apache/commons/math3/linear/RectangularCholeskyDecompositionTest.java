import static org.junit.Assert.*;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;
    private RealMatrix matrix;
    private double small;

    @Before
    public void setUp() {
        // Initialize test values
        // Set up a 5x5 matrix
        double[][] data = {{4, 1, 1, 1, 1},
                           {1, 3, 1, 1, 1},
                           {1, 1, 2, 1, 1},
                           {1, 1, 1, 1, 1},
                           {1, 1, 1, 1, 2}};
        matrix = MatrixUtils.createRealMatrix(data);
        small = 1e-6;
    }

    @Test
    public void testRootMatrix() {
        decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        // Verify dimensions of root matrix
        assertEquals(5, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());

        // Verify the values of the root matrix
        double[][] expectedData = {{2.0, 0.0, 0.0},
                                   {0.5, 1.3228756555322954, 0.0},
                                   {0.5, -0.33251445405152406, 1.527525231651946},
                                   {0.5, -0.33251445405152406, -0.5660227456004534},
                                   {0.5, -0.33251445405152406, -0.5660227456004534}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(MatrixUtils.isEqual(expectedMatrix, rootMatrix, 1e-6));
    }

    @Test
    public void testRank() {
        decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();
        // Verify rank of the matrix
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        // Set up a non positive definite matrix
        double[][] data = {{4, 1, 1, 1, 1},
                           {1, 3, 1, 1, 1},
                           {1, 1, -2, 1, 1},
                           {1, 1, 1, 1, 1},
                           {1, 1, 1, 1, 2}};
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testSmallDiagonalElements() {
        // Set up a 6x6 matrix with small diagonal elements
        double[][] data = {{1e-9, 0, 0, 0, 0, 0},
                           {0, 2e-6, 0, 0, 0, 0},
                           {0, 0, 3e-5, 0, 0, 0},
                           {0, 0, 0, 4e-4, 0, 0},
                           {0, 0, 0, 0, 5e-3, 0},
                           {0, 0, 0, 0, 0, 6e-2}};
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        // Verify dimensions of root matrix
        assertEquals(6, rootMatrix.getRowDimension());
        assertEquals(6, rootMatrix.getColumnDimension());

        // Verify diagonal elements of the root matrix
        double[] expectedDiagonalElements = {1e-5, 1e-4, 1e-3, 1e-2, 1e-1, 0.6};
        for (int i = 0; i < expectedDiagonalElements.length; i++) {
            assertEquals(expectedDiagonalElements[i], rootMatrix.getEntry(i, i), 1e-6);
        }
    }
}