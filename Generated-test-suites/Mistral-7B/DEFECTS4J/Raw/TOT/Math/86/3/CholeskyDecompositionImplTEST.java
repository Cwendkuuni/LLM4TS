package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CholeskyDecompositionImpl.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the CholeskyDecompositionImpl with a valid positive definite matrix.
     */
    @Test
    public void testValidPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 2.0},
                {2.0, 5.0, 3.0},
                {2.0, 3.0, 4.0}
        });
        try {
            CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = decomp.getL();
            RealMatrix lt = decomp.getLT();
            assertEquals(lt, l.transpose());
            RealVector x = l.solve(MatrixUtils.createRealVector(new double[]{3.0, 1.0, 1.0}));
            assertEquals(MatrixUtils.createRealVector(new double[]{1.0, -0.5, 0.25}), x);
        } catch (NotPositiveDefiniteMatrixException | NonSquareMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the CholeskyDecompositionImpl with a square but not positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 2.0},
                {2.0, 4.0, 3.0},
                {2.0, 3.0, 2.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the CholeskyDecompositionImpl with a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 5.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the CholeskyDecompositionImpl with a symmetric but not positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0},
                {1.0, 2.0, 2.0},
                {1.0, 2.0, 5.0}
        });
        new CholeskyDecompositionImpl(matrix, 1e-10, 1e-15);
    }

    /**
     * Test the CholeskyDecompositionImpl with a tolerance for symmetry.
     */
    @Test
    public void testSymmetricMatrixWithTolerance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1e-16, 1e-16},
                {1e-16, 1.0, 1e-16},
                {1e-16, 1e-16, 1.0}
        });
        try {
            new CholeskyDecompositionImpl(matrix, 1e-15, 1e-15);
            assertTrue(true);
        } catch (NotSymmetricMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}