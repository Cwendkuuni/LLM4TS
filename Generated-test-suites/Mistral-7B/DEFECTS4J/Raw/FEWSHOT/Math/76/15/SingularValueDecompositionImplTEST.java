package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.544965579592253, 0.74532638129434}, {-0.812235451741473, 0.44725359172409}, {-0.435496214542192, -0.435496214542192}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{15.5728755441358}, {0.0}, {0.0}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.446428440708907, 0.616523952011644, 0.765648525119285}, {-0.765648525119285, 0.446428440708907, 0.408285273625976}, {-0.616523952011644, -0.408285273625976, 0.446428440708907}});
        assertEquals(expectedU, u);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
    }

    @Test
    public void testSingularValueDecompositionImplWithMaxSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.544965579592253, 0.74532638129434}, {-0.812235451741473, 0.44725359172409}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{15.5728755441358, 11.7605066329489}, {0.0, 0.0}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.446428440708907, 0.616523952011644}, {-0.765648525119285, 0.446428440708907}});
        assertEquals(expectedU, u);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
    }

    @Test
    public void testSingularValueDecompositionImplWithZeroMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0}, {0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.707106781186548}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{0.707106781186548}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.707106781186548}, {-0.707106781186548}});
        assertEquals(expectedU, u);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(1.0);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{5.29844745187935, 4.4816568884253}, {4.4816568884253, 5.29844745187935}});
        assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        double expectedNorm = 15.5728755441358;
        assertEquals(expectedNorm, norm, 0.001);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        double expectedConditionNumber = 15.5728755441358 / 0.001;
        assertEquals(expectedConditionNumber, conditionNumber, 0.001);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        int expectedRank = 3;
        assertEquals(expectedRank, rank);
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetRankZeroMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0}, {0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverInvalidMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        double[] result = solver.solve(new double[]{1.0, 2.0, 3.0});
    }

    @Test
    public void testSolver() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        double[] result = solver.solve(new double[]{10.0, 11.0, 12.0});
        double[] expectedResult = new double[]{1.66532807481798, -2.21184768365766, 1.45596235625682};
        assertArrayEquals(expectedResult, result, 0.001);
    }

}