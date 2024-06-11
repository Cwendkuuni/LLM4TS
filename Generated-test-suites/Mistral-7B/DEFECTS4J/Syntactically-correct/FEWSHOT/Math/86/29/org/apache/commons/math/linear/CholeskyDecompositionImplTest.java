package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class CholeskyDecompositionImplTest {
    @Test
    public void testConstructorWithMatrix() throws Exception {
        // Create a test matrix
        double[][] matrixData = { { 1.0, 0.2, 0.3 }, { 0.2, 1.0, 0.4 }, { 0.3, 0.4, 1.0 } };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(matrixData, false);

        // Create CholeskyDecompositionImpl instance
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(inputMatrix);

        // Validate the decomposition
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lT = choleskyDecomposition.getLT();

        // Verify L is a lower triangular matrix
        for (int i = 0; i < l.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertNull(l.getEntry(i, j));
            }
        }

        // Verify LT is the transpose of L
        assertArrayEquals(lT.getData(), l.transpose().getData());

        // Verify determinant is correct
        assertEquals(1.0, choleskyDecomposition.getDeterminant(), 1e-14);

        // Verify solver works correctly
        RealVector b = new RealVectorImpl(new double[]{ 1.0, 2.0, 3.0 }, false);
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expectedSolution = new RealVectorImpl(new double[]{ 3.1622776601683795E-16, 0.5, 1.0 }, false);
        assertArrayEquals(expectedSolution.getData(), x.getData(), 1e-14);

        // Verify inverse works correctly
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(l.getRowDimension());
        RealMatrix inverse = choleskyDecomposition.getSolver().getInverse();
        RealMatrix product = choleskyDecomposition.getL().multiply(inverse);
        SingularValueDecomposition svd = new SingularValueDecomposition(identity);
        RealMatrix expectedInverse = svd.getU().transpose().multiply(svd.getVT());
        assertTrue(product.isSimilarTo(expectedInverse, 1e-14));

        // Test with matrices of different sizes
        RealMatrix smallMatrix = MatrixUtils.createRealIdentityMatrix(2);
        CholeskyDecompositionImpl smallCholeskyDecomposition = new CholeskyDecompositionImpl(smallMatrix);
        assertEquals(2.0, smallCholeskyDecomposition.getDeterminant(), 1e-14);

        RealMatrix largeMatrix = MatrixUtils.createRealIdentityMatrix(100);
        CholeskyDecompositionImpl largeCholeskyDecomposition = new CholeskyDecompositionImpl(largeMatrix);
        assertEquals(1.0E100, largeCholeskyDecomposition.getDeterminant(), 1e-14);

        // Test with non-square matrix
        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3).transpose());
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Pass
        }

        // Test with non-symmetric matrix
        double[][] nonSymmetricMatrix = { { 1.0, 0.2, 0.3 }, { 0.2, 1.0, -0.4 }, { 0.3, 0.4, 1.0 } };
        RealMatrix nonSymmetricInputMatrix = new Array2DRowRealMatrix(nonSymmetricMatrix, false);
        try {
            new CholeskyDecompositionImpl(nonSymmetricInputMatrix, 1e-15, 1e-10);
            fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // Pass
        }

        // Test with non-positive definite matrix
        double[][] nonPositiveDefiniteMatrix = { { 1.0, 0.2, 0.3 }, { 0.2, -1.0, 0.4 }, { 0.3, 0.4, -1.0 } };
        RealMatrix nonPositiveDefiniteInputMatrix = new Array2DRowRealMatrix(nonPositiveDefiniteMatrix, false);
        try {
            new CholeskyDecompositionImpl(nonPositiveDefiniteInputMatrix, 1e-15, 1e-10);
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Pass
        }

        // Test with negative symmetry and positivity thresholds
        try {
            new CholeskyDecompositionImpl(inputMatrix, -1.0e-15, 1e-10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        // Test with negative positivity threshold
        try {
            new CholeskyDecompositionImpl(inputMatrix, 1.0e-15, -1.0e-10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

    }
}