package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    /**
     * Test CholeskyDecompositionImpl constructor with default threshold values
     * @throws Exception
     */
    @Test
    public void testDefaultConstructor() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.7071067811865476, 0.0}, {-0.7071067811865476, -0.7071067811865476, 0.8164965809277261}});
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.7071067811865476, -0.7071067811865476}, {0.7071067811865476, 0.5, 0.5}, {-0.7071067811865476, 0.5, 0.5}});
        assertEquals(expectedL.getRowDimension(), l.getRowDimension());
        assertEquals(expectedL.getColumnDimension(), l.getColumnDimension());
        assertEquals(expectedLT.getRowDimension(), lt.getRowDimension());
        assertEquals(expectedLT.getColumnDimension(), lt.getColumnDimension());
        assertArrayEquals(expectedL.getData(), l.getData(), 1e-14);
        assertArrayEquals(expectedLT.getData(), lt.getData(), 1e-14);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with custom threshold values
     * @throws Exception
     */
    @Test
    public void testCustomConstructor() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-10, 1e-10);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.7071067811865476, 0.0}, {-0.7071067811865476, -0.7071067811865476, 0.8164965809277261}});
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.7071067811865476, -0.7071067811865476}, {0.7071067811865476, 0.5, 0.5}, {-0.7071067811865476, 0.5, 0.5}});
        assertEquals(expectedL.getRowDimension(), l.getRowDimension());
        assertEquals(expectedL.getColumnDimension(), l.getColumnDimension());
        assertEquals(expectedLT.getRowDimension(), lt.getRowDimension());
        assertEquals(expectedLT.getColumnDimension(), lt.getColumnDimension());
        assertArrayEquals(expectedL.getData(), l.getData(), 1e-10);
        assertArrayEquals(expectedLT.getData(), lt.getData(), 1e-10);
    }

    /**
     * Test CholeskyDecompositionImpl exception handling for non-square matrix
     * @throws Exception
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1, 1.0, 0.1}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl exception handling for non-symmetric matrix
     * @throws Exception
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.1}});
        new CholeskyDecompositionImpl(matrix, 1e-15, 1e-10);
    }

    /**
     * Test CholeskyDecompositionImpl exception handling for non-positive definite matrix
     * @throws Exception
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, -1.0, 0.1}, {0.1, 0.1, -1.0}});
        new CholeskyDecompositionImpl(matrix, 1e-15, 1e-10);
    }

    /**
     * Test getDeterminant method
     * @throws Exception
     */
    @Test
    public void testGetDeterminant() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double determinant = cholesky.getDeterminant();
        assertEquals(1.0, determinant, 1e-15);
    }

    /**
     * Test getSolver method
     * @throws Exception
     */
    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        assertNotNull(solver);
    }

    /**
     * Test Solver.isNonSingular method
     * @throws Exception
     */
    @Test
    public void testSolverIsNonSingular() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        assertTrue(solver.isNonSingular());
    }

    /**
     * Test Solver.solve method for vector
     * @throws Exception
     */
    @Test
    public void testSolverSolveVector() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{3.1622776601683795, 1.5811388252211904, 0.1648433612793394});
        assertEquals(expectedX.getDimension(), x.getDimension());
        assertArrayEquals(expectedX.getData(), x.getData(), 1e-15);
    }

    /**
     * Test Solver.solve method for matrix
     * @throws Exception
     */
    @Test
    public void testSolverSolveMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix x = solver.solve(b);
        RealMatrix expectedX = new Array2DRowRealMatrix(new double[][]{{3.1622776601683795, 1.5811388252211904}, {-0.2448937867553362, -0.8398955652632578}});
        assertEquals(expectedX.getRowDimension(), x.getRowDimension());
        assertEquals(expectedX.getColumnDimension(), x.getColumnDimension());
        assertArrayEquals(expectedX.getData(), x.getData(), 1e-15);
    }

    /**
     * Test Solver.getInverse method
     * @throws Exception
     */
    @Test
    public void testSolverGetInverse() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        RealMatrix inverse = solver.getInverse();
        RealMatrix expectedInverse = new Array2DRowRealMatrix(new double[][]{{1.7320508075688772, 0.2448937867553362, 0.2448937867553362}, {-0.2448937867553362, 1.4142137412503161, 0.8398955652632578}, {-0.2448937867553362, 0.8398955652632578, 1.7320508075688772}});
        assertEquals(expectedInverse.getRowDimension(), inverse.getRowDimension());
        assertEquals(expectedInverse.getColumnDimension(), inverse.getColumnDimension());
        assertArrayEquals(expectedInverse.getData(), inverse.getData(), 1e-15);
    }

}