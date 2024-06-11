package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 5}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        assertEquals(root, rcd.getRootMatrix());
        assertEquals(rank, rcd.getRank());
    }
    
    @Test
    public void testRectangularCholeskyDecompositionWithSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 5}});
        double small = 0.01;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(root, rcd.getRootMatrix());
        assertEquals(rank, rcd.getRank());
    }
    
    @Test(expected=NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1, 2}, {2, 5}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        fail("Expected NonPositiveDefiniteMatrixException");
    }
    
    @Test(expected=NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithNonPositiveDefiniteMatrixExceptionAndSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1, 2}, {2, 5}});
        double small = 0.01;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        fail("Expected NonPositiveDefiniteMatrixException");
    }
    
}