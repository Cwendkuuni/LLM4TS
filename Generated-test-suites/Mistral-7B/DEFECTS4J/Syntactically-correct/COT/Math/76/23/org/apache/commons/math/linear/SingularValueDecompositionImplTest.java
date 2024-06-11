import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstruction() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
            assertTrue(singularValueDecompositionImpl instanceof SingularValueDecompositionImpl);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplConstructionWithMax() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        try {
            SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix, 2);
            assertTrue(singularValueDecompositionImpl instanceof SingularValueDecompositionImpl);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImplException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 0.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.44948974278317, -0.81649658092772},
                {-0.81649658092772, 0.44948974278317}});
        RealMatrix u = singularValueDecompositionImpl.getU();
        MatrixUtils.assertEquals(expectedU, u);
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedUt = MatrixUtils.createRealMatrix(new double[][]{{0.44948974278317, -0.81649658092772},
                {-0.81649658092772, 0.44948974278317}});
        RealMatrix ut = singularValueDecompositionImpl.getUT();
        MatrixUtils.assertEquals(expectedUt, ut);
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{5.0, 7.0});
        RealMatrix s = singularValueDecompositionImpl.getS();
        MatrixUtils.assertEquals(expectedS, s);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = {5.0, 7.0};
        double[] singularValues = singularValueDecompositionImpl.getSingularValues();
        Assert.assertArrayEquals(expectedSingularValues, singularValues);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.40824829046386, 0.81649658092772},
                {0.81649658092772, -0.40824829046386}});
        RealMatrix v = singularValueDecompositionImpl.getV();
        MatrixUtils.assertEquals(expectedV, v);
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][]{{-0.40824829046386, 0.81649658092772},
                {0.81649658092772, -0.40824829046386}});
        RealMatrix vt = singularValueDecompositionImpl.getVT();
        MatrixUtils.assertEquals(expectedVt, vt);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{2.88663050919656, 0.21453054130154},
                {0.21453054130154, 2.76235539920301}});
        RealMatrix covariance = singularValueDecompositionImpl.getCovariance(0.5);
        MatrixUtils.assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        double expectedNorm = 5.0;
        double norm = singularValueDecompositionImpl.getNorm();
        assertEquals(expectedNorm, norm);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        double expectedConditionNumber = 10.0;
        double conditionNumber = singularValueDecompositionImpl.getConditionNumber();
        assertEquals(expectedConditionNumber, conditionNumber);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        int expectedRank = 2;
        int rank = singularValueDecompositionImpl.getRank();
        assertEquals(expectedRank, rank);
    }

    @Test
    public void testSolver() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix, 2);
        RealVector expectedResult = new ArrayRealVector(new double[]{1.0 / 13, 1.0 / 5, -0.125});
        RealVector result = singularValueDecompositionImpl.getSolver().solve(new ArrayRealVector(new double[]{5.0, 7.0, 9.0}));
        Assert.assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testSolverNonSingular() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl = new SingularValueDecompositionImpl(matrix);
        RealVector expectedResult = new ArrayRealVector(new double[]{1.0 / 5.0, -1.0 / 5.0});
        RealVector result = singularValueDecompositionImpl.getSolver().solve(new ArrayRealVector(new double[]{5.0, -5.0}));
        Assert.assertArrayEquals(expectedResult.toArray(), result.toArray());
    }
}