package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.exception.NotSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.exception.InvalidMatrixException;

/**
 * Unit tests for CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test constructor CholeskyDecompositionImpl(RealMatrix matrix).
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testConstructor() throws Exception {
        double[][] matrixData = { {1.0, 0.0, 0.0}, {0.0, 2.0, 1.0}, {0.0, 1.0, 3.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        // Test getters
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        double determinant = decomposition.getDeterminant();
        DecompositionSolver solver = decomposition.getSolver();
        // Test L and LT are equals
        assertEquals(l, lt.transpose());
        // Test determinant is correct
        assertEquals(determinant, 1.0 * 2.0 * 3.0, 1e-15);
        // Test solver
        double[] b = {1.0, 2.0, 3.0};
        RealVector x = solver.solve(new RealVectorImpl(b, false));
        assertEquals(x.getDimension(), 3, 0);
        assertEquals(x.getEntry(0), 1.0 / Math.sqrt(1.0), 1e-15);
        assertEquals(x.getEntry(1), (2.0 - 1.0 / Math.sqrt(1.0)) / Math.sqrt(2.0), 1e-15);
        assertEquals(x.getEntry(2), (3.0 - 1.0 / Math.sqrt(1.0) - (2.0 - 1.0 / Math.sqrt(1.0)) / Math.sqrt(2.0)) / Math.sqrt(3.0), 1e-15);
    }

    /**
     * Test constructor CholeskyDecompositionImpl(RealMatrix matrix, double relativeSymmetryThreshold, double absolutePositivityThreshold).
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testConstructorWithThresholds() throws Exception {
        double[][] matrixData = { {1.0e-10, 0.0, 0.0}, {0.0, 2.0e-10, 1.0e-10}, {0.0, 1.0e-10, 3.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-8, 1.0e-6);
        // Test getters
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        double determinant = decomposition.getDeterminant();
        DecompositionSolver solver = decomposition.getSolver();
        // Test L and LT are equals
        assertEquals(l, lt.transpose());
        // Test determinant is correct
        assertEquals(determinant, 1.0 * 2.0 * 3.0, 1e-14);
        // Test solver
        double[] b = {1.0, 2.0, 3.0};
        RealVector x = solver.solve(new RealVectorImpl(b, false));
        assertEquals(x.getDimension(), 3, 0);
        assertEquals(x.getEntry(0), 1.0 / Math.sqrt(1.0), 1e-15);
        assertEquals(x.getEntry(1), (2.0 - 1.0 / Math.sqrt(1.0)) / Math.sqrt(2.0), 1e-15);
        assertEquals(x.getEntry(2), (3.0 - 1.0 / Math.sqrt(1.0) - (2.0 - 1.0 / Math.sqrt(1.0)) / Math.sqrt(2.0)) / Math.sqrt(3.0), 1e-15);
    }

    /**
     * Test constructor CholeskyDecompositionImpl(RealMatrix matrix, double relativeSymmetryThreshold, double absolutePositivityThreshold) with non-symmetric matrix.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() throws Exception {
        double[][] matrixData = { {1.0, 0.0, 1.0}, {0.0, 2.0, 0.0}, {1.0, 0.0, 3.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-10);
    }

    /**
     * Test constructor CholeskyDecompositionImpl(RealMatrix matrix, double relativeSymmetryThreshold, double absolutePositivityThreshold) with non-positive definite matrix.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() throws Exception {
        double[][] matrixData = { {1.0, 0.0, 0.0}, {0.0, -1.0, 1.0}, {0.0, 1.0, -1.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-10);
    }

    /**
     * Test constructor CholeskyDecompositionImpl(RealMatrix matrix, double relativeSymmetryThreshold, double absolutePositivityThreshold) with square non-matrix.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() throws Exception {
        double[][] matrixData = { {1.0, 0.0}, {0.0, 1.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-10);
    }

}