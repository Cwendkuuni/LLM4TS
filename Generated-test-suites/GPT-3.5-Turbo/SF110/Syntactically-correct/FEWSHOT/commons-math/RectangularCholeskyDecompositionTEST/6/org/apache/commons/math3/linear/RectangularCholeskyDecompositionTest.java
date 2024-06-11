
package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testRectangularCholeskyDecomposition() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {
            {1.0, -2.0, 3.0},
            {-2.0, 5.0, -5.0},
            {3.0, -5.0, 7.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(3, decomposition.getRank());
        assertEquals(1.0, rootMatrix.getEntry(0,0), 0.0001);
        assertEquals(-0.5, rootMatrix.getEntry(0,1), 0.0001);
        assertEquals(3.0, rootMatrix.getEntry(0,2), 0.0001);
        assertEquals(-0.5, rootMatrix.getEntry(1,0), 0.0001);
        assertEquals(0.5, rootMatrix.getEntry(1,1), 0.0001);
        assertEquals(-1.0, rootMatrix.getEntry(1,2), 0.0001);
        assertEquals(1.0, rootMatrix.getEntry(2,0), 0.0001);
        assertEquals(-1.0, rootMatrix.getEntry(2,1), 0.0001);
        assertEquals(1.0, rootMatrix.getEntry(2,2), 0.0001);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {
            {1.0, -2.0, 3.0},
            {-2.0, 5.0, -5.0},
            {3.0, -5.0, -7.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
}
