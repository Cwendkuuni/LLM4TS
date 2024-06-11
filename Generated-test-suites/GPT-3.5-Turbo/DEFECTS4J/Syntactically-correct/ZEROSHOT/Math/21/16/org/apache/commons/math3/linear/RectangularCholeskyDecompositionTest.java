package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    private RealMatrix matrix;
    private double small;
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionNonPositiveDefiniteMatrixException() {
        // Initialize test input
        matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {2, 1}});
        small = 1e-10;

        // Perform the test
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Initialize test input
        matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {2, 5}});
        small = 1e-10;

        // Perform the test
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);

        // Check the result
        assertEquals(2, rcd.getRank());
        RealMatrix root = rcd.getRootMatrix();
        assertEquals(2, root.getColumnDimension());
        assertEquals(2, root.getRowDimension());
        assertEquals(1.0, root.getEntry(0, 0), 1e-10);
        assertEquals(2.0, root.getEntry(0, 1), 1e-10);
        assertEquals(0.0, root.getEntry(1, 0), 1e-10);
        assertEquals(1.0, root.getEntry(1, 1), 1e-10);
    }
    
}