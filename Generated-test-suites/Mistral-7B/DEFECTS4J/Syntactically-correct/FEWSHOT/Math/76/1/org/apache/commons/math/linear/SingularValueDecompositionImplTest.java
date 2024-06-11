package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                           {4.0, 5.0, 6.0},
                                                                           {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(15.0),
                                                                          1.0 / Math.sqrt(15.0),
                                                                          1.0 / Math.sqrt(15.0)},
                                                                       {-1.0 / Math.sqrt(15.0),
                                                                        -1.0 / Math.sqrt(15.0),
                                                                        1.0 / Math.sqrt(15.0)},
                                                                       {-1.0 / Math.sqrt(15.0),
                                                                         1.0 / Math.sqrt(15.0),
                                                                         -1.0 / Math.sqrt(15.0)}});
        RealMatrix expectedVT = new Array2DRowRealMatrix(new double[][]{{Math.sqrt(15.0),
                                                                           -Math.sqrt(15.0),
                                                                           -Math.sqrt(15.0)},
                                                                       {2.0 * Math.sqrt(15.0),
                                                                        2.0 * Math.sqrt(15.0),
                                                                        2.0 * Math.sqrt(15.0)},
                                                                       {3.0 * Math.sqrt(15.0),
                                                                        3.0 * Math.sqrt(15.0),
                                                                        3.0 * Math.sqrt(15.0)}});

        assertEquals(matrix.getRowDimension(), svd.getRank());
        assertEquals(matrix.getColumnDimension(), svd.getV().getColumnDimension());
        assertEquals(svd.getU().getRowDimension(), svd.getU().getColumnDimension(), 2);
        assertEquals(svd.getS().getRowDimension(), svd.getS().getColumnDimension(), 2);
        assertEquals(svd.getV().getRowDimension(), svd.getV().getColumnDimension(), 2);
        assertEquals(svd.getUT().getRowDimension(), svd.getUT().getColumnDimension(), 2);
        assertEquals(svd.getVT().getRowDimension(), svd.getVT().getColumnDimension(), 2);
        assertNotNull(svd.getSolver());

        assertEquals(u, svd.getU());
        assertEquals(s, svd.getS());
        assertEquals(v, svd.getV());
        assertEquals(ut, svd.getUT());
        assertEquals(vt, svd.getVT());

        assertArrayEquals(expectedUT.getData(), ut.getData(), 0.0001);
        assertArrayEquals(expectedVT.getData(), vt.getData(), 0.0001);
    }

    @Test
    public void testSingularValueDecompositionWithZeroMatrix() throws Exception {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();

        assertNotNull(svd.getSolver());
        assertEquals(0, svd.getRank());
        assertEquals(1, s.getRowDimension());
        assertEquals(1, s.getColumnDimension());
        assertEquals(1, v.getRowDimension());
        assertEquals(1, v.getColumnDimension());
        assertEquals(1, ut.getRowDimension());
        assertEquals(1, ut.getColumnDimension());
        assertEquals(1, vt.getRowDimension());
        assertEquals(1, vt.getColumnDimension());
        assertArrayEquals(new double[][]{{}}, ut.getData());
        assertArrayEquals(new double[][]{{}}, vt.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionWithNegativeMatrix() throws Exception {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 3.0},
                                                                           {4.0, -5.0, 6.0},
                                                                           {7.0, 8.0, -9.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testSolver() throws Exception {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                           {4.0, 5.0, 6.0},
                                                                           {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        RealVector b = new ArrayRealVector(new double[]{1.0, 0.0, 1.0});
        RealVector x = solver.solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{0.3333333333333333,
                                                               -0.2,
                                                               0.6});
        assertEquals(expectedX, x);

        b = new ArrayRealVector(new double[]{1.0, 0.0, 1.0, 1.0});
        x = solver.solve(b);
        expectedX = new ArrayRealVector(new double[]{0.5,
                                                   0.0,
                                                   -0.5});
        assertEquals(expectedX, x);

        b = new ArrayRealVector(new double[]{1.0, 0.0, 1.0, 1.0, 1.0});
        x = solver.solve(b);
        expectedX = new ArrayRealVector(new double[]{0.4142857142857143,
                                                    0.4142857142857143,
                                                    0.4142857142857143});
        assertEquals(expectedX, x);

        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                              {4.0, 5.0, 6.0},
                                                              {7.0, 8.0, 9.0}});
        RealMatrix B = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0},
                                                              {2.0, 2.0, 2.0},
                                                              {3.0, 3.0, 3.0}});
        RealMatrix X = solver.solve(B);

        RealMatrix expectedX2 = new Array2DRowRealMatrix(new double[][]{{-0.0726268798636081,
                                                                           0.3333333333333333,
                                                                           0.5666666666666667},
                                                                       {-0.1259342806511295,
                                                                           -0.3333333333333333,
                                                                           0.3333333333333333},
                                                                       {-0.1792416816186509,
                                                                           -0.3333333333333333,
                                                                           0.3333333333333333}});
        assertArrayEquals(expectedX2.getData(), X.getData(), 0.0001);

        RealMatrix expectedAx = new Array2DRowRealMatrix(new double[][]{{-0.046925384445316,
                                                                           2.154897684228845,
                                                                           3.5894023731365325},
                                                                        {1.781506682718624,
                                                                         6.82913731121676,
                                                                         11.48917441157438},
                                                                        {3.509751955371233,
                                                                         13.25031572593851,
                                                                         19.63952926485563}});
        RealMatrix ax = A.multiply(X);
        assertArrayEquals(expectedAx.getData(), ax.getData(), 0.0001);
    }

    @Test
    public void testSolverWithZeroMatrix() throws Exception {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        RealMatrix B = new Array2DRowRealMatrix(new double[][]{{1.0}, {0.0}, {1.0}});
        RealMatrix X = solver.solve(B);
        assertTrue(X.isIdentityMatrix());
    }

    @Test
    public void testSolverWithSingularMatrix() throws Exception {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0, 0.0},
                                                                           {0.0, 0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        RealMatrix B = new Array2DRowRealMatrix(new double[][]{{1.0}, {0.0}, {1.0}});
        RealMatrix X = solver.solve(B);
        assertTrue(X.isIdentityMatrix());
    }

    @Test
    public void testSolverWithNegativeMatrix() throws Exception {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 3.0},
                                                                           {4.0, -5.0, 6.0},
                                                                           {7.0, 8.0, -9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        RealMatrix B = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0},
                                                              {2.0, 2.0, 2.0},
                                                              {3.0, 3.0, 3.0}});
        RealMatrix X = solver.solve(B);
        assertNotNull(X);
    }

    @Test
    public void testSolverWithEmptyMatrix() throws Exception {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        RealMatrix B = new Array2DRowRealMatrix(new double[][]{});
        RealMatrix X = solver.solve(B);
        assertTrue(X.isIdentityMatrix());
    }
}