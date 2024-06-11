package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testDecompositionOfSymmetricPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                     {2.0, 3.0, 4.0},
                                                                     {2.0, 4.0, 5.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        Assert.assertEquals(matrix, l.transpose().multiply(lt));
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testDecompositionOfMatrixWithNegativeEigenvalues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                     {2.0, -1.0, 4.0},
                                                                     {2.0, 4.0, 5.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testDecompositionOfMatrixWithOffDiagonalNotSymmetricElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                     {2.0, 3.0, 4.0},
                                                                     {2.0, 4.0, 5.0},
                                                                     {2.0, 4.0, 6.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testSolvingLinearEquation() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                     {2.0, 3.0}});
        RealVector b = new ArrayRealVector(new double[]{1.0, 3.0});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        Assert.assertEquals(new ArrayRealVector(new double[]{1.5, 1.0}), x);
    }

    @Test
    public void testInverseMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                     {2.0, 3.0}});
        RealMatrix inverse = new CholeskyDecompositionImpl(matrix).getInverse();
        Assert.assertEquals(MatrixUtils.createRealIdentityMatrix(2), inverse.multiply(matrix));
    }

}