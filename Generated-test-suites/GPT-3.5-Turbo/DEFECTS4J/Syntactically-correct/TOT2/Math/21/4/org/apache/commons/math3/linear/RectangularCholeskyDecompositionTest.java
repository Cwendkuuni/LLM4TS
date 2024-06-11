package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test constructor with valid input
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 5}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        assertNotNull(decomposition);
        assertEquals(2, decomposition.getRank());
        assertEquals(matrix, decomposition.getRootMatrix().multiply(decomposition.getRootMatrix().transpose()));

        // Test constructor with invalid input
        RealMatrix invalidMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}});
        try {
            RectangularCholeskyDecomposition invalidDecomposition = new RectangularCholeskyDecomposition(invalidMatrix, 1e-10);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Test exception handling
            assertEquals(0, e.getProblemIndex());
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 5}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1}, {1, 5}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        assertEquals(2, decomposition.getRank());
    }

}