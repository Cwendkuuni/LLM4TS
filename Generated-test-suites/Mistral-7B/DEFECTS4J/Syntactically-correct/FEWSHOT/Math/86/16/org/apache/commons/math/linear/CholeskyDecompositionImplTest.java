package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        });

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();

        // Check if L is lower triangular
        for (int i = 0; i < l.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(0, l.getEntry(i, j), 1e-14);
            }
        }

        // Check if LT is symmetric with L
        for (int i = 0; i < l.getRowDimension(); i++) {
            for (int j = i; j < l.getColumnDimension(); j++) {
                double lij = l.getEntry(i, j);
                double lji = l.getEntry(j, i);
                double ltilj = lt.getEntry(i, j);
                double ltlij = lt.getEntry(j, i);
                assertEquals(lij, ltilj, 1e-14);
                assertEquals(lji, ltlij, 1e-14);
            }
        }

        // Check if LT * L = matrix
        RealMatrix product = lt.multiply(l);
        RealMatrix identity = MatrixUtils.createIdentityMatrix(matrix.getRowDimension());
        assertEquals(identity, product, 1e-14);

        // Check if L * LT = matrix
        product = l.multiply(lt);
        assertEquals(matrix, product, 1e-14);

        // Test solver
        RealVector b = new RealVectorImpl(new double[]{1.0, -1.0, 1.0}, false);
        RealVector x = cholesky.getSolver().solve(b);
        assertEquals(new double[]{0.5, -0.5, 0.5}, x.toArray(), 1e-14);

        RealVector x2 = new RealVectorImpl(new double[]{2.0, -1.0, 1.0}, false);
        assertNull(cholesky.getSolver().solve(x2));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.5},
            {0.5, 1.0},
            {0.5, 1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.5},
            {0.5, 1.0},
            {0.5, 1.1}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.5},
            {0.5, -1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

}