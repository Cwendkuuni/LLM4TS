package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();

        RealMatrix originalMatrix = u.multiply(s).multiply(v.transpose());
        RealMatrix difference = originalMatrix.subtract(matrix);
        assertEquals(MatrixUtils.createIdentityMatrix(matrix.getRowDimension()), difference);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testInvalidMatrixException() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                      {4.0, 5.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        double[] singularValues = svd.getSingularValues();
        assertEquals(3.0, singularValues[0], 0.0);
        assertEquals(6.830508529233558, singularValues[1], 0.001);
        assertEquals(0.0, singularValues[2], 0.0);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix covariance = svd.getCovariance(5.0);

        assertEquals(3, covariance.getRowDimension());
        assertEquals(3, covariance.getColumnDimension());
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        double norm = svd.getNorm();

        assertEquals(Math.sqrt(35.0), norm, 0.01);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        double conditionNumber = svd.getConditionNumber();

        assertEquals(Math.sqrt(35.0) / Math.sqrt(3.0), conditionNumber, 0.01);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        int rank = svd.getRank();

        assertEquals(3, rank);
    }

    @Test
    public void testSolver() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = svd.getSolver().solve(b);

        RealVector expected = new ArrayRealVector(new double[]{1.171306682619538,
                                                              2.198146874949464,
                                                              3.0});

        assertEquals(expected, x);
    }
}