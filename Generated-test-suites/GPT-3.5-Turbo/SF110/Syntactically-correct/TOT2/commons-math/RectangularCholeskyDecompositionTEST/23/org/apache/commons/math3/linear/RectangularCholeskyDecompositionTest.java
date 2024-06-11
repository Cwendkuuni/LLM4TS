package org.apache.commons.math3.linear;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {

    // Test case for RectangularCholeskyDecomposition constructor with non-positive definite matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_NonPositiveDefiniteMatrixException() {
        double[][] data = {{1.0, -2.0}, {-2.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
    }

    // Test case for getRootMatrix method
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4.0, 1.0, 2.0}, {1.0, 2.0, 3.0}, {2.0, 3.0, 11.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = cholesky.getRootMatrix();
        assertEquals(4.0, root.getEntry(0, 0), 0.0001);
        assertEquals(1.0, root.getEntry(1, 0), 0.0001);
        assertEquals(2.0, root.getEntry(2, 0), 0.0001);
        assertEquals(1.0, root.getEntry(1, 1), 0.0001);
        assertEquals(1.0, root.getEntry(2, 1), 0.0001);
        assertEquals(3.0, root.getEntry(2, 2), 0.0001);
    }

    // Test case for getRank method
    @Test
    public void testGetRank() {
        double[][] data = {{4.0, 1.0, 2.0}, {1.0, 2.0, 3.0}, {2.0, 3.0, 11.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        int rank = cholesky.getRank();
        assertEquals(3, rank);
    }
}