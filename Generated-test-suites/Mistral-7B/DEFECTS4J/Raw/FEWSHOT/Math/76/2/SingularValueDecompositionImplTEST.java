package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.BiDiagonalTransformer;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.MathUtils;

import java.lang.reflect.Array;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                       {4.0, 5.0, 6.0},
                                                                       {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.53846013, 0.85710181, 0.12405385},
                                                                          {-0.85710181, 0.53846013, -0.12405385},
                                                                          {-0.12405385, -0.12405385, 0.98937835}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.02698937, 0.97362732, 0.17252853},
                                                                         {0.97362732, -0.02698937, 0.17252853},
                                                                         {-0.17252853, -0.17252853, 0.99215386}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{3.74165739, 0.0, 0.0});
        assertEquals(expectedU, u);
        assertEquals(expectedV, v);
        assertEquals(expectedS, s);
        assertEquals(expectedU, ut);
        assertEquals(expectedV, vt);
    }

    @Test
    public void testSingularValueDecompositionWithMaxSingularValues() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                       {4.0, 5.0, 6.0},
                                                                       {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.53846013, 0.85710181},
                                                                          {-0.85710181, 0.53846013}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.33566106, 0.92386331},
                                                                         {-0.92386331, 0.33566106}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{3.74165739, 1.22474476});
        assertEquals(expectedU, u);
        assertEquals(expectedV, v);
        assertEquals(expectedS, s);
        assertEquals(expectedU, ut);
        assertEquals(expectedV, vt);
    }

    @Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                       {4.0, 5.0, 6.0},
                                                                       {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(1.0);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.00643743, 0.00809038},
                                                                                 {0.00809038, 0.01556948}});
        assertEquals(expectedCovariance, covariance);
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetRankZero() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                       {2.0, 2.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getRank();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCovarianceNegativeSingularValue() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                       {2.0, -2.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getCovariance(-1.0);
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                       {4.0, 5.0, 6.0},
                                                                       {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(3.74165739, svd.getNorm(), 1e-9);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                       {4.0, 5.0, 6.0},
                                                                       {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(3.74165739 / 0.0, svd.getConditionNumber(), 1e-9);
    }

    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                       {4.0, 5.0, 6.0},
                                                                       {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        double[] b = new double[]{1.0, 2.0, 3.0};
        double[] expectedResult = new double[]{1.14431856, 1.30401911, 1.46372966};
        assertEquals(expectedResult, solver.solve(b), 1e-9);
    }

    @Test
    public void testGetSolverWithNonSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                       {2.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        double[] b = new double[]{1.0, 2.0};
        double[] expectedResult = new double[]{1.0 / 2.0, 1.0 / -2.0};
        assertEquals(expectedResult, solver.solve(b), 1e-9);
    }

    @Test
    public void testGetSolverWithSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                       {2.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        double[] b = new double[]{1.0, 2.0};
        assertArrayEquals(new double[]{}, solver.solve(b), 1e-9);
    }

}