package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.LinearAlgebraTests;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition.SingularValue;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0},
                {4.0, 6.0, 7.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        data = new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 4.0}, {3.0, 4.0, 6.0}};
        matrix = new Array2DRowRealMatrix(data, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertTrue(decomp.getRank() == 2);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
        }
    }

    /**
     * Test getRootMatrix method.
     */
    @org.junit.Test
    public void testGetRootMatrix() {
        double small = 1e-10;
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 4.0},
                {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 2.0},
                    {0.0, 2.0},
                    {0.0, 1.0}
            }, false);
            assertArrayEquals(expected.getData(), decomp.getRootMatrix().getData(), 1e-10);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
        }
    }

    /**
     * Test getRank method.
     */
    @org.junit.Test
    public void testGetRank() {
        double small = 1e-10;
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 4.0},
                {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomp.getRank();
            assertTrue(rank == 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
        }
        data = new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 4.0}, {3.0, 4.0, 5.0}};
        matrix = new Array2DRowRealMatrix(data, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomp.getRank();
            assertTrue(rank == 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
        }
        data = new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 0.0}, {3.0, 4.0, 6.0}};
        matrix = new Array2DRowRealMatrix(data, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomp.getRank();
            assertTrue(rank == 2);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
        }
        data = new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 7.0}};
        matrix = new Array2DRowRealMatrix(data, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomp.getRank();
            assertTrue(rank == 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
        }
    }

    /**
     * Test exception handling.
     */
    @org.junit.Test
    public void testExceptionHandling() {
        double small = 1e-10;
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, -1.0, 4.0},
                {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            // should not reach here
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 2.0},
                    {0.0, 2.0},
                    {0.0, 1.0}
            }, false);
            assertArrayEquals(expected.getData(), decomp.getRootMatrix().getData(), 1e-10);
        }
    }

    /**
     * Test SVD of the root matrix.
     */
    @org.junit.Test
    public void testSvdOfRootMatrix() {
        double small = 1e-10;
        double epsilon = 1e-15;
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 4.0},
                {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomp.getRootMatrix();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 1.0, 1.0},
                    {2.0, 1.0, 1.0},
                    {3.0, 1.0, 1.0}
            }, false);
            SingularValueDecomposition svd = new SingularValueDecomposition(root);
            SingularValue[] singularValues = svd.getSingularValues();
            RealVector u = svd.getU();
            RealVector v = svd.getV();
            for (int i = 0; i < singularValues.length; ++i) {
                assertTrue(Precision.equals(singularValues[i].getValue(), i + 1, epsilon));
            }
            assertArrayEquals(u.toArray(), LinearAlgebraTests.identityMatrix(3), epsilon);
            assertArrayEquals(v.toArray(), LinearAlgebraTests.identityMatrix(3).transpose().toArray(), epsilon);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
        }
    }

    /**
     * Test random values.
     */
    @org.junit.Test
    public void testRandomValues() {
        Random random = new Random(123);
        double small = 1e-10;
        double threshold = 0.1;
        int size = 10;
        RealMatrix matrix = new Array2DRowRealMatrix(size, size, random);
        matrix = matrix.transpose().add(matrix);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomp.getRootMatrix();
            assertTrue(decomp.getRank() == size);
            assertArrayEquals(root.getData(), root.transpose().getData(), 1e-10);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
        }
        // check that the decomposition is not symmetric
        RealMatrix root = decomp.getRootMatrix();
        RealMatrix rootT = decomp.getRootMatrix().transpose();
        assertFalse(root.equals(rootT));
    }

}