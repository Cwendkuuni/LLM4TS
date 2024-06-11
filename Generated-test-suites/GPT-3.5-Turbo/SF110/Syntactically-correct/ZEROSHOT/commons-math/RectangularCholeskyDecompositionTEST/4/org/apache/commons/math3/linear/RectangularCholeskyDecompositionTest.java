
package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_ThrowsNonPositiveDefiniteMatrixException() {
        double[][] matrixData = {{-1, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testConstructor_ZeroSmall_ThrowsNonPositiveDefiniteMatrixException() {
        double[][] matrixData = {{1, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException not thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1.0, e.getOffendingValue(), 1e-10);
            assertEquals(0, e.getOffendingIndex());
            assertEquals(0.0, e.getSmall(), 1e-10);
        }
    }
    
    @Test
    public void testGetRootMatrix_ReturnsExpectedMatrix() {
        double[][] matrixData = {{4, -2}, {-2, 13}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertTrue(rootMatrix.isSquare());
        assertEquals(matrix.getColumnDimension(), rootMatrix.getRowDimension());
        assertEquals(matrix.getColumnDimension(), rootMatrix.getColumnDimension());
        assertEquals(2.0, rootMatrix.getEntry(0, 0), 1e-10);
        assertEquals(0.0, rootMatrix.getEntry(0, 1), 1e-10);
        assertEquals(-1.0, rootMatrix.getEntry(1, 0), 1e-10);
        assertEquals(3.0, rootMatrix.getEntry(1, 1), 1e-10);
    }
    
    @Test
    public void testGetRank_ReturnsExpectedRank() {
        double[][] matrixData = {{4, -2, 1}, {-2, 13, 5}, {1, 5, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(3, decomposition.getRank());
    }
}
