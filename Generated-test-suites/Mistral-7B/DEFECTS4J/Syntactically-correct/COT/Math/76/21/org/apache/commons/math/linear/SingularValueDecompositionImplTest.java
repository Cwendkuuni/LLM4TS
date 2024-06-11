import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            Assert.assertEquals(2, singularValueDecomposition.getRank());
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.7417, -0.6889},
                {0.6889, 0.7417}});
        RealMatrix u = singularValueDecomposition.getU();
        Assert.assertEquals(expectedU, u);
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedUt = MatrixUtils.createRealMatrix(new double[][]{{0.7417, 0.6889},
                {-0.6889, 0.7417}});
        RealMatrix ut = singularValueDecomposition.getUT();
        Assert.assertEquals(expectedUt, ut);
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{2.2361, 0.8084});
        RealMatrix s = singularValueDecomposition.getS();
        Assert.assertEquals(expectedS, s);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = new double[]{2.2361, 0.8084};
        double[] singularValues = singularValueDecomposition.getSingularValues();
        Assert.assertArrayEquals(expectedSingularValues, singularValues);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.7417, -0.6889},
                {0.6889, 0.7417}});
        RealMatrix v = singularValueDecomposition.getV();
        Assert.assertEquals(expectedV, v);
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][]{{-0.7417, 0.6889},
                {-0.6889, -0.7417}});
        RealMatrix vt = singularValueDecomposition.getVT();
        Assert.assertEquals(expectedVt, vt);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{2.9153, 0.1876},
                {0.1876, 2.2282}});
        RealMatrix covariance = singularValueDecomposition.getCovariance(1.0);
        Assert.assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        double expectedNorm = 3.7417;
        double norm = singularValueDecomposition.getNorm();
        Assert.assertEquals(expectedNorm, norm, 1e-8);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        double expectedConditionNumber = 7.4825;
        double conditionNumber = singularValueDecomposition.getConditionNumber();
        Assert.assertEquals(expectedConditionNumber, conditionNumber, 1e-8);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 0.0}, {2.0, 3.0, 0.0},
                {0.0, 0.0, 1.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        int expectedRank = 3;
        int rank = singularValueDecomposition.getRank();
        Assert.assertEquals(expectedRank, rank);
    }

    @Test(expected = MathRuntimeException.class)
    public void testNonSquareMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0}});
        new SingularValueDecompositionImpl(matrix);
    }
}