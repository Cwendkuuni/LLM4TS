import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl_identityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        SingularValueDecomposition singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = MatrixUtils.createIdentityMatrix(3);
        RealMatrix expectedS = MatrixUtils.createIdentityMatrix(3);
        RealMatrix expectedV = MatrixUtils.createIdentityMatrix(3);
        Assert.assertEquals(expectedU, singularValueDecomposition.getU());
        Assert.assertEquals(expectedS, singularValueDecomposition.getS());
        Assert.assertEquals(expectedV, singularValueDecomposition.getV());
    }

    @Test
    public void testSingularValueDecompositionImpl_zeroMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createZeroMatrix(2, 2);
        SingularValueDecomposition singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = MatrixUtils.createIdentityMatrix(2);
        RealMatrix expectedS = MatrixUtils.createZeroMatrix(2, 2);
        RealMatrix expectedV = MatrixUtils.createIdentityMatrix(2);
        Assert.assertEquals(expectedU, singularValueDecomposition.getU());
        Assert.assertEquals(expectedS, singularValueDecomposition.getS());
        Assert.assertEquals(expectedV, singularValueDecomposition.getV());
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImpl_invalidMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRandomMatrix(3, 3);
        SingularValueDecomposition singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = new Array2DRowRealMatrix(3, 3); // this should not be initialized
        RealMatrix expectedS = new Array2DRowRealMatrix(3, 3); // this should not be initialized
        RealMatrix expectedV = new Array2DRowRealMatrix(3, 3); // this should not be initialized
        Assert.assertEquals(expectedU, singularValueDecomposition.getU()); // this should fail
        Assert.assertEquals(expectedS, singularValueDecomposition.getS()); // this should fail
        Assert.assertEquals(expectedV, singularValueDecomposition.getV()); // this should fail
    }

    @Test
    public void testSingularValueDecompositionImpl_singularValueDecomposition() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {1, 2}});
        SingularValueDecomposition singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.7411, 0.6708},
                {-0.6708, 0.7411}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{2.4495}, {2.4495}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.7071, 0.6928},
                {-0.6928, 0.7071}});
        Assert.assertEquals(expectedU, singularValueDecomposition.getU());
        Assert.assertEquals(expectedS, singularValueDecomposition.getS());
        Assert.assertEquals(expectedV, singularValueDecomposition.getV());
    }

    @Test
    public void testSingularValueDecompositionImpl_getSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {1, 2}});
        SingularValueDecomposition singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = {2.4495, 2.4495};
        Assert.assertArrayEquals(expectedSingularValues,
                singularValueDecomposition.getSingularValues(), 0.0);
    }

    @Test
    public void testSingularValueDecompositionImpl_getRank() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {1, 2}});
        SingularValueDecomposition singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        int expectedRank = 2;
        Assert.assertEquals(expectedRank, singularValueDecomposition.getRank());
    }

    @Test
    public void testSingularValueDecompositionImpl_getCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {1, 2}});
        SingularValueDecomposition singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        double minSingularValue = 1.5;
        RealMatrix covarianceMatrix = singularValueDecomposition.getCovariance(minSingularValue);
        double[][] covarianceData = covarianceMatrix.getData();
        Assert.assertEquals(2, covarianceData[0].length);
        Assert.assertEquals(2, covarianceData[1].length);
        Assert.assertEquals(0.0661, covarianceData[0][0], 0.001);
        Assert.assertEquals(0.0661, covarianceData[0][1], 0.001);
        Assert.assertEquals(0.0661, covarianceData[1][0], 0.001);
        Assert.assertEquals(0.0661, covarianceData[1][1], 0.001);
    }

    @Test
    public void testSingularValueDecompositionImpl_getNorm() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {1, 2}});
        SingularValueDecomposition singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        double expectedNorm = 2.4495;
        Assert.assertEquals(expectedNorm, singularValueDecomposition.getNorm(), 0.0);
    }

    @Test
    public void testSingularValueDecompositionImpl_getConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {1, 2}});
        SingularValueDecomposition singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        double expectedConditionNumber = 2.4495 / 0.00024495;
        Assert.assertEquals(expectedConditionNumber,
                singularValueDecomposition.getConditionNumber(), 0.0);
    }
}