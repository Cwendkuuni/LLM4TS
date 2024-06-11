package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));

        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();

        SingularValueDecomposition singValDecomp = new SingularValueDecomposition(
                MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
        RealMatrix singValU = singValDecomp.getU();
        RealMatrix singValS = singValDecomp.getS();
        RealMatrix singValV = singValDecomp.getV();

        assertArrayEquals(singValU.getData(), u.getData(), 0.001);
        assertArrayEquals(singValS.getData(), s.getData(), 0.001);
        assertArrayEquals(singValV.getData(), v.getData(), 0.001);

        double[] singularValues = svd.getSingularValues();
        double[] expectedSingularValues = {Math.sqrt(5.0), Math.sqrt(2.0)};
        assertArrayEquals(expectedSingularValues, singularValues, 0.001);
    }

    @Test
    public void testGetCovariance() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0},
                        {3.0, 4.0, 5.0}}));

        RealMatrix covariance = svd.getCovariance(2.0);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.2766, 0.2236},
                {0.2236, 0.1796}});
        assertArrayEquals(expectedCovariance.getData(), covariance.getData(), 0.001);
    }

    @Test
    public void testGetNorm() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
        double norm = svd.getNorm();
        double expectedNorm = Math.sqrt(5.0);
        assertEquals(expectedNorm, norm, 0.001);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
        double conditionNumber = svd.getConditionNumber();
        double expectedConditionNumber = Math.sqrt(5.0) / Math.sqrt(3.0);
        assertEquals(expectedConditionNumber, conditionNumber, 0.001);
    }

    @Test
    public void testGetRank() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
        int rank = svd.getRank();
        int expectedRank = 2;
        assertEquals(expectedRank, rank);
    }

    @Test
    public void testGetSolver() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
        DecompositionSolver solver = svd.getSolver();

        double[] b = {2.0, 3.0};
        double[] x = solver.solve(b);
        double[] expectedX = {1.5, 1.5};
        assertArrayEquals(expectedX, x, 0.001);

        RealVector rvB = new Array2DRowRealVector(b);
        RealVector rvX = solver.solve(rvB);
        double[] xVector = rvX.toArray();
        assertArrayEquals(expectedX, xVector, 0.001);

        RealMatrix rmB = MatrixUtils.createRealMatrix(b);
        RealMatrix rmX = solver.solve(rmB);
        double[][] xMatrix = rmX.getData();
        assertArrayEquals(expectedX, xMatrix[0], 0.001);

        assertTrue(solver.isNonSingular());
        RealMatrix inverse = solver.getInverse();
        double[][] inverseMatrix = inverse.getData();
        double epsilon = 0.001;
        for (int i = 0; i < inverseMatrix.length; i++) {
            for (int j = 0; j < inverseMatrix[i].length; j++) {
                double expectedValue = 1.0 / svd.getSingularValues()[i];
                assertEquals(expectedValue, inverseMatrix[i][j], epsilon);
            }
        }
    }

}