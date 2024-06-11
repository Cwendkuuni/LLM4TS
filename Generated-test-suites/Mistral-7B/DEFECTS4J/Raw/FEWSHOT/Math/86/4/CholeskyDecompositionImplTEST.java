package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {

        double[][] matrixData = {{1.0, 2.0, 3.0}, {2.0, 5.0, 4.0}, {3.0, 4.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            new CholeskyDecompositionImpl(matrix);
        } catch (NotSquareMatrixException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        } catch (NotSymmetricMatrixException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        } catch (NotPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
        }

        RealMatrix l = new CholeskyDecompositionImpl(matrix).getL();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.44948974278317, 1.0, 0.0},
                {0.27970562334347, 1.24152924643363, 1.0}
        }, false);
        assertTrue(l.equals(expectedL));

        RealMatrix lt = new CholeskyDecompositionImpl(matrix).getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.44948974278317, 0.27970562334347},
                {0.0, 1.0, 1.24152924643363},
                {0.0, 0.0, 1.0}
        }, false);
        assertTrue(lt.equals(expectedLT));

        double determinant = new CholeskyDecompositionImpl(matrix).getDeterminant();
        assertTrue(Math.abs(determinant - 35.0) < 1e-14);

        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0}, false);
        RealVector x = new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
        assertArrayEquals(new double[]{1.0 / 3.0, (-2.0 / 3.0 + 1.0 / 6.0) / 3.0, 1.0 / 6.0}, x.toArray(), 1e-14);

        RealMatrix B = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 5.0}
        }, false);
        RealMatrix X = new CholeskyDecompositionImpl(matrix).getSolver().solve(B);
        assertArrayEquals(new double[][]{
                {3.0 / 5.0, 1.0 / 5.0},
                {-2.0 / 5.0 + 2.0 / 15.0, 1.0 / 5.0 - 2.0 / 15.0}
        }, X.getData(), 1e-14);

        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3));
            throw new AssertionError("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected
        }

        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(2));
            throw new AssertionError("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3), 0.0, 1.0e-16);
            throw new AssertionError("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected
        }

        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3), 1.0e-15, 0.0);
            throw new AssertionError("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // Expected
        }

        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3), 1.0e-15, 1.0e-14);
            throw new AssertionError("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected
        }

    }

}