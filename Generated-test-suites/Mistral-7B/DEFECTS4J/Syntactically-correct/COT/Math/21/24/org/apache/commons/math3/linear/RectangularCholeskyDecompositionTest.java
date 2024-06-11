package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertTrue("The decompositions should not be the same", !matrix.equals(decomp.getRootMatrix()));
            assertEquals("The rank should be 3", 3, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown for positive definite matrix");
        }

        try {
            RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, -1, 6}, {3, 6, 9}});
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix2, 0.0);
            fail("Exception should be thrown for non positive definite matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        try {
            RealMatrix matrix3 = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 2}});
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix3, 0.0);
            fail("Exception should be thrown for non square matrix");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(1), 0, 0},
                {1.0 / Math.sqrt(2), 1.0 / Math.sqrt(2), 0},
                {1.0 / Math.sqrt(3), 2.0 / Math.sqrt(2), 1.0 / Math.sqrt(3)}});
            RealMatrix actual = decomp.getRootMatrix();
            assertEquals("The root matrix should be correct", expected, actual);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown for positive definite matrix");
        }

        try {
            RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, -1, 6}, {3, 6, 9}});
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix2, 0.0);
            fail("Decomposition should not be possible for non positive definite matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            int expected = 3;
            int actual = decomp.getRank();
            assertEquals("The rank should be correct", expected, actual);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown for positive definite matrix");
        }

        try {
            RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, -1, 6}, {3, 6, 9}});
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix2, 0.0);
            fail("Decomposition should not be possible for non positive definite matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSingularValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
            double[] singularValues = svd.getSingularValues();
            double[] expected = new double[]{Math.sqrt(1), Math.sqrt(5), Math.sqrt(13)};
            for (int i = 0; i < 3; ++i) {
                assertEquals("Singular value " + i + " should be correct", Precision.round(expected[i], 5),
                        Precision.round(singularValues[i], 5));
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown for positive definite matrix");
        }

        try {
            RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, -1, 6}, {3, 6, 9}});
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix2, 0.0);
            fail("Decomposition should not be possible for non positive definite matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}