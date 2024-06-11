import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        Assert.assertEquals(2, svd.getU().getRowDimension());
        Assert.assertEquals(2, svd.getU().getColumnDimension());
        Assert.assertEquals(2, svd.getS().getRowDimension());
        Assert.assertEquals(2, svd.getS().getColumnDimension());
        Assert.assertEquals(2, svd.getV().getRowDimension());
        Assert.assertEquals(2, svd.getV().getColumnDimension());
    }

    @Test
    public void testSingularValueDecompositionImplGetU() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{-0.44948974278317, 0.89442718055555},
                {0.89442718055555, -0.44948974278317}});
        Assert.assertArrayEquals(expectedU.getData(), u.getData(), 0.0001);
    }

    @Test
    public void testSingularValueDecompositionImplGetUT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = svd.getUT();
        RealMatrix expectedUt = MatrixUtils.createRealMatrix(new double[][]{{-0.44948974278317, -0.89442718055555},
                {0.89442718055555, 0.44948974278317}});
        Assert.assertArrayEquals(expectedUt.getData(), ut.getData(), 0.0001);
    }

    @Test
    public void testSingularValueDecompositionImplGetS() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix s = svd.getS();
        double[] expectedSingularValues = {3.741657386773941, 0.0};
        Assert.assertArrayEquals(expectedSingularValues, s.getData(), 0.0001);
    }

    @Test
    public void testSingularValueDecompositionImplGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = {3.741657386773941, 0.0};
        Assert.assertArrayEquals(expectedSingularValues, svd.getSingularValues(), 0.0001);
    }

    @Test
    public void testSingularValueDecompositionImplGetV() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.44948974278317, -0.81649658092773},
                {0.81649658092773, 0.44948974278317}});
        Assert.assertArrayEquals(expectedV.getData(), v.getData(), 0.0001);
    }

    @Test
    public void testSingularValueDecompositionImplGetVT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix vt = svd.getVT();
        RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][]{{-0.44948974278317, 0.81649658092773},
                {-0.81649658092773, -0.44948974278317}});
        Assert.assertArrayEquals(expectedVt.getData(), vt.getData(), 0.0001);
    }

    @Test
    public void testSingularValueDecompositionImplGetCovariance() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.5);
        RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{0.41614683654714, 0.41614683654714},
                {0.41614683654714, 0.41614683654714}});
        Assert.assertArrayEquals(expectedCovariance.getData(), covariance.getData(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionImplGetCovarianceWithNegativeCutoff() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getCovariance(-1.0);
    }

    @Test
    public void testSingularValueDecompositionImplGetRank() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        Assert.assertEquals(2, rank);
    }

    @Test
    public void testSingularValueDecompositionImplGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        Assert.assertEquals(3.741657386773941, norm, 0.0001);
    }

    @Test
    public void testSingularValueDecompositionImplGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        Assert.assertEquals(3.741657386773941 / 0.0, conditionNumber, 0.0001);
    }
}