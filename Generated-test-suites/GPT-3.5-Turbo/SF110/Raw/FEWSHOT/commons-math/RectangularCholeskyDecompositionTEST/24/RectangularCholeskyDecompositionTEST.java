
package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test Case 1
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(new double[][] {
            {1, 2},
            {2, 5}
        });
        RectangularCholeskyDecomposition decomposition1 = new RectangularCholeskyDecomposition(matrix1);
        assertEquals(2, decomposition1.getRootMatrix().getRowDimension());
        assertEquals(2, decomposition1.getRootMatrix().getColumnDimension());
        assertEquals(2, decomposition1.getRank());
        
        // Test Case 2
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(new double[][] {
            {1, 0, 0},
            {0, 4, 0},
            {0, 0, 9}
        });
        RectangularCholeskyDecomposition decomposition2 = new RectangularCholeskyDecomposition(matrix2);
        assertEquals(3, decomposition2.getRootMatrix().getRowDimension());
        assertEquals(3, decomposition2.getRootMatrix().getColumnDimension());
        assertEquals(3, decomposition2.getRank());
        
        // Additional Test Cases...
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {-1, 0},
            {0, 1}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
}
