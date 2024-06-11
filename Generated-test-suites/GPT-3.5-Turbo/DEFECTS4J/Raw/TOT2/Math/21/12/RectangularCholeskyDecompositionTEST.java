import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {

    // Test for basic functionality of RectangularCholeskyDecomposition constructor
    @Test
    public void testRectangularCholeskyDecompositionConstructor() {
        double[][] data = {{4, 12}, {12, 37}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decompose = new RectangularCholeskyDecomposition(matrix, 1e-10);
            assertNotNull(decompose);
            assertEquals(2, decompose.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown");
        }
    }

    // Test for error scenario when passing a non-positive semidefinite matrix to the constructor
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{-1, 1}, {1, -1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decompose = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    // Test for typical use case scenario of getting the root matrix
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 12}, {12, 37}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decompose = new RectangularCholeskyDecomposition(matrix, 1e-10);
            RealMatrix rootMatrix = decompose.getRootMatrix();
            assertNotNull(rootMatrix);
            assertEquals(2, rootMatrix.getColumnDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown");
        }
    }

    // Test for typical use case scenario of getting the rank of the matrix
    @Test
    public void testGetRank() {
        double[][] data = {{4, 12}, {12, 37}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decompose = new RectangularCholeskyDecomposition(matrix, 1e-10);
            int rank = decompose.getRank();
            assertEquals(2, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown");
        }
    }

}