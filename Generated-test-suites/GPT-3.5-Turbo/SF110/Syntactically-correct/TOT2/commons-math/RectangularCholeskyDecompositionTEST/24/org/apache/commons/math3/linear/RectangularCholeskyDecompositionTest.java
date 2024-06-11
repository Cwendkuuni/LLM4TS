package org.apache.commons.math3.linear;
import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test constructor with valid input matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomposition);
        
        // Test constructor with invalid input matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{-1, 2}, {2, 3}});
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Exception not thrown for non positive definite matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Success
        }
    }

    @Test
    public void testGetRootMatrix() {
        // Test getRootMatrix method
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertNotNull(root);
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        // Test getRank method
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, decomposition.getRank());
    }
}