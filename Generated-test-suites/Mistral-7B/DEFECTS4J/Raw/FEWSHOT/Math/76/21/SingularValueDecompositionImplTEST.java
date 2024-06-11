package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        double[] singularValues = svd.getSingularValues();

        assertEquals(2, u.getRowDimension());
        assertEquals(2, u.getColumnDimension());
        assertEquals(2, s.getRowDimension());
        assertEquals(2, s.getColumnDimension());
        assertEquals(2, v.getRowDimension());
        assertEquals(2, v.getColumnDimension());
        assertEquals(2, ut.getRowDimension());
        assertEquals(2, ut.getColumnDimension());
        assertEquals(2, vt.getRowDimension());
        assertEquals(2, vt.getColumnDimension());

        assertEquals(1.0, u.getEntry(0, 0), 1e-14);
        assertEquals(1.0 / Math.sqrt(2.0), u.getEntry(0, 1), 1e-14);
        assertEquals(1.0 / Math.sqrt(2.0), u.getEntry(1, 0), 1e-14);
        assertEquals(-1.0 / Math.sqrt(2.0), u.getEntry(1, 1), 1e-14);

        assertEquals(Math.sqrt(2.0), singularValues[0], 1e-14);
        assertEquals(0.0, singularValues[1], 1e-14);

        assertEquals(Math.sqrt(2.0), s.getEntry(0, 0), 1e-14);
        assertEquals(0.0, s.getEntry(1, 1), 1e-14);

        assertEquals(Math.sqrt(2.0) / 2.0, v.getEntry(0, 0), 1e-14);
        assertEquals(Math.sqrt(2.0) / 2.0, v.getEntry(1, 1), 1e-14);
        assertEquals(-Math.sqrt(2.0) / 2.0, v.getEntry(0, 1), 1e-14);
        assertEquals(Math.sqrt(2.0) / 2.0, v.getEntry(1, 0), 1e-14);

        assertEquals(1.0 / Math.sqrt(2.0), ut.getEntry(0, 0), 1e-14);
        assertEquals(1.0 / Math.sqrt(2.0), ut.getEntry(1, 1), 1e-14);
        assertEquals(-1.0 / Math.sqrt(2.0), ut.getEntry(0, 1), 1e-14);
        assertEquals(1.0 / Math.sqrt(2.0), ut.getEntry(1, 0), 1e-14);

        assertEquals(1.0 / Math.sqrt(2.0), vt.getEntry(0, 0), 1e-14);
        assertEquals(1.0 / Math.sqrt(2.0), vt.getEntry(1, 1), 1e-14);
        assertEquals(-1.0 / Math.sqrt(2.0), vt.getEntry(0, 1), 1e-14);
        assertEquals(1.0 / Math.sqrt(2.0), vt.getEntry(1, 0), 1e-14);

        assertArrayEquals(new double[]{1.0, 1.0}, u.multiply(s).multiply(v).getColumnVector(0), 1e-14);
        assertArrayEquals(new double[]{1.0, 2.0}, matrix.multiply(v).getColumnVector(0), 1e-14);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testInvalidMatrixException() throws Exception {
        new SingularValueDecompositionImpl(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}));
    }

    @Test
    public void testSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new Array2DRowRealVector(new double[]{1.0, 2.0});

        double[] x = solver.solve(b.toArray());
        RealVector expected = new Array2DRowRealVector(new double[]{2.0 / 3.0, 1.0 / 3.0});
        assertEquals(expected.toString(), x.toString(), 1e-14);

        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        RealMatrix X = A.multiply(solver.getInverse());
        assertArrayEquals(X.getColumnVector(0).toArray(), x, 1e-14);
    }

    @Test
    public void testRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 2.0, 2.0}, {1.0, 2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        int rank = svd.getRank();
        assertTrue(rank == 2);
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        assertEquals(Math.sqrt(2.0), norm, 1e-14);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        assertEquals(Math.sqrt(2.0) / Math.sqrt(0.0), conditionNumber, 1e-14);
    }

    @Test
    public void testCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 2.0, 2.0}, {1.0, 2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix covariance = svd.getCovariance(1e-10);

        double[][] data = covariance.getData();
        assertEquals(2, data[0].length);
        assertEquals(2, data[1].length);
        assertEquals(0.25, data[0][0], 1e-14);
        assertEquals(0.25, data[0][1], 1e-14);
        assertEquals(0.25, data[1][0], 1e-14);
        assertEquals(0.25, data[1][1], 1e-14);
    }
}