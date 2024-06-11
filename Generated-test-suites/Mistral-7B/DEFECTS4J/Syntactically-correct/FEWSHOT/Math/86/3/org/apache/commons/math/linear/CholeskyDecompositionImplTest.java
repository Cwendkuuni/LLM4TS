package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {4.0, 1.0, 1.0},
            {1.0, 4.0, 1.0},
            {1.0, 1.0, 4.0}
        });

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        assertEquals(l.getRowDimension(), lt.getRowDimension());
        assertEquals(l.getColumnDimension(), lt.getColumnDimension());
        assertEquals(l.getRowDimension(), l.getColumnDimension());
        assertNotNull(l);
        assertNotNull(lt);

        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {1.0, 2.0, 0.0},
            {1.0, 1.0, 2.0}
        });

        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        });

        assertEquals(expectedL, l);
        assertEquals(expectedLT, lt);

        double determinant = cholesky.getDeterminant();
        assertEquals(determinant, 1.0 * 2.0 * 2.0, 1e-14);

        DecompositionSolver solver = cholesky.getSolver();
        RealVector x = solver.solve(new ArrayRealVector(new double[]{1.0, 2.0, 3.0}));
        assertEquals(x.getDimension(), 3);
        RealVector b = new ArrayRealVector(new double[]{1.0, 0.0, 0.0});
        RealVector y = solver.solve(b);
        assertEquals(y.getDimension(), 3);
        assertEquals(y, b);

        RealVector z = new ArrayRealVector(new double[]{0.1, 0.2, 0.3});
        RealVector w = solver.solve(z);

        assertArrayEquals(new double[]{0.1, 0.05, 0.025}, w.toArray(), 1e-14);

    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {4.0, 1.0},
            {1.0, 4.0},
            {1.0, 1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {4.0, 1.0, 1.0},
            {1.0, 4.0, 1.0},
            {1.0, 1.0, 4.0},
            {1.0, 1.0, 1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {4.0, 1.0, 1.0},
            {1.0, 4.0, -1.0},
            {1.0, -1.0, 4.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {4.0, 1.0, 1.0},
            {1.0, 4.0, 1.0},
            {1.0, 1.0, 4.0},
            {1.0, 2.0, 1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testCholeskyDecompositionImplWithCustomThreshold() throws Exception {
        double relativeSymmetryThreshold = 1e-8;
        double absolutePositivityThreshold = 1e-14;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {4.0, 1.0, 1.0},
            {1.0, 4.0, 1.0},
            {1.0, 1.0, 4.0}
        });
        new CholeskyDecompositionImpl(matrix, relativeSymmetryThreshold, absolutePositivityThreshold);
    }

}