package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.util.MatrixUtils;

import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        double[][] matrixData = {
                {1.0, 0.5, 0.5},
                {0.5, 1.0, 0.5},
                {0.5, 0.5, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();

        assertEquals("L is not equal to L^T * Matrix", l.transpose().multiply(matrix), lt);

        RealVector x = lt.solve(new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}, {1.0}}));
        RealVector y = matrix.solve(x);
        assertEquals("Solution is not equal to identity", MatrixUtils.createRealIdentityMatrix(3).multiply(x), y);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] matrixData = {
                {1.0, 0.5, 0.5},
                {0.5, 0.5, 0.5},
                {0.5, 0.5, -1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] matrixData = {
                {1.0, 0.5},
                {0.5, 0.5},
                {0.5, 0.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] matrixData = {
                {1.0, 0.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }
}