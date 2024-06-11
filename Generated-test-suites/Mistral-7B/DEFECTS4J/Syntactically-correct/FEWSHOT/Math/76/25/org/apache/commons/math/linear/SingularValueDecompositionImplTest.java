package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.7417, -0.6965}, {-0.6965, 0.7417}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{2.7321, 0.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.7417, 0.6965}, {0.6965, -0.7417}});
        assertEquals(expectedU, u);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
        RealMatrix expectedUT = MatrixUtils.createRealMatrix(new double[][]{{0.7417, 0.6965}, {-0.6965, -0.7417}});
        RealMatrix expectedVT = MatrixUtils.createRealMatrix(new double[][]{{-0.7417, 0.6965}, {0.6965, -0.7417}});
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        assertEquals(expectedUT, ut);
        assertEquals(expectedVT, vt);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] actualSingularValues = svd.getSingularValues();
        double[] expectedSingularValues = new double[]{2.7321, 0.0};
        assertArrayEquals(expectedSingularValues, actualSingularValues);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double actualConditionNumber = svd.getConditionNumber();
        double expectedConditionNumber = 2.7321 / 0.0;
        assertEquals(expectedConditionNumber, actualConditionNumber, 0.01);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {0.0, 0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int actualRank = svd.getRank();
        int expectedRank = 2;
        assertEquals(expectedRank, actualRank);
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetSolverWithSingularMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getSolver();
    }

    @Test
    public void testGetSolverWithNonSingularMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        assertEquals(true, solver.isNonSingular());
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{-1.4142, -0.7143}, {1.4142, 0.7143}});
        RealMatrix result = solver.solve(MatrixUtils.createRealMatrix(new double[][]{{1.0}, {1.0}}));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix covariance = svd.getCovariance(1.0);
        double[][] expectedData = new double[][]{{-0.4534, -0.2664, -0.0409},
                {-0.2664, 0.5556, 0.1547},
                {-0.0409, 0.1547, 0.8019}};
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(expectedData, false);
        RealMatrix actualCovariance = svd.getCovariance(1.0);
        assertEquals(expectedCovariance, actualCovariance);
    }

}