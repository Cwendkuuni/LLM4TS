package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.TridiagonalMatrix64;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    private static SingularValueDecompositionImpl singularValueDecomposition;
    private static RealMatrix inputMatrix;

    @BeforeClass
    public static void setUp() throws Exception {
        inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}});
        singularValueDecomposition = new SingularValueDecompositionImpl(inputMatrix);
    }

    @Test
    public void testGetU() throws Exception {
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{
                {0.44988415, 0.57355869, 0.70137513},
                {-0.75258136, 0.27580776, 0.43301272},
                {0.40824829, -0.81549579, 0.43301272}});
        RealMatrix u = singularValueDecomposition.getU();
        Assert.assertEquals(expectedU, u);
    }

    @Test
    public void testGetS() throws Exception {
        double[] expectedSingularValues = {1.0, 2.1630666064941216, 3.2249822255123878};
        RealMatrix s = singularValueDecomposition.getS();
        Assert.assertNotNull(s);
        Assert.assertEquals(expectedSingularValues.length, s.getRowDimension());
        Assert.assertEquals(expectedSingularValues.length, s.getColumnDimension());
        Assert.assertArrayEquals(expectedSingularValues, s.getData(), 0.0);
    }

    @Test
    public void testGetV() throws Exception {
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{
                {0.53452254, 0.51921836, 0.49386232},
                {-0.81649660, -0.46442973, 0.20183452},
                {0.13531352, 0.85945292, 0.4791787}});
        RealMatrix v = singularValueDecomposition.getV();
        Assert.assertEquals(expectedV, v);
    }

    @Test
    public void testGetVT() throws Exception {
        RealMatrix expectedVT = MatrixUtils.createRealMatrix(new double[][]{
                {0.53452254, -0.81649660, 0.13531352},
                {0.51921836, -0.46442973, 0.85945292},
                {0.49386232, 0.20183452, 0.4791787}});
        RealMatrix vt = singularValueDecomposition.getVT();
        Assert.assertEquals(expectedVT, vt);
    }

    @Test
    public void testGetSingularValues() throws Exception {
        double[] expectedSingularValues = {1.0, 2.1630666064941216, 3.2249822255123878};
        double[] singularValues = singularValueDecomposition.getSingularValues();
        Assert.assertNotNull(singularValues);
        Assert.assertArrayEquals(expectedSingularValues, singularValues, 0.0);
    }

    @Test
    public void testGetUT() throws Exception {
        RealMatrix expectedUT = MatrixUtils.createRealMatrix(new double[][]{
                {0.44988415, -0.75258136, 0.40824829},
                {0.57355869, 0.27580776, -0.81549579},
                {0.70137513, 0.43301272, 0.43301272}});
        RealMatrix ut = singularValueDecomposition.getUT();
        Assert.assertEquals(expectedUT, ut);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testInvalidInputMatrix() throws Exception {
        new SingularValueDecompositionImpl(MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {4.0, 5.0},
                {7.0, 8.0}}));
    }

    @Test
    public void testGetCovariance() throws Exception {
        double minSingularValue = 1.5;
        RealMatrix covariance = singularValueDecomposition.getCovariance(minSingularValue);
        Assert.assertEquals(covariance.getRowDimension(), covariance.getColumnDimension());
        Assert.assertEquals(3, covariance.getRowDimension());
    }

    @Test
    public void testGetNorm() throws Exception {
        Assert.assertEquals(3.2249822255123878, singularValueDecomposition.getNorm(), 0.0);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        Assert.assertEquals(1.0 / 3.2249822255123878, singularValueDecomposition.getConditionNumber(), 0.0);
    }

    @Test
    public void testGetRank() throws Exception {
        Assert.assertEquals(3, singularValueDecomposition.getRank());
    }

    @Test
    public void testGetSolver() throws Exception {
        Solver solver = singularValueDecomposition.getSolver();
        Assert.assertNotNull(solver);
        Assert.assertTrue(solver instanceof DecompositionSolver);
    }

    @Test
    public void testSolveSimple() throws Exception {
        double[] b = new double[]{1.0, 2.0, 3.0};
        RealVector x = singularValueDecomposition.getSolver().solve(b);
        RealVector expectedX = MatrixUtils.createRealVector(new double[]{2.73607354e-14, 1.23612266e-13, 6.13357354e-14});
        Assert.assertEquals(expectedX, x, 1e-14);
    }

    @Test
    public void testSolveWithIdentityMatrix() throws Exception {
        RealMatrix b = MatrixUtils.createIdentityMatrix(3);
        RealMatrix x = singularValueDecomposition.getSolver().solve(b);
        Assert.assertEquals(MatrixUtils.createIdentityMatrix(3), x);
    }

    @Test
    public void testSolveWithSingularMatrix() throws Exception {
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}});
        RealMatrix x = singularValueDecomposition.getSolver().solve(b);
        Assert.assertEquals(MatrixUtils.createRealMatrix(new double[][]{
                {1.0 / 3.2249822255123878, 0.0, 0.0},
                {0.0, 1.0 / 2.1630666064941216, 0.0},
                {0.0, 0.0, 1.0 / 1.0}}), x);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithMatrixSizeMismatch() throws Exception {
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {4.0, 5.0}});
        singularValueDecomposition.getSolver().solve(b);
    }
}