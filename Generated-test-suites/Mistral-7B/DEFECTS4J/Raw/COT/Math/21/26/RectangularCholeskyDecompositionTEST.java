package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import org.junit.Test;

/**
 * Test class for {@link RectangularCholeskyDecomposition}
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructor() {
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, 5.0, 6.0 }, { 3.0, 6.0, 10.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 0.0;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomp.getRootMatrix());
        assertNotNull(decomp.getRank());
    }

    /**
     * Test constructor with invalid input (non-positive definite matrix).
     */
    @Test
    public void testConstructorInvalidMatrix() {
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, -1.0, 6.0 }, { 3.0, 6.0, 10.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 0.0;
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test getRootMatrix() method.
     */
    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, 5.0, 6.0 }, { 3.0, 6.0, 10.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 0.0;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomp.getRootMatrix();
        double[][] expectedRootData = { { 1.0, 1.41421356 }, { 0.0, 2.23606798 }, { 0.0, 0.0 } };
        assertArrayEquals(expectedRootData, root.getData());
    }

    /**
     * Test getRank() method.
     */
    @Test
    public void testGetRank() {
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, 5.0, 6.0 }, { 3.0, 6.0, 10.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 0.0;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomp.getRank();
        assertEquals(3, rank);
    }

}