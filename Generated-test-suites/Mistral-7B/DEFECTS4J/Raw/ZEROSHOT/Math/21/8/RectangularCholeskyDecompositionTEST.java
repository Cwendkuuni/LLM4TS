package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testValidDecomposition() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 5.0, 4.0},
                {1.0, 4.0, 6.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(3.0), 0.0, 0.0},
                    {0.5 * FastMath.sqrt(13.0), 1.5 * FastMath.sqrt(13.0), 0.0},
                    {-0.5 * FastMath.sqrt(13.0), 0.5 * FastMath.sqrt(13.0), 2.0 * FastMath.sqrt(13.0)}
            });
            assertEquals(expectedRoot, root);
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSmallDiagonalElements() {
        double small = 1e-5;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 4.9999999999999995e-10, 4.0},
                {1.0, 4.0, 6.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(3.0), 0.0, 0.0},
                    {0.0, 0.0, 0.0},
                    {-2.0 * FastMath.sqrt(3.0), 1.0, 2.0 * FastMath.sqrt(3.0)}
            });
            assertEquals(expectedRoot, root);
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testNegativeDiagonalElements() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 5.0, 4.0},
                {1.0, 4.0, -6.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testIdentityMatrix() {
        double small = 1e-14;
        RealMatrix matrix = IdentityMatrix.DIAGONAL_2.clone();
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expectedRoot = matrix;
            assertEquals(expectedRoot, root);
            assertEquals(IdentityMatrix.DIAGONAL_2.getDimension(), decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSingularMatrix() {
        double small = 1e-14;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testRandomMatrix() {
        double small = 1e-10;
        int size = 10;
        RealMatrix matrix = new Array2DRowRealMatrix(new Random().nextGauss(size, size).toArray());
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expectedRoot = new Array2DRowRealMatrix(decomposition.getRank(), size);
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < decomposition.getRank(); ++j) {
                    expectedRoot.setEntry(j, i, Precision.round(decomposition.getRootMatrix().getEntry(i, j), 14));
                }
            }
            assertEquals(expectedRoot, root);
            assertEquals(decomposition.getRank(), matrix.getRowDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

}