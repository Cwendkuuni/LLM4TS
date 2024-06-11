package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        assertNotNull(decomposition.getL());
        assertNotNull(decomposition.getLT());
        assertEquals(3, decomposition.getL().getRowDimension());
        assertEquals(3, decomposition.getL().getColumnDimension());
        assertEquals(3, decomposition.getLT().getRowDimension());
        assertEquals(3, decomposition.getLT().getColumnDimension());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}, {2.0, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 2.0}, {2.0, -1.0, 3.0}, {2.0, 3.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedL = new LUDecomposition(matrix).getSolver().getInverse();
        RealMatrix actualL = decomposition.getL();
        assertArrayEquals(expectedL.getData(), actualL.getData(), 0.0);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedLT = decomposition.getLT();
        RealMatrix actualLT = decomposition.getLT();
        assertArrayEquals(expectedLT.getData(), actualLT.getData(), 0.0);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        double expectedDeterminant = 1.0 * 3.0 * 4.0 * 3.0 * 2.0 * 2.0;
        double actualDeterminant = decomposition.getDeterminant();
        assertEquals(expectedDeterminant, actualDeterminant, 0.0);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        Solver solver = decomposition.getSolver();
        assertNotNull(solver);
        DecompositionSolver expectedSolver = new LUDecomposition(matrix).getSolver();
        assertEquals(expectedSolver.getClass(), solver.getClass());
    }

    @Test
    public void testSolveVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, -1.0}, {1.0, 9.0, 2.0}, {-1.0, 2.0, 16.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector vector = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector expectedResult = new RealVectorImpl(new double[]{0.382683432365843, -0.082683432365843, 0.774602526641493});
        RealVector result = decomposition.getSolver().solve(vector);
        assertEquals(expectedResult.getClass(), result.getClass());
        assertArrayEquals(expectedResult.getData(), result.getData(), 0.0);
    }

    @Test
    public void testSolveMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, -1.0}, {1.0, 9.0, 2.0}, {-1.0, 2.0, 16.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedResult = new MatrixUtils.createRealMatrix(new double[][]{{3.0, 0.5, -0.5}, {0.5, 3.0, 0.5}, {-0.5, 0.5, 3.0}});
        RealMatrix result = decomposition.getSolver().solve(MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}, {2.0, 1.0}}));
        assertArrayEquals(expectedResult.getData(), result.getData(), 0.0);
    }

}