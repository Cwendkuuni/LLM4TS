package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link CholeskyDecompositionImpl}.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(createIdentityMatrix(2));
        CholeskyDecomposition cholesky = decomp.getCholesky();
        assertTrue(cholesky.isSymmetric());
        assertTrue(cholesky.isPositiveDefinite());
        RealVector x = cholesky.solve(new RealVectorImpl(new double[] {1.0, 1.0}, false));
        assertEquals(new double[] {1.0, 1.0}, x.toArray(), 1e-14);
    }

    /**
     * Test the constructor with custom tolerance values.
     */
    @Test
    public void testCustomToleranceValues() {
        RealMatrix matrix = createSymmetricMatrix(2, 1e-8, 1e-12);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix, 1e-13, 1e-16);
        CholeskyDecomposition cholesky = decomp.getCholesky();
        assertTrue(cholesky.isSymmetric());
        assertTrue(cholesky.isPositiveDefinite());
        RealVector x = cholesky.solve(new RealVectorImpl(new double[] {1.0, 1.0}, false));
        assertEquals(new double[] {1.0, 1.0}, x.toArray(), 1e-16);
    }

    /**
     * Test that a non-square matrix throws an exception.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = createMatrix(2, 3);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test that a non-symmetric matrix throws an exception.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = createMatrix(2, 2, 1.0);
        matrix.setEntry(1, 1, 1.2);
        matrix.setEntry(1, 2, 1.1);
        matrix.setEntry(2, 1, 0.9);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test that a non-positive definite matrix throws an exception.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = createMatrix(2, 2, -1.0);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the getL() method.
     */
    @Test
    public void testGetL() {
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(createIdentityMatrix(2));
        CholeskyDecomposition cholesky = decomp.getCholesky();
        RealMatrix l = cholesky.getL();
        assertTrue(l.isSymmetric());
        assertTrue(l.isUpperTriangular());
        assertEquals(l.getRowDimension(), l.getColumnDimension());
    }

    /**
     * Test the getLT() method.
     */
    @Test
    public void testGetLT() {
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(createIdentityMatrix(2));
        CholeskyDecomposition cholesky = decomp.getCholesky();
        RealMatrix lt = cholesky.getLT();
        assertTrue(lt.isLowerTriangular());
        assertTrue(lt.isTranspose(cholesky.getL()));
        assertEquals(lt.getRowDimension(), lt.getColumnDimension());
    }

    /**
     * Test the getDeterminant() method.
     */
    @Test
    public void testGetDeterminant() {
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(createIdentityMatrix(2));
        CholeskyDecomposition cholesky = decomp.getCholesky();
        assertEquals(1.0, cholesky.getDeterminant(), 1e-14);
    }

    /**
     * Test the getSolver() method.
     */
    @Test
    public void testGetSolver() {
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(createIdentityMatrix(2));
        CholeskyDecomposition cholesky = decomp.getCholesky();
        DecompositionSolver solver = cholesky.getSolver();
        RealVector x = solver.solve(new RealVectorImpl(new double[] {1.0, 1.0}, false));
        assertEquals(new double[] {1.0, 1.0}, x.toArray(), 1e-14);
    }

    /**
     * Create a matrix of given dimensions with given values.
     * @param rows number of rows
     * @param cols number of columns
     * @param value the value for the identity elements
     * @return the created matrix
     */
    private RealMatrix createMatrix(int rows, int cols, double value) {
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; ++i) {
            data[i][i] = value;
        }
        return MatrixUtils.createRealMatrix(data);
    }

    /**
     * Create a symmetric matrix of given dimensions with given tolerance values.
     * @param rows number of rows
     * @param relSymmetryThreshold relative symmetry threshold
     * @param absPositivityThreshold absolute positivity threshold
     * @return the created matrix
     */
    private RealMatrix createSymmetricMatrix(int rows, double relSymmetryThreshold, double absPositivityThreshold) {
        double[][] data = new double[rows][rows];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j <= i; ++j) {
                data[i][j] = Math.min(Math.max(Math.random(), -Math.random()),
                        Math.max(Math.abs(Math.random()) * (1.0 + relSymmetryThreshold),
                                absPositivityThreshold));
                data[j][i] = data[i][j];
            }
        }
        return MatrixUtils.createRealMatrix(data);
    }

    /**
     * Create an identity matrix of given dimensions.
     * @param rows number of rows
     * @return the created matrix
     */
    private RealMatrix createIdentityMatrix(int rows) {
        double[][] data = new double[rows][rows];
        for (int i = 0; i < rows; ++i) {
            data[i][i] = 1.0;
        }
        return MatrixUtils.createRealMatrix(data);
    }

}