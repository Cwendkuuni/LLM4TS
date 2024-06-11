package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

/**
 * Test class for CholeskyDecompositionImpl
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix)}
     */
    @Test
    public void testCholeskyDecompositionImpl() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 4.0, 6.0}, {2.0, 6.0, 12.0}});
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomp.getL();
        RealMatrix lt = decomp.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865475, 0.7071067811865475, 0.0}, {0.4226180655752687, 0.8451345306102369, 1.4142137412141561}});
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.7071067811865475, 0.4226180655752687}, {0.7071067811865475, 0.7071067811865475, 0.8451345306102369}, {0.4226180655752687, 0.8451345306102369, 1.4142137412141561}});
        assertArrayEquals(expectedL.getData(), l.getData(), 1e-15);
        assertArrayEquals(expectedLT.getData(), lt.getData(), 1e-15);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix, double, double)}
     */
    @Test
    public void testCholeskyDecompositionImplWithCustomThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0e-8, 1.0}, {1.0e-8, 1.0, 1.0e-8}, {1.0, 1.0e-8, 1.0}});
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix, 1e-18, 1e-18);
        RealMatrix l = decomp.getL();
        RealMatrix lt = decomp.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 1.0}, {0.0, 1.0, 1.0}});
        assertArrayEquals(expectedL.getData(), l.getData(), 1e-18);
        assertArrayEquals(expectedLT.getData(), lt.getData(), 1e-18);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getDeterminant()}
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 4.0, 6.0}, {2.0, 6.0, 12.0}});
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        double determinant = decomp.getDeterminant();
        assertEquals(determinant, 48.0, 1e-15);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getL()}
     */
    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 4.0, 6.0}, {2.0, 6.0, 12.0}});
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomp.getL();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865475, 0.7071067811865475, 0.0}, {0.4226180655752687, 0.8451345306102369, 1.4142137412141561}});
        assertArrayEquals(expectedL.getData(), l.getData(), 1e-15);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getLT()}
     */
    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 4.0, 6.0}, {2.0, 6.0, 12.0}});
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = decomp.getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.7071067811865475, 0.4226180655752687}, {0.7071067811865475, 0.7071067811865475, 0.8451345306102369}, {0.4226180655752687, 0.8451345306102369, 1.4142137412141561}});
        assertArrayEquals(expectedLT.getData(), lt.getData(), 1e-15);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getSolver()}
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 4.0, 6.0}, {2.0, 6.0, 12.0}});
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomp.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector solution = solver.solve(vector);
        assertArrayEquals(new double[]{1.211088336417527, -0.1388880536947174, -0.577350269189626}, solution.toArray(), 1e-15);
    }
}