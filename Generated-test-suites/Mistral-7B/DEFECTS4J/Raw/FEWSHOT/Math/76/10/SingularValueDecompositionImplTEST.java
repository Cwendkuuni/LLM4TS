package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedUSV = MatrixUtils.createRealMatrix(new double[][]{{0.44954444, 0.71255555, 0.43301273},
                {-0.71255555, 0.44954444, -0.43301273}, {-0.43301273, -0.43301273, 0.88888889}});
        RealMatrix expectedUSVTranspose = MatrixUtils.createRealMatrix(new double[][]{{0.44954444, -0.71255555, -0.43301273},
                {0.71255555, 0.44954444, 0.43301273}, {0.43301273, -0.43301273, 0.88888889}});

        assertEquals(expectedUSV, u.multiply(s).multiply(v));
        assertEquals(expectedUSVTranspose, v.transpose().multiply(u.transpose()));
    }

    @Test
    public void testSingularValueDecompositionImplZeroMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[0][0]);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertTrue(svd.getU() instanceof RealMatrix);
        assertTrue(svd.getS() instanceof RealDiagonalMatrix);
        assertTrue(svd.getV() instanceof RealMatrix);
        assertTrue(svd.getU().getRowDimension() == 0);
        assertTrue(svd.getU().getColumnDimension() == 0);
        assertTrue(svd.getS().getRowDimension() == 0);
        assertTrue(svd.getS().getColumnDimension() == 0);
        assertTrue(svd.getV().getRowDimension() == 0);
        assertTrue(svd.getV().getColumnDimension() == 0);
    }

    @Test
    public void testSingularValueDecompositionImplIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedUSV = MatrixUtils.createIdentityMatrix(3);

        assertEquals(expectedUSV, u.multiply(s).multiply(v));
        assertEquals(expectedUSV, v.transpose().multiply(u.transpose()));
    }

    @Test
    public void testSingularValueDecompositionImplSingularMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedUSV = MatrixUtils.createRealMatrix(new double[][]{{0.70710678, 0.70710678},
                {0.70710678, -0.70710678}});

        assertEquals(expectedUSV, u.multiply(s).multiply(v));
        assertEquals(expectedUSV.transpose(), v.transpose().multiply(u.transpose()));
    }

    @Test
    public void testSingularValueDecompositionImplTruncatedSVD() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedUSV = MatrixUtils.createRealMatrix(new double[][]{{0.44954444, 0.71255555, 0.43301273},
                {-0.71255555, 0.44954444, -0.43301273}, {-0.43301273, -0.43301273, 0.88888889}});
        RealMatrix expectedUSVTruncated = MatrixUtils.createRealMatrix(new double[][]{{0.44954444, 0.71255555},
                {-0.71255555, 0.44954444}});

        assertEquals(expectedUSV, u.multiply(s).multiply(v));
        assertEquals(expectedUSVTruncated, svd.getU().multiply(svd.getS()).multiply(svd.getV()));
    }

    @Test
    public void testSingularValueDecompositionImplGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();
        double expectedFirstSingularValue = Math.sqrt(5.0);
        double expectedSecondSingularValue = Math.sqrt(2.0);

        assertEquals(expectedFirstSingularValue, singularValues[0], 0.00001);
        assertEquals(expectedSecondSingularValue, singularValues[1], 0.00001);
    }

    @Test
    public void testSingularValueDecompositionImplGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double actualNorm = svd.getNorm();
        double expectedNorm = Math.sqrt(5.0);

        assertEquals(expectedNorm, actualNorm, 0.00001);
    }

    @Test
    public void testSingularValueDecompositionImplGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double actualConditionNumber = svd.getConditionNumber();
        double expectedConditionNumber = Math.sqrt(5.0) / Math.sqrt(2.0);

        assertEquals(expectedConditionNumber, actualConditionNumber, 0.00001);
    }

    @Test
    public void testSingularValueDecompositionImplGetRank() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int actualRank = svd.getRank();
        int expectedRank = 3;

        assertEquals(expectedRank, actualRank);
    }

    @Test
    public void testSingularValueDecompositionImplGetRankZero() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int actualRank = svd.getRank();
        int expectedRank = 0;

        assertEquals(expectedRank, actualRank);
    }

    @Test
    public void testSingularValueDecompositionImplCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.01);
        double[] singularValues = svd.getSingularValues();
        double expectedFirstSingularValue = singularValues[0];
        double expectedSecondSingularValue = singularValues[1];
        double expectedThirdSingularValue = singularValues[2];

        assertEquals(expectedFirstSingularValue * expectedFirstSingularValue, covariance.getEntry(0, 0), 0.00001);
        assertEquals(expectedSecondSingularValue * expectedSecondSingularValue, covariance.getEntry(1, 1), 0.00001);
        assertEquals(expectedThirdSingularValue * expectedThirdSingularValue, covariance.getEntry(2, 2), 0.00001);
    }

    @Test
    public void testSingularValueDecompositionImplCovarianceZeroMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[0][0]);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.0);

        assertTrue(covariance.isEmpty());
    }

    @Test
    public void testSingularValueDecompositionImplSolver() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        double[] b = {1, 2};
        double[] x = solver.solve(b);
        double expectedX = (-2.0 / Math.sqrt(5.0)) * b[0] + (1.0 / Math.sqrt(5.0)) * b[1];

        assertEquals(expectedX, x[0], 0.00001);
        assertEquals(0.0, x[1], 0.00001);
    }

    @Test
    public void testSingularValueDecompositionImplInverse() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix inverse = svd.getInverse();
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(new double[][]{{-2.0 / Math.sqrt(5.0), 1.0 / Math.sqrt(5.0)},
                {1.0 / Math.sqrt(5.0), -2.0 / Math.sqrt(5.0)}});

        assertEquals(expectedInverse, inverse);
    }

}