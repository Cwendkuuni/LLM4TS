package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, 3.0, 3.0},
            {2.0, 3.0, 5.0}
        });

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = choleskyDecomposition.getL();
        assertNotNull(l);
        RealMatrix lt = choleskyDecomposition.getLT();
        assertNotNull(lt);
        assertEquals(matrix, lt.transposeMultiply(l));
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {-1.0, 0.0, 0.0},
            {0.0, -1.0, 0.0},
            {0.0, 0.0, -1.0}
        });

        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {2.0, 3.0},
            {0.0, 3.0}
        });

        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, 3.0, 3.0},
            {2.0, 3.0, 5.0}
        });

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        assertEquals(37.0, choleskyDecomposition.getDeterminant(), 0.0);
    }

    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 2.0},
            {2.0, 3.0, 3.0},
            {2.0, 3.0, 5.0}
        });

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);

        RealVector expected = new ArrayRealVector(new double[]{0.6, -0.2, 0.2});
        assertArrayEquals(expected.toArray(), x.toArray(), 0.0);
    }

}