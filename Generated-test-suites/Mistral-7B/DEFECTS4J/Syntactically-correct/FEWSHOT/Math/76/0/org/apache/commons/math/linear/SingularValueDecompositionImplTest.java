package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{-0.42261907, 0.81649662, -0.42261907}, {0.81649662, -0.42261907, 0.81649662}, {-0.42261907, -0.81649662, -0.42261907}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{2.44948974, 3.53553391, 0.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, -0.80653282, 0.25881905}, {-0.80653282, 0.53846931, 0.25881905}, {-0.25881905, 0.25881905, -0.81649662}});
        RealMatrix expectedUSV = u.multiply(s).multiply(v);
        RealMatrix resultUSV = u.multiply(s).multiply(v);
        assertEquals(expectedUSV.toString(), resultUSV.toString());

        double[] singularValues = svd.getSingularValues();
        assertArrayEquals(new double[]{2.44948974, 3.53553391, 0.0}, singularValues, 0.001);
    }

    @Test
    public void testSingularValueDecompositionImplWithMaxSingularValues()
        throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{-0.55555555, -0.82455011}, {0.82455011, -0.55555555}, {-0.55555555, 0.82455011}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{5.51673821, 1.41421367});
        RealMatrix expectedVT = MatrixUtils.createRealMatrix(new double[][]{{-0.4472136, 0.8944272}, {0.8944272, -0.4472136}});
        RealMatrix expectedUT = u.transpose();
        RealMatrix expectedUSV = expectedUT.multiply(expectedS).multiply(expectedVT);
        RealMatrix resultUSV = u.multiply(s).multiply(v.transpose());
        assertEquals(expectedUSV.toString(), resultUSV.toString());

        double[] singularValues = svd.getSingularValues();
        assertArrayEquals(new double[]{5.51673821, 1.41421367}, singularValues, 0.001);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImplWithInvalidMatrix()
        throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.5);
        RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{0.01341641, 0.01098611}, {0.01098611, 0.01341641}});
        assertEquals(expectedCovariance.toString(), covariance.toString());
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        double expectedNorm = 3.74165679;
        assertEquals(expectedNorm, norm, 0.001);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        double expectedConditionNumber = 11.1259221;
        assertEquals(expectedConditionNumber, conditionNumber, 0.001);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        int expectedRank = 3;
        assertEquals(expectedRank, rank);
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        double[] b = new double[]{1.0, 2.0, 3.0};
        double[] result = solver.solve(b);
        double[] expectedResult = new double[]{1.110223027, -0.325107854, 0.534522541};
        assertArrayEquals(expectedResult, result, 0.001);

        RealVector rv = RealVectorUtils.createRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector resultRV = solver.solve(rv);
        RealVector expectedResultRV = RealVectorUtils.createRealVector(new double[]{1.110223027, -0.325107854, 0.534522541});
        assertEquals(expectedResultRV.toString(), resultRV.toString());

        RealMatrix rm = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        RealMatrix resultRM = solver.solve(rm);
        RealMatrix expectedResultRM = MatrixUtils.createRealMatrix(new double[][]{{1.000000011, 0.0, 0.0}, {0.0, 1.000000011, 0.0}, {0.0, 0.0, 1.000000011}});
        assertEquals(expectedResultRM.toString(), resultRM.toString());
    }
}