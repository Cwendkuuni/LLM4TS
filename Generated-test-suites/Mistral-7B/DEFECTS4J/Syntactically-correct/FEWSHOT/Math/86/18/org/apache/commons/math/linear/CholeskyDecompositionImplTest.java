package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

/**
 * Test case for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test Cholesky decomposition of a diagonal matrix
     */
    @Test
    public void testDiagonalMatrix() {
        double[][] data = { { 1.0, 0.0, 0.0 }, { 0.0, 2.0, 0.0 }, { 0.0, 0.0, 3.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix inverse = decomposition.getSolver().getInverse();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.707106781186548, 0.707106781186548, 0.0 }, { 0.408248290463863, 0.0, 0.816496580927726 } }, false);
        RealMatrix expectedLT = inverse.transpose();
        assertArrayEquals("L != expected L", expectedL.getData(), l.getData(), 1e-12);
        assertArrayEquals("LT != expected LT", expectedLT.getData(), lt.getData(), 1e-12);
    }

    /**
     * Test Cholesky decomposition of a symmetric matrix
     */
    @Test
    public void testSymmetricMatrix() {
        double[][] data = { { 1.0, 2.0, 2.0 }, { 2.0, 4.0, 4.0 }, { 2.0, 4.0, 9.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertNotNull("L == null", l);
        assertNotNull("LT == null", lt);
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] { { 1.0, 1.0, 1.0 }, { 1.0, 2.0, 1.0 }, { 1.0, 1.0, 3.0 } }, false);
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{ { 1.0/3.0, 1.0/3.0, 2.0/3.0 }, { 1.0/3.0, 2.0/3.0, 1.0/3.0 }, { 2.0/3.0, 1.0/3.0, 1.0 } });
        assertArrayEquals("L != expected L", expectedL.getData(), l.getData(), 1e-12);
        assertArrayEquals("LT != expected LT", expectedLT.getData(), lt.getData(), 1e-12);
    }

    /**
     * Test Cholesky decomposition of a positive definite matrix with off-diagonal elements
     */
    @Test
    public void testPosDefMatrixWithOffDiagonal() {
        double[][] data = { { 1.0, 0.2, 0.3 }, { 0.2, 2.0, 0.4 }, { 0.3, 0.4, 3.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-10, 1e-16);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertNotNull("L == null", l);
        assertNotNull("LT == null", lt);
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.532908802415152, 0.723301350765341 }, { 0.532908802415152, 1.414213741217632, 1.266286566732683 }, { 0.723301350765341, 1.266286566732683, 1.816496580927726 } }, false);
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{ { 0.577350269189626, 0.577350269189626, 0.682674154989773 }, { 0.577350269189626, 0.816496580927726, 0.734398503844379 }, { 0.682674154989773, 0.734398503844379, 1.0 } });
        assertArrayEquals("L != expected L", expectedL.getData(), l.getData(), 1e-12);
        assertArrayEquals("LT != expected LT", expectedLT.getData(), lt.getData(), 1e-12);
    }

    /**
     * Test Cholesky decomposition of a positive definite matrix with small off-diagonal elements
     */
    @Test
    public void testPosDefMatrixWithSmallOffDiagonal() {
        double[][] data = { { 1.0, 1.0e-16, 1.0e-16 }, { 1.0e-16, 1.0, 1.0e-16 }, { 1.0e-16, 1.0e-16, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-16, 1e-16);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertNotNull("L == null", l);
        assertNotNull("LT == null", lt);
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] { { 1.0, 1.0, 1.0 }, { 1.0, 1.0, 0.0 }, { 1.0, 0.0, 1.0 } }, false);
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{ { 1.0, 0.0, 1.0 }, { 1.0, 1.0, 0.0 }, { 1.0, 0.0, 1.0 } });
        assertArrayEquals("L != expected L", expectedL.getData(), l.getData(), 1e-16);
        assertArrayEquals("LT != expected LT", expectedLT.getData(), lt.getData(), 1e-16);
    }

    /**
     * Test Cholesky decomposition of a singular matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testSingularMatrix() {
        double[][] data = { { 1.0, 2.0, 2.0 }, { 2.0, 1.0, 2.0 }, { 2.0, 2.0, -1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test Cholesky decomposition of a rectangular matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testRectangularMatrix() {
        double[][] data = { { 1.0, 2.0, 2.0 }, { 2.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test getDeterminant method
     */
    @Test
    public void testGetDeterminant() {
        double[][] data = { { 1.0, 2.0, 2.0 }, { 2.0, 4.0, 4.0 }, { 2.0, 4.0, 9.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = decomposition.getDeterminant();
        assertEquals("Determinant != expected determinant", 144.0, determinant, 1e-12);
    }

    /**
     * Test getSolver method
     */
    @Test
    public void testGetSolver() {
        double[][] data = { { 1.0, 2.0, 2.0 }, { 2.0, 4.0, 4.0 }, { 2.0, 4.0, 9.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomposition.getSolver();
        RealVector vector = new RealVectorImpl(new double[] { 1.0, 2.0, 3.0 }, false);
        RealVector result = solver.solve(vector);
        RealVector expectedResult = new RealVectorImpl(new double[] { 0.333333333333333, 0.5, 0.666666666666667 }, false);
        assertArrayEquals("Solver result != expected result", expectedResult.getData(), result.getData(), 1e-12);
    }

}