import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
            return;
        }
        // Check the instance variables
        Assert.assertEquals(2, svd.getM());
        Assert.assertEquals(2, svd.getN());
        Assert.assertNotNull(svd.getTransformer());
        Assert.assertNotNull(svd.getMainBidiagonal());
        Assert.assertNotNull(svd.getSecondaryBidiagonal());
        Assert.assertNotNull(svd.getMainTridiagonal());
        Assert.assertNotNull(svd.getSecondaryTridiagonal());
        Assert.assertNotNull(svd.getEigenDecomposition());
        Assert.assertNotNull(svd.getSingularValues());
        Assert.assertNotNull(svd.getCachedU());
        Assert.assertNotNull(svd.getCachedUt());
        Assert.assertNotNull(svd.getCachedS());
        Assert.assertNotNull(svd.getCachedV());
        Assert.assertNotNull(svd.getCachedVt());
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.54405876, -0.81518575},
                {-0.81518575, 0.54405876}});
        RealMatrix u = svd.getU();
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.54405876, -0.81518575},
                {-0.81518575, 0.54405876}});
        RealMatrix ut = svd.getUT();
        Assert.assertEquals(expectedU, u);
        Assert.assertEquals(expectedUt, ut);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = {4.898981695, 0.0};
        double[] singularValues = svd.getSingularValues();
        Assert.assertArrayEquals(expectedSingularValues, singularValues);
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{4.898981695}});
        RealMatrix s = svd.getS();
        Assert.assertEquals(expectedS, s);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.81518575, 0.54405876},
                {0.54405876, -0.81518575}});
        RealMatrix v = svd.getV();
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{-0.81518575, 0.54405876},
                {0.54405876, -0.81518575}});
        RealMatrix vt = svd.getVT();
        Assert.assertEquals(expectedV, v);
        Assert.assertEquals(expectedVt, vt);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.563509645, 0.51443572},
                {0.51443572, 0.563509645}});
        RealMatrix covariance = svd.getCovariance(0.0);
        Assert.assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double expectedNorm = 4.898981695;
        double norm = svd.getNorm();
        Assert.assertEquals(expectedNorm, norm, 0.0001);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double expectedConditionNumber = 9.89896339;
        double conditionNumber = svd.getConditionNumber();
        Assert.assertEquals(expectedConditionNumber, conditionNumber, 0.0001);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {3.0, 4.0, 5.0},
                {6.0, 7.0, 8.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
        int expectedRank = 3;
        int rank = svd.getRank();
        Assert.assertEquals(expectedRank, rank);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSingularValuesWithInvalidSize() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0},
                {7.0, 8.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSingularValuesWithNegativeSize() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0},
                {7.0, 8.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, -1);
    }
}