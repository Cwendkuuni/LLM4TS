package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        assertEquals(matrix.getRowDimension(), l.getRowDimension());
        assertEquals(matrix.getColumnDimension(), l.getColumnDimension());
        assertNotNull(l);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = decomposition.getLT();
        assertEquals(matrix.getColumnDimension(), lt.getColumnDimension());
        assertEquals(matrix.getRowDimension(), lt.getRowDimension());
        assertNotNull(lt);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = null;
        try {
            decomposition = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            // expected
        }
        assertNull(decomposition);
        if (decomposition == null) {
            matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 3.0}});
            decomposition = new CholeskyDecompositionImpl(matrix);
        }
        double determinant = decomposition.getDeterminant();
        assertEquals("Determinant should be 2.0", 2.0, determinant, 1e-15);
    }

    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = decomposition.getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[]{0.428571429, 0.571428571, 1.0});
        assertArrayEquals("Solver test failed", expected.toArray(), x.toArray(), 1e-15);
    }

    @Test
    public void testSolverIdentity() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(2);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(2);
        RealMatrix x = decomposition.getSolver().solve(identity);
        assertEquals("Inverse should be identity matrix", identity, x);
    }
}