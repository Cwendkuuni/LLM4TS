package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{-0.7071067811865476, 0.7071067811865476},
                {0.7071067811865476, 0.7071067811865476}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{1.414213741059603}, {1.414213741059603}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.7071067811865476, 0.7071067811865476},
                {0.7071067811865476, -0.7071067811865476}});
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix result = u.multiply(s).multiply(v.transpose());
        Assert.assertEquals(expectedU, u);
        Assert.assertEquals(expectedS, s);
        Assert.assertEquals(expectedV, v);
        Assert.assertEquals(expectedU, result.multiply(v).multiply(s).multiply(u.transpose()));
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionWithInvalidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        Assert.assertEquals(2, rank);
    }

    @Test
    public void testGetSingularValues() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        double[] singularValues = svd.getSingularValues();
        Assert.assertEquals(2, singularValues.length);
        Assert.assertEquals(1.4142135625, singularValues[0], 0.00001);
        Assert.assertEquals(1.4142135625, singularValues[1], 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCovarianceWithNegativeMinSingularValue() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getCovariance(-1.0);
    }

    @Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix covariance = svd.getCovariance(1.0);
        RealVector expectedEigenvector = new ArrayRealVector(new double[]{0.6536436227524347, 0.7343855593420352});
        RealVector eigenvector = svd.getVT().getColumnVector(0);
        RealVector expectedCovarianceEigenvector = new ArrayRealVector(new double[]{1.221651647220908, 1.3754894550953543,
                1.165258434044317});
        RealVector covarianceEigenvector = covariance.getColumnVector(0);
        Assert.assertEquals(expectedEigenvector, eigenvector);
        Assert.assertEquals(expectedCovarianceEigenvector, covarianceEigenvector);
    }

    @Test
    public void testGetCovarianceWithIdentityMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix covariance = svd.getCovariance(1.0);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        Assert.assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        Assert.assertEquals(1.4142135625, conditionNumber, 0.00001);
    }

    @Test
    public void testSolveWithIdentityMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 1.0});
        RealVector expectedSolution = new ArrayRealVector(new double[]{1.0, 1.0});
        RealVector solution = svd.getSolver().solve(b);
        Assert.assertEquals(expectedSolution, solution);
    }

    @Test
    public void testSolveWithNonSquareMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        RealVector expectedSolution = new ArrayRealVector(new double[]{1.4142135625, 1.4142135625, 1.4142135625});
        RealVector solution = svd.getSolver().solve(b);
        Assert.assertEquals(expectedSolution, solution);
    }

    @Test
    public void testSolveWithSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        RealVector expectedSolution = new ArrayRealVector(new double[]{1.0 / Math.SQRT12, 1.0 / Math.SQRT12, 0.0});
        RealVector solution = svd.getSolver().solve(b);
        Assert.assertEquals(expectedSolution, solution);
    }

}