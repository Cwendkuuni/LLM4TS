package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                        {4.0, 5.0, 6.0},
                                                                        {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix ut = svd.getUT();
        RealMatrix u = svd.getU();
        RealMatrix vt = svd.getVT();
        RealMatrix v = svd.getV();
        RealMatrix s = svd.getS();

        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.39336963, -0.81649659, 0.43301271},
                                                                           {0.81649659, 0.39336963, -0.43301271},
                                                                           {-0.43301271, -0.43301271, 0.81649659}});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.04263534, -0.97282389, 0.24848624},
                                                                           {0.97282389, 0.04263534, -0.24848624},
                                                                           {0.24848624, -0.24848624, 0.97282389}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{3.69574714, 2.23606798, 0.0});

        assertEquals(expectedU, u);
        assertEquals(expectedV, v);
        assertEquals(expectedS, s);
        assertEquals(expectedU.transpose(), ut);
        assertEquals(expectedV.transpose(), vt);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionException() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0},
                                                                        {4.0, 5.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testSingularValueDecompositionMax() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                        {4.0, 5.0, 6.0},
                                                                        {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);

        RealMatrix ut = svd.getUT();
        RealMatrix u = svd.getU();
        RealMatrix vt = svd.getVT();
        RealMatrix v = svd.getV();
        RealMatrix s = svd.getS();

        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.36595427, -0.54772259, 0.68307395},
                                                                           {-0.54772259, 0.36595427, -0.68307395}});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.53846042, 0.81649659},
                                                                           {0.81649659, -0.53846042}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{3.73205081, 2.16445267});

        assertEquals(expectedU, u);
        assertEquals(expectedV, v);
        assertEquals(expectedS, s);
        assertEquals(expectedU.transpose(), ut);
        assertEquals(expectedV.transpose(), vt);
    }

    @Test
    public void testSolveSingularValueDecomposition() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                        {4.0, 5.0, 6.0},
                                                                        {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        double[] expectedSolution = {1.0, 1.0, 1.0};
        double[] solution = svd.getSolver().solve(new double[]{1.0, 1.0, 1.0});

        assertArrayEquals(expectedSolution, solution);

        RealMatrix expectedSolutionMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0/3.69574714, -2.13191053/3.69574714, 1.1663897/3.69574714},
                                                                            {1.0/3.69574714, 2.13191053/3.69574714, -1.1663897/3.69574714},
                                                                            {1.0/3.69574714, 0.0, 1.0/3.69574714}});
        RealMatrix solutionMatrix = svd.getSolver().solve(MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                                          {0.0, 1.0, 0.0},
                                                                                                          {0.0, 0.0, 1.0}}));

        assertEquals(expectedSolutionMatrix, solutionMatrix);

        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                    {0.0, 1.0, 0.0},
                                                                    {0.0, 0.0, 1.0}});
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.0/3.69574714, 1.0/3.69574714, 1.0/3.69574714},
                                                                               {1.0/3.69574714, 1.0/3.69574714, 1.0/3.69574714},
                                                                               {1.0/3.69574714, 1.0/3.69574714, 1.0/3.69574714}});
        RealMatrix result = svd.getSolver().solve(b);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                        {4.0, 5.0, 6.0},
                                                                        {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        int rank = svd.getRank();

        assertEquals(rank, 3);

        RealMatrix matrix2 = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0},
                                                                           {0.0, 1.0}});
        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(matrix2);

        rank = svd2.getRank();

        assertEquals(rank, 2);
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                        {4.0, 5.0, 6.0},
                                                                        {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        double norm = svd.getNorm();

        assertEquals(norm, 3.69574714);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                        {4.0, 5.0, 6.0},
                                                                        {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        double conditionNumber = svd.getConditionNumber();

        assertEquals(conditionNumber, 3.69574714 / 0.0);
    }

    @Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                        {4.0, 5.0, 6.0},
                                                                        {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix covariance = svd.getCovariance(0.0);

        RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{0.0, 0.0, 0.0},
                                                                                      {0.0, 0.0, 0.0},
                                                                                      {0.0, 0.0, 0.0}});

        assertEquals(expectedCovariance, covariance);

        RealMatrix matrix2 = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                           {4.0, 5.0, 6.0},
                                                                           {7.0, 8.0, 1.0}});
        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(matrix2);

        covariance = svd2.getCovariance(0.5);

        RealMatrix expectedCovariance2 = MatrixUtils.createRealMatrix(new double[][]{{0.09459459, 0.0, 0.0},
                                                                                      {0.0, 0.09459459, 0.0},
                                                                                      {0.0, 0.0, 0.09459459}});

        assertEquals(expectedCovariance2, covariance);
    }
}