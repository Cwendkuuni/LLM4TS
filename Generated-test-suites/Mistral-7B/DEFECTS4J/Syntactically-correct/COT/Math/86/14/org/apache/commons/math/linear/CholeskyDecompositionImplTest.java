package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructorWithDefaultThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        // Test getL() and getLT() methods
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1, 0}, {1, 1}});
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lT = choleskyDecomposition.getLT();
        assertEquals(expectedLT, lT);
        assertEquals(lT, l.transpose());
    }

    @Test
    public void testConstructorWithCustomThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1e-6}, {1, 1}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1e-10, 1e-15);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lT = choleskyDecomposition.getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1, 0}, {1, 1}});
        assertEquals(expectedLT, lT);
        assertEquals(lT, l.transpose());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}, {1, 1}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 1}});
        new CholeskyDecompositionImpl(matrix, 1e-10, 1e-15);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1, 1}, {1, -1}});
        new CholeskyDecompositionImpl(matrix, 1e-10, 1e-15);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        assertEquals(1.0, determinant, 1e-12);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        // Test solve(RealVector) method
        RealVector vector = new RealVectorImpl(new double[]{1, 1});
        RealVector result = solver.solve(vector);
        RealVector expected = new RealVectorImpl(new double[]{1, 1});
        assertEquals(expected, result);
        // Test solve(double[]) method
        double[] array = {1, 1};
        RealVector result2 = solver.solve(new RealVectorImpl(array, false));
        RealVector expected2 = new RealVectorImpl(array, false);
        assertEquals(expected2, result2);
        // Test solve(RealMatrix) method
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        RealMatrix result3 = solver.solve(matrix2);
        RealMatrix expected3 = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        assertEquals(expected3, result3);
    }

}