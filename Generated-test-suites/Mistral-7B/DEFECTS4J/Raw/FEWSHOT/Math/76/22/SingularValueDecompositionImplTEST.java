package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws InvalidMatrixException {
        RealMatrix matrix = Array2DRowRealMatrix.identity(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{0.841, -0.538, 0.132},
                {0.538, 0.841, 0.132}, {-0.132, 0.132, 0.966}});
        assertEquals(expectedU, u);
        assertEquals(s, MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.0, 1.0}));
        assertEquals(expectedV, v);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        svd = new SingularValueDecompositionImpl(matrix, 1);
        u = svd.getU();
        s = svd.getS();
        v = svd.getV();
        expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.75, 0.632},
                {-0.632, 0.75}});
        assertEquals(expectedU, u);
        assertEquals(s, MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 0.0}));
        assertEquals(v, MatrixUtils.createRealMatrix(new double[][]{{0.816, -0.573},
                {-0.573, 0.816}}));

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 2);
        u = svd.getU();
        s = svd.getS();
        v = svd.getV();
        expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.447, 0.812, 0.408},
                {-0.812, 0.447, -0.408}, {-0.408, -0.408, 0.812}});
        assertEquals(expectedU, u);
        assertEquals(s, MatrixUtils.createRealDiagonalMatrix(new double[]{1.414, 1.414}));
        assertEquals(v, MatrixUtils.createRealMatrix(new double[][]{{-0.756, 0.652, -0.082},
                {0.652, -0.756, 0.082}}));

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 3);
        u = svd.getU();
        s = svd.getS();
        v = svd.getV();
        expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.539, 0.573, 0.587},
                {-0.573, 0.539, -0.587}, {-0.587, -0.587, 0.539}});
        assertEquals(expectedU, u);
        assertEquals(s, MatrixUtils.createRealDiagonalMatrix(new double[]{2.165, 1.414, 0.0}));
        assertEquals(v, MatrixUtils.createRealMatrix(new double[][]{{-0.816, 0.398, -0.408},
                {0.398, -0.816, 0.408}, {0.408, 0.408, 0.816}}));
    }

    @Test
    public void testSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = Array2DRowRealMatrix.identity(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, singularValues, 0.001);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        svd = new SingularValueDecompositionImpl(matrix, 1);
        singularValues = svd.getSingularValues();
        assertArrayEquals(new double[]{1.0, 0.0}, singularValues, 0.001);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 2);
        singularValues = svd.getSingularValues();
        assertArrayEquals(new double[]{1.414, 0.0}, singularValues, 0.001);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 3);
        singularValues = svd.getSingularValues();
        assertArrayEquals(new double[]{2.165, 1.414, 0.0}, singularValues, 0.001);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = Array2DRowRealMatrix.identity(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(1.0, svd.getNorm(), 0.001);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        svd = new SingularValueDecompositionImpl(matrix, 1);
        assertEquals(1.0, svd.getNorm(), 0.001);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 2);
        assertEquals(2.236, svd.getNorm(), 0.001);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 3);
        assertEquals(3.742, svd.getNorm(), 0.001);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = Array2DRowRealMatrix.identity(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(1.0, svd.getConditionNumber(), 0.001);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        svd = new SingularValueDecompositionImpl(matrix, 1);
        assertEquals(1.0, svd.getConditionNumber(), 0.001);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 2);
        assertEquals(6.0, svd.getConditionNumber(), 0.001);

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 3);
        assertEquals(20.258, svd.getConditionNumber(), 0.001);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = Array2DRowRealMatrix.identity(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(3, svd.getRank());

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        svd = new SingularValueDecompositionImpl(matrix, 1);
        assertEquals(1, svd.getRank());

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 2);
        assertEquals(2, svd.getRank());

        matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}});
        svd = new SingularValueDecompositionImpl(matrix, 3);
        assertEquals(3, svd.getRank());
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
        RealMatrix covariance = svd.getCovariance(1.0);
        RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{1.414, 0.0},
                {0.0, 1.414}});
        assertEquals(expectedCovariance, covariance);
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetCovarianceNegativeSingularValue() throws InvalidMatrixException {
        RealMatrix matrix = Array2DRowRealMatrix.valueOf(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
        svd.getCovariance(-1.0);
    }
}