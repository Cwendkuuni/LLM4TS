package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testConstructAndGetRootMatrix() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 5.0},
            {3.0, 5.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 0.0;
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 2.0, 1.0}, {0.0, 0.0, 1.0}});
            RealMatrix root = decompositon.getRootMatrix();
            assertEquals(expectedRoot, root);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected since matrix is not positive definite
        }
    }

    @org.junit.Test
    public void testConstructWithSmallNegativeDiagonalElement() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 4.5},
            {3.0, 5.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 0.0;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            // exception should have been thrown
            assert(false);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(4.5, e.getDiagonalValue(), 0.0);
            assertEquals(1, e.getDiagonalIndex());
        }
    }

    @org.junit.Test
    public void testConstructWithLargeNegativeDiagonalElements() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, -5.0},
            {3.0, 5.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 0.0;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            // exception should have been thrown
            assert(false);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-5.0, e.getDiagonalValue(), 0.0);
            assertEquals(1, e.getDiagonalIndex());
        }
    }

    @org.junit.Test
    public void testConstructWithSmallDiagonalElements() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 0.5, 4.5},
            {3.0, 5.0, 0.1}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 0.5;
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 1.0, 1.5}, {0.0, 0.0, 1.0}});
            RealMatrix root = decompositon.getRootMatrix();
            assertEquals(expectedRoot, root);
        } catch (NonPositiveDefiniteMatrixException e) {
            // exception should not have been thrown
        }
    }

    @org.junit.Test
    public void testGetRank() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 5.0},
            {3.0, 5.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 0.0;
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decompositon.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            // exception should not have been thrown
        }
    }

}