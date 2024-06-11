package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the constructor with a valid square matrix
     */
    @Test
    public void testValidConstruction() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        // Basic check
        RealMatrix l = decomp.getL();
        Assert.assertEquals(l.getRowDimension(), l.getColumnDimension());
        DecompositionSolver solver = decomp.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector solution = solver.solve(vector);
        Assert.assertArrayEquals(new double[]{1.0, 0.0, 0.0}, solution.toArray(), 0.0);
    }

    /**
     * Test the constructor with a non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testInvalidConstruction() {
        double[][] matrixData = {{1.0, 2.0}, {2.0, 3.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a non-symmetric matrix
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
        double[][] lTData = matrix.getData();
        // Check off-diagonal elements
        Assert.assertFalse(Math.abs(lTData[0][1] - lTData[1][0]) <= 1e-15);
        Assert.assertFalse(Math.abs(lTData[1][2] - lTData[2][1]) <= 1e-15);
    }

    /**
     * Test the constructor with a non-positive definite matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, -1.0, 3.0}, {2.0, 3.0, -1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    /**
     * Test the getL() method
     */
    @Test
    public void testGetL() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomp.getL();
        Assert.assertEquals(l.getRowDimension(), l.getColumnDimension());
        Assert.assertArrayEquals(l.getData(), decomp.lTData, 0.0);
    }

    /**
     * Test the getLT() method
     */
    @Test
    public void testGetLT() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = decomp.getLT();
        Assert.assertEquals(lt.getRowDimension(), lt.getColumnDimension());
        Assert.assertArrayEquals(lt.getData(), matrix.transpose().getData(), 0.0);
    }

    /**
     * Test the getDeterminant() method
     */
    @Test
    public void testGetDeterminant() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        double determinant = decomp.getDeterminant();
        Assert.assertEquals(determinant, 2.0 * Math.pow(4.0, 1.5), 1e-15);
    }

    /**
     * Test the getSolver() method
     */
    @Test
    public void testGetSolver() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomp.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector solution = solver.solve(vector);
        Assert.assertArrayEquals(new double[]{1.0, 0.0, 0.0}, solution.toArray(), 0.0);
    }
}