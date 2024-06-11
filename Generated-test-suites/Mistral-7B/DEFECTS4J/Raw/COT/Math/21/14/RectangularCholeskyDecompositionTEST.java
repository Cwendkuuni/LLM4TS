package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Test class for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructionValidInput() {
        double[][] matrixData = { { 1.0, 2.0, 3.0, 4.0 }, { 2.0, 5.0, 6.0, 7.0 }, { 3.0, 6.0, 8.0, 9.0 },
                { 4.0, 7.0, 9.0, 10.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.001;

        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomposer.getRootMatrix());
        assertNotNull(decomposer.getRank());
    }

    /**
     * Test constructor with small matrix.
     */
    @Test
    public void testConstructionSmallMatrix() {
        double[][] matrixData = { { 1.0, 2.0 }, { 2.0, 3.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test constructor with negative definite matrix.
     */
    @Test
    public void testConstructionNegativeDefiniteMatrix() {
        double[][] matrixData = { { -1.0, 2.0 }, { 2.0, -3.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test getRootMatrix method with valid input.
     */
    @Test
    public void testGetRootMatrixValidInput() {
        double[][] matrixData = { { 1.0, 2.0, 3.0, 4.0 }, { 2.0, 5.0, 6.0, 7.0 }, { 3.0, 6.0, 8.0, 9.0 },
                { 4.0, 7.0, 9.0, 10.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.001;

        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0, 0.0 },
                { 0.7425245479078134, 1.4142137311575912, 0.0, 0.0 },
                { 1.2612223594893184, 1.8485549431028871, 2.0, 0.0 },
                { 1.7213113259185137, 2.4330105511131208, 2.4494897427831725, 1.0 } });

        RealMatrix root = decomposer.getRootMatrix();
        assertEquals(expectedRoot, root);
    }

    /**
     * Test getRank method with valid input.
     */
    @Test
    public void testGetRankValidInput() {
        double[][] matrixData = { { 1.0, 2.0, 3.0, 4.0 }, { 2.0, 5.0, 6.0, 7.0 }, { 3.0, 6.0, 8.0, 9.0 },
                { 4.0, 7.0, 9.0, 10.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.001;

        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposer.getRank();
        assertEquals(4, rank);
    }

    /**
     * Test getRank method with singular matrix.
     */
    @Test
    public void testGetRankSingularMatrix() {
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, 4.0, 4.0 }, { 3.0, 4.0, 4.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        RectangularCholeskyDecomposition decomposer = assertThrows(NonPositiveDefiniteMatrixException.class,
                () -> new RectangularCholeskyDecomposition(matrix, small));
        int rank = decomposer.getRank();
        assertEquals(3, rank);
    }

    /**
     * Test getRank method with matrix with small diagonal elements.
     */
    @Test
    public void testGetRankSmallDiagonalElements() {
        double[][] matrixData = { { 1.0, 2.0, 3.0, 4.0 }, { 2.0, 0.1, 6.0, 7.0 }, { 3.0, 6.0, 0.1, 9.0 },
                { 4.0, 7.0, 9.0, 0.1 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.1;

        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposer.getRank();
        assertEquals(3, rank);
    }

    /**
     * Test constructor with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionNullMatrix() {
        new RectangularCholeskyDecomposition(null, 0.001);
    }

    /**
     * Test constructor with empty matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructionEmptyMatrix() {
        double[][] matrixData = {};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test constructor with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionNegativeMatrix() {
        double[][] matrixData = { { -1.0, 2.0 }, { 2.0, -3.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double small = 0.0;

        new RectangularCholeskyDecomposition(matrix, small);
    }

}