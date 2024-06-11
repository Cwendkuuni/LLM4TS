package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    /**
     * Test CholeskyDecompositionImpl constructor with a valid square, symmetric, positive definite matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testValidCholeskyDecomposition() throws Exception {
        double[][] matrixData = {
                {4.0, 2.0, 2.0},
                {2.0, 4.0, 2.0},
                {2.0, 2.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        assertNotNull(l);
        RealMatrix lt = choleskyDecomposition.getLT();
        assertNotNull(lt);
        RealMatrix inverseL = l.getInverse();
        RealMatrix expectedInverseL = new Array2DRowRealMatrix(new double[][]{{1.0/Math.sqrt(4.0), 0.0, 0.0},
                {-0.5/Math.sqrt(4.0), 1.0/Math.sqrt(4.0), 0.0},
                {-0.5/Math.sqrt(4.0), -0.5/Math.sqrt(4.0), 1.0/Math.sqrt(4.0)}});
        assertEquals(expectedInverseL, inverseL);
        RealMatrix lMultiplyLT = l.multiply(lt);
        RealMatrix expectedIdentity = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}});
        assertEquals(expectedIdentity, lMultiplyLT);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with a non-square matrix.
     * @throws Exception if an error occurs
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() throws Exception {
        double[][] matrixData = {
                {4.0, 2.0},
                {2.0, 4.0},
                {2.0, 2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with a non-symmetric matrix.
     * @throws Exception if an error occurs
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() throws Exception {
        double[][] matrixData = {
                {4.0, 2.0},
                {2.0, 3.0},
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with a non-positive definite matrix.
     * @throws Exception if an error occurs
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() throws Exception {
        double[][] matrixData = {
                {-1.0, 2.0},
                {2.0, -1.0},
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with a valid square, symmetric, positive definite matrix and custom thresholds.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomCholeskyDecomposition() throws Exception {
        double[][] matrixData = {
                {1.0e-12, 2.0e-12, 2.0e-12},
                {2.0e-12, 4.0, 2.0e-12},
                {2.0e-12, 2.0e-12, 4.0},
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        double relativeSymmetryThreshold = 1.0e-8;
        double absolutePositivityThreshold = 0.0;
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, relativeSymmetryThreshold, absolutePositivityThreshold);
        RealMatrix l = choleskyDecomposition.getL();
        assertNotNull(l);
        RealMatrix lt = choleskyDecomposition.getLT();
        assertNotNull(lt);
        RealMatrix inverseL = l.getInverse();
        // checking if inverseL is equal to identity, which is the expected result
        RealMatrix expectedIdentity = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}});
        assertEquals(expectedIdentity, inverseL);
        RealMatrix lMultiplyLT = l.multiply(lt);
        assertEquals(expectedIdentity, lMultiplyLT);
    }

    /**
     * Test Solver class
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolver() throws Exception {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 2.0},
                {3.0, 2.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        Solver solver = new Solver(lTData);
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{2.0/3.0, -1.0/3.0, 1.0/3.0});
        assertArrayEquals(expectedX.toArray(), x.toArray());
    }
}