package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.62682258, -0.75680249, 0.27301866}, {-0.75680249, 0.62682258, -0.27301866}, {0.27301866, 0.27301866, 0.86602540}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 0.84857283, 0.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.22998025, -0.91083727}, {-0.91083727, 0.22998025}});
        assertEquals(expectedU, u);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
    }

    @Test
    public void testSingularValueDecompositionImplWithMaxSingularValues() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.60111277, -0.74886533, 0.29367793}, {-0.74886533, 0.60111277, -0.29367793}, {0.29367793, 0.29367793, 0.8313742}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 0.59965646, 0.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.43301273, -0.86314127}, {-0.86314127, 0.43301273}});
        assertEquals(expectedU, u);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
    }

    @Test
    public void testGetSingularValues() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();
        double[] expectedSingularValues = new double[]{1.0, 0.84857283, 0.0};
        assertArrayEquals(expectedSingularValues, singularValues);
    }

    @Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.5);
        RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{0.02441408, 0.02217577}, {0.02217577, 0.02441408}});
        assertEquals(expectedCovariance, covariance);
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetSingularValuesWithEmptyMatrix() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(MatrixUtils.createRealMatrix(new double[0][0]));
        svd.getSingularValues();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSingularValuesWithInvalidMinSingularValue() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getCovariance(-1.0);
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(1.0, svd.getNorm(), 1e-14);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(1.0 / 0.0, svd.getConditionNumber(), 1e-14);
        assertEquals(1.0 / 0.84857283, svd.getConditionNumber(), 1e-14);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(3, svd.getRank());
    }

    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        assertEquals(svd.getRank(), solver.getRank());
        RealVector b = new Array2DRowRealMatrix(new double[][]{{7.0, 8.0, 9.0}}).getColumnVector(0);
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{{-2.60417958}, {-3.19492251}, {-1.16924536}}).getColumnVector(0);
        RealVector solution = solver.solve(b);
        assertArrayEquals(expectedSolution.toArray(), solution.toArray());
    }

}