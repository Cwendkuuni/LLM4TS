package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testConstructor_ValidMatrix_Success() {
        // given
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, 5.0, 6.0 }, { 3.0, 6.0, 8.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        // when
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // then
        assertNotNull(decomposition);
        assertNotNull(decomposition.getRootMatrix());
        assertNotNull(decomposition.getRank());
        assertTrue(decomposition.getRank() > 0);
    }

    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrix_Exception() {
        // given
        double[][] matrixData = { { 1.0, -1.0 }, { -1.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        // when
        new RectangularCholeskyDecomposition(matrix, small);

        // then (exception should be thrown)
    }

    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NegativeDiagonalElements_Exception() {
        // given
        double[][] matrixData = { { 1.0, 2.0 }, { 2.0, -3.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        // when
        new RectangularCholeskyDecomposition(matrix, small);

        // then (exception should be thrown)
    }

    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_ZeroRankMatrix_Exception() {
        // given
        double[][] matrixData = { { 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        // when
        new RectangularCholeskyDecomposition(matrix, small);

        // then (exception should be thrown)
    }

    @org.junit.Test
    public void testConstructor_DifferentMatrixDimensions_Exception() {
        // given
        double[][] matrixData1 = { { 1.0, 2.0 }, { 2.0, 3.0 } };
        double[][] matrixData2 = { { 1.0, 2.0 }, { 2.0, 3.0 }, { 3.0, 3.0 } };
        RealMatrix matrix1 = new Array2DRowRealMatrix(matrixData1, false);
        RealMatrix matrix2 = new Array2DRowRealMatrix(matrixData2, false);
        double small = 0.0;

        // when
        // then (exception should not be thrown, but the decomposition should fail)
        try {
            new RectangularCholeskyDecomposition(matrix1, small);
            new RectangularCholeskyDecomposition(matrix2, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected, as matrices are not of the same dimensions
        }

        // then
        assertNull(matrix1.getRootMatrix());
        assertNull(matrix1.getRank());
        assertNull(matrix2.getRootMatrix());
        assertNull(matrix2.getRank());
    }

    @org.junit.Test
    public void testGetRootMatrix_ValidDecomposition_Success() {
        // given
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, 5.0, 6.0 }, { 3.0, 6.0, 8.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // when
        RealMatrix root = decomposition.getRootMatrix();

        // then
        assertNotNull(root);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 / Math.sqrt(2.0) }, { 2.0 / Math.sqrt(2.0), 1.0 }, { 3.0 / Math.sqrt(5.0), 6.0 / Math.sqrt(5.0) } }, false);
        assertArrayEquals(expected.getData(), root.getData(), 0.0);
    }

    @org.junit.Test
    public void testGetRank_ValidDecomposition_Success() {
        // given
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, 5.0, 6.0 }, { 3.0, 6.0, 8.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // when
        int rank = decomposition.getRank();

        // then
        assertTrue(rank > 0);
        assertEquals(rank, 3);
    }

    @org.junit.Test
    public void testGetRank_ZeroRankDecomposition_ZeroRank() {
        // given
        double[][] matrixData = { { 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // when
        int rank = decomposition.getRank();

        // then
        assertEquals(rank, 0);
    }

    @org.junit.Test
    public void testGetRank_NonSquareMatrix_RankSameAsColumns() {
        // given
        double[][] matrixData = { { 1.0, 2.0 }, { 2.0, 5.0 }, { 3.0, 6.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // when
        int rank = decomposition.getRank();

        // then
        assertEquals(rank, 2);
    }

    @org.junit.Test
    public void testGetRank_SingularValueDecomposition_SameRank() {
        // given
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, 5.0, 6.0 }, { 3.0, 6.0, 8.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // when
        int rank = decomposition.getRank();
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);

        // then
        assertEquals(rank, svd.getRank());
    }

}