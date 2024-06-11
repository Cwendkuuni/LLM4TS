package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.BiDiagonalTransformer;
import org.apache.commons.math3.linear.RealMatrixutils;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = new SingularValueDecomposition(matrix).getMatrixU();
        RealMatrix expectedV = new SingularValueDecomposition(matrix).getMatrixV();
        RealMatrix expectedS = new SingularValueDecomposition(matrix).getSingularValues();

        assertEquals(expectedU.getRowDimension(), u.getRowDimension());
        assertEquals(expectedU.getColumnDimension(), u.getColumnDimension());
        assertEquals(expectedV.getRowDimension(), v.getRowDimension());
        assertEquals(expectedV.getColumnDimension(), v.getColumnDimension());
        assertEquals(expectedS.getRowDimension(), s.getRowDimension());
        assertEquals(expectedS.getColumnDimension(), s.getColumnDimension());

        assertArrayEquals(expectedU.getData(), u.getData());
        assertArrayEquals(expectedV.getData(), v.getData());
        assertArrayEquals(expectedS.getData(), s.getData());
    }

    @Test
    public void testGetUT() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = svd.getUT();
        RealMatrix expectedUt = svd.getU().transpose();

        assertEquals(ut.getRowDimension(), expectedUt.getRowDimension());
        assertEquals(ut.getColumnDimension(), expectedUt.getColumnDimension());
        assertArrayEquals(expectedUt.getData(), ut.getData());
    }

    @Test
    public void testGetS() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix s = svd.getS();

        assertEquals(s.getRowDimension(), s.getColumnDimension());
        assertEquals(s.getRowDimension(), matrix.getRowDimension());
        assertEquals(s.getColumnDimension(), matrix.getColumnDimension());
        assertTrue(ArrayUtils.isSameDimension(s.getData(), svd.singularValues));
        assertArrayEquals(svd.singularValues, s.getData());
    }

    @Test
    public void testGetSingularValues() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();

        assertEquals(singularValues.length, matrix.getRowDimension());
        assertEquals(singularValues.length, matrix.getColumnDimension());
        assertArrayEquals(svd.singularValues, singularValues);
    }

    @Test
    public void testGetV() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();
        RealMatrix expectedV = new SingularValueDecomposition(matrix).getMatrixV();

        assertEquals(v.getRowDimension(), expectedV.getRowDimension());
        assertEquals(v.getColumnDimension(), expectedV.getColumnDimension());
        assertArrayEquals(expectedV.getData(), v.getData());
    }

    @Test
    public void testGetVT() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix vt = svd.getVT();
        RealMatrix expectedVt = svd.getV().transpose();

        assertEquals(vt.getRowDimension(), expectedVt.getRowDimension());
        assertEquals(vt.getColumnDimension(), expectedVt.getColumnDimension());
        assertArrayEquals(expectedVt.getData(), vt.getData());
    }

    @Test
    public void testCovariance() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(5, 5);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double minSingularValue = 0.0001;
        RealMatrix covariance = svd.getCovariance(minSingularValue);

        assertEquals(covariance.getRowDimension(), matrix.getRowDimension());
        assertEquals(covariance.getColumnDimension(), covariance.getColumnDimension());
        for (int i = 0; i < covariance.getRowDimension(); i++) {
            double[] row = covariance.getRowVector(i);
            double sum = 0.0;
            for (double d : row) {
                sum += d * d;
            }
            assertEquals(minSingularValue * minSingularValue, sum, 1e-14);
        }
    }

    @Test
    public void testNorm() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();

        RealMatrix expectedU = new SingularValueDecomposition(matrix).getMatrixU();
        RealMatrix expectedS = new SingularValueDecomposition(matrix).getSingularValues();
        RealMatrix expectedV = new SingularValueDecomposition(matrix).getMatrixV();
        double product = 0.0;

        for (int i = 0; i < expectedS.getRowDimension(); i++) {
            product += Math.pow(expectedS.getData()[i][0], 2);
        }

        for (int i = 0; i < expectedU.getRowDimension(); i++) {
            double[] row = expectedU.getRowVector(i);
            for (int j = 0; j < expectedV.getColumnDimension(); j++) {
                double value = row[j] * expectedV.getData()[j][i];
                product += value * value;
            }
        }

        assertEquals(Math.sqrt(product), norm, 1e-14);
    }

    @Test
    public void testConditionNumber() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();

        RealMatrix expectedU = new SingularValueDecomposition(matrix).getMatrixU();
        RealMatrix expectedS = new SingularValueDecomposition(matrix).getSingularValues();
        RealMatrix expectedV = new SingularValueDecomposition(matrix).getMatrixV();
        double smallestSingularValue = 0.0;

        for (int i = 0; i < expectedS.getRowDimension(); i++) {
            smallestSingularValue = Math.min(smallestSingularValue, expectedS.getData()[i][0]);
        }

        assertEquals(smallestSingularValue / svd.singularValues[svd.singularValues.length - 1], conditionNumber, 1e-14);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testGetRankZero() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(new double[0][0]));
        svd.getRank();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverInvalidArguments() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] rightHandSide = new double[5];
        Solver solver = svd.getSolver();
        solver.solve(rightHandSide);
    }

    @Test
    public void testSolver() throws Exception {
        RealMatrix matrix = RealMatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] rightHandSide = new double[3];
        Solver solver = svd.getSolver();
        solver.solve(rightHandSide);

        double[] solution = svd.getSolver().solve(rightHandSide);

        double[] expectedSolution = new double[3];
        RealMatrix expectedVT = svd.getVT();
        RealMatrix expectedS = svd.getS();

        for (int i = 0; i < 3; i++) {
            expectedSolution[i] = (rightHandSide[i] / svd.singularValues[i]);
            assertEquals(expectedSolution[i], solution[i], 1e-14);
        }

        RealMatrix expectedX = new Array2DRowRealMatrix(expectedSolution, false);
        RealMatrix expectedProduct = expectedX.multiply(expectedVT);
        RealMatrix actualProduct = RealMatrixUtils.createRandomMatrix(3, 3).multiply(solution);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedProduct.getData()[i][j], actualProduct.getData()[i][j], 1e-14);
            }
        }
    }
}