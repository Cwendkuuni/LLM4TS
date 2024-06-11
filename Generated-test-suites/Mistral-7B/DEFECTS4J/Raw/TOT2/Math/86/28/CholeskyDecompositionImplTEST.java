package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SolverCondition;
import org.apache.commons.math3.linear.util.SingularValueDecompositionImpl;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test(expected = NotSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithSymmetricButNotPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, 0.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        RealMatrix inverseL = choleskyDecomposition.getSolver().getInverse();
        assertEquals(l.transpose().multiply(lt), MatrixUtils.createIdentityMatrix(2));
        assertArrayEquals(l.multiply(inverseL), MatrixUtils.createIdentityMatrix(2));
        assertEquals(choleskyDecomposition.getDeterminant(), 6.0);
    }

    @Test
    public void testSolverWithVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0}, {1.0, 4.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0}, false);
        RealVector x = solver.solve(b);
        assertEquals(x.getDimension(), 2);
        assertEquals(x.getEntry(0), -0.16666666666666663);
        assertEquals(x.getEntry(1), -0.16666666666666663);
    }

    @Test
    public void testSolverWithRealVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0}, {1.0, 4.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new Array2DRowRealVector(new double[]{1.0, 1.0});
        RealVector x = solver.solve(b);
        assertEquals(x.getDimension(), 2);
        assertEquals(x.getEntry(0), -0.16666666666666663, 1e-12);
        assertEquals(x.getEntry(1), -0.16666666666666663, 1e-12);
    }

    @Test
    public void testSolverWithMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0}, {1.0, 4.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RealMatrix x = solver.solve(b);
        assertEquals(x.getRowDimension(), 2);
        assertEquals(x.getColumnDimension(), 2);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(x.getEntry(i, j), -0.16666666666666663, 1e-12);
            }
        }
    }

    @Test
    public void testSolverWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0}, false);
        try {
            solver.solve(b);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testSolverWithSingularMatrix2() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealMatrix b = MatrixUtils.createRealIdentityMatrix(2);
        try {
            solver.solve(b);
            fail("Expected SingularValueDecomposition required for singular matrices");
        } catch (IllegalArgumentException e) {
            //expected
        }
        SingularValueDecomposition svd = new SingularValueDecompositionImpl(matrix, SolverCondition.LU_COMPUTATION);
        RealMatrix x = svd.getSolver().solve(b);
        assertEquals(x.getRowDimension(), 2);
        assertEquals(x.getColumnDimension(), 2);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(x.getEntry(i, j), 0.5 * (Precision.isClose(svd.getS()
                        .getEntry(i, i), 0.0) ? 0.0 : 1.0 / svd.getS().getEntry(i, i)), 1e-12);
            }
        }
    }

}