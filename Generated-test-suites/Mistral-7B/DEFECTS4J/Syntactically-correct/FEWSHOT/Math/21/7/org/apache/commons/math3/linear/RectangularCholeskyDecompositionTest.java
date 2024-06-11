package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition for a full rank positive semidefinite matrix.
     */
    @Test
    public void testFullRank() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.5, 0.25},
            {0.5, 1.0, 0.5},
            {0.25, 0.5, 1.0}
        });

        RealMatrix root = new RectangularCholeskyDecomposition(matrix, small).getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {FastMath.sqrt(1.0), 0.0, 0.0},
            {0.5, FastMath.sqrt(0.5), 0.0},
            {0.25, 0.25, FastMath.sqrt(0.5)}
        });
        assertArrayEquals(expected.getData(), root.getData(), small);
    }

    /**
     * Test the decomposition for a matrix with a rank less than the number of rows.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRankDeficient() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.5, 0.25},
            {0.5, 1.0, 0.5},
            {0.25, 0.5, 0.1}
        });
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test the decomposition for a non-symmetric matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonSymmetric() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.5},
            {0.5, 1.0},
            {0.25, 0.5}
        });
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test the decomposition for an empty matrix.
     */
    @Test
    public void testEmpty() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    /**
     * Test the decomposition for a diagonal matrix with all elements below threshold.
     */
    @Test
    public void testAllBelowThreshold() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1e-15, 0.0},
            {0.0, 1e-15}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test the decomposition for a diagonal matrix with one element above threshold.
     */
    @Test
    public void testOneAboveThreshold() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1e-15, 1.0},
            {0.0, 1e-15}
        });
        RealMatrix root = new RectangularCholeskyDecomposition(matrix, small).getRootMatrix();
        RealVector expected = new ArrayRealVector(new double[] {1.0, 0.0});
        assertNotNull(root);
        assertTrue(Precision.isClose(1.0, root.getEntry(0, 0), small));
        assertTrue(Precision.isClose(0.0, root.getEntry(1, 0), small));
        assertArrayEquals(expected.toArray(), root.getColumnVector(0).toArray(), small);
    }

}