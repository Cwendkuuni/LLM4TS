package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 2.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        RealMatrix identity = MatrixUtils.createIdentityMatrix(3);
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.447213595522388, 1.0, 0.0}, {-0.447213595522388, -0.764575130856758, 1.0}});
        RealMatrix expectedLT = lt.transpose();
        assertEquals(expectedL, l);
        assertEquals(expectedLT, lt);
        RealMatrix inverseL = l.getInverse();
        RealMatrix lMultiplyInverseL = l.multiply(inverseL);
        RealMatrix identity2 = MatrixUtils.createIdentityMatrix(3);
        assertTrue(lMultiplyInverseL.isIdentityMatrix());
        assertTrue(identity.equals(identity2));
        RealMatrix expectedInverse = new Array2DRowRealMatrix(new double[][]{{1.5, -0.3333333333333335, 0.6666666666666667}, {-0.3333333333333335, 1.2, -0.6}, {0.6666666666666667, -0.6, 1.5}});
        assertEquals(expectedInverse, inverseL);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, -1.0}});
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testMathRuntimeException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 0.0}});
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

}