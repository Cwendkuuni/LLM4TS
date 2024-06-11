package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.SpecialFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
            return;
        }
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                {1.0, 2.5811388300841898, 3.1622776601683795},
                {1.0, 0.0, 3.244596222103326}});
        RealMatrix resultRoot = decomp.getRootMatrix();
        assertEquals(expectedRoot.getEntry(0, 0), resultRoot.getEntry(0, 0), 1e-14);
        assertEquals(expectedRoot.getEntry(1, 0), resultRoot.getEntry(1, 0), 1e-14);
        assertEquals(expectedRoot.getEntry(2, 0), resultRoot.getEntry(2, 0), 1e-14);
        assertEquals(expectedRoot.getEntry(0, 1), resultRoot.getEntry(0, 1), 1e-14);
        assertEquals(expectedRoot.getEntry(1, 1), resultRoot.getEntry(1, 1), 1e-14);
        assertEquals(expectedRoot.getEntry(1, 2), resultRoot.getEntry(1, 2), 1e-14);
        assertEquals(expectedRoot.getEntry(2, 1), resultRoot.getEntry(2, 1), 1e-14);
        assertEquals(expectedRoot.getEntry(2, 2), resultRoot.getEntry(2, 2), 1e-14);
        int expectedRank = 3;
        int actualRank = decomp.getRank();
        assertEquals(expectedRank, actualRank);
    }

    /**
     * Test the decomposition of a positive semidefinite matrix.
     */
    @Test
    public void testPositiveSemidefinite() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 5.0, 10.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
            return;
        }
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                {1.0, 2.5811388300841898, 3.1622776601683795},
                {1.0, 0.0, 3.244596222103326}});
        RealMatrix resultRoot = decomp.getRootMatrix();
        assertEquals(expectedRoot.getEntry(0, 0), resultRoot.getEntry(0, 0), 1e-14);
        assertEquals(expectedRoot.getEntry(1, 0), resultRoot.getEntry(1, 0), 1e-14);
        assertEquals(expectedRoot.getEntry(2, 0), resultRoot.getEntry(2, 0), 1e-14);
        assertEquals(expectedRoot.getEntry(0, 1), resultRoot.getEntry(0, 1), 1e-14);
        assertEquals(expectedRoot.getEntry(1, 1), resultRoot.getEntry(1, 1), 1e-14);
        assertEquals(expectedRoot.getEntry(1, 2), resultRoot.getEntry(1, 2), 1e-14);
        assertEquals(expectedRoot.getEntry(2, 1), resultRoot.getEntry(2, 1), 1e-14);
        assertEquals(expectedRoot.getEntry(2, 2), resultRoot.getEntry(2, 2), 1e-14);
        int expectedRank = 3;
        int actualRank = decomp.getRank();
        assertEquals(expectedRank, actualRank);
    }

    /**
     * Test the decomposition of a non-symmetric matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonSymmetric() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 4.0, 10.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix, 1e-14);
    }

    /**
     * Test the decomposition of a singular matrix
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingular() {
        double[][] matrixData = {
            {1.0, 0.0},
            {0.0, 0.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix, 1e-14);
    }

    /**
     * Test the decomposition of a matrix with small diagonal elements.
     */
    @Test
    public void testSmallDiagonal() {
        double[][] matrixData = {
            {1.0, 1e-10, 3.0},
            {1e-10, 1.0, 5.0},
            {3.0, 5.0, 10.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
            return;
        }
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 3.0},
                {1.0, 1.0, 3.0},
                {1.0, 0.0, 3.1622776601683795}});
        RealMatrix resultRoot = decomp.getRootMatrix();
        assertEquals(expectedRoot.getEntry(0, 0), resultRoot.getEntry(0, 0), 1e-14);
        assertEquals(expectedRoot.getEntry(1, 0), resultRoot.getEntry(1, 0), 1e-14);
        assertEquals(expectedRoot.getEntry(2, 0), resultRoot.getEntry(2, 0), 1e-14);
        assertEquals(expectedRoot.getEntry(0, 1), resultRoot.getEntry(0, 1), 1e-14);
        assertEquals(expectedRoot.getEntry(1, 1), resultRoot.getEntry(1, 1), 1e-14);
        assertEquals(expectedRoot.getEntry(1, 2), resultRoot.getEntry(1, 2), 1e-14);
        assertEquals(expectedRoot.getEntry(2, 1), resultRoot.getEntry(2, 1), 1e-14);
        assertEquals(expectedRoot.getEntry(2, 2), resultRoot.getEntry(2, 2), 1e-14);
        int expectedRank = 3;
        int actualRank = decomp.getRank();
        assertEquals(expectedRank, actualRank);
    }

    /**
     * Test the decomposition of a matrix with negative diagonal elements.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeDiagonal() {
        double[][] matrixData = {
            {-1.0, 1.0, 3.0},
            {1.0, 5.0, 6.0},
            {3.0, 5.0, 10.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix, 1e-14);
    }

}