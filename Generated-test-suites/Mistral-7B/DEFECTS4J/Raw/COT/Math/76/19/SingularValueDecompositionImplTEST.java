import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructor() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testSingularValueDecompositionImplMaxSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 1);
        Assert.assertEquals(1, svd.getSingularValues().length);
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        Assert.assertEquals(2, u.getRowDimension());
        Assert.assertEquals(2, u.getColumnDimension());
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = svd.getUT();
        Assert.assertEquals(2, ut.getRowDimension());
        Assert.assertEquals(2, ut.getColumnDimension());
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix s = svd.getS();
        Assert.assertEquals(2, s.getRowDimension());
        Assert.assertEquals(2, s.getColumnDimension());
        double[] singularValues = svd.getSingularValues();
        Assert.assertEquals(Math.sqrt(5.0), singularValues[0], 0.01);
        Assert.assertEquals(Math.sqrt(13.0), singularValues[1], 0.01);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();
        Assert.assertEquals(Math.sqrt(5.0), singularValues[0], 0.01);
        Assert.assertEquals(Math.sqrt(13.0), singularValues[1], 0.01);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();
        Assert.assertEquals(2, v.getRowDimension());
        Assert.assertEquals(2, v.getColumnDimension());
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix vt = svd.getVT();
        Assert.assertEquals(2, vt.getRowDimension());
        Assert.assertEquals(2, vt.getColumnDimension());
    }

    @Test
    public void testGetCovarianceWithPositiveSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.0);
        Assert.assertEquals(2, covariance.getRowDimension());
        Assert.assertEquals(2, covariance.getColumnDimension());
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetCovarianceWithNegativeSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {-2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getCovariance(0.0);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        Assert.assertEquals(Math.sqrt(5.0), norm, 0.01);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        Assert.assertEquals(Math.sqrt(13.0) / Math.sqrt(5.0), conditionNumber, 0.01);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        Assert.assertEquals(2, rank);
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealMatrix a = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0});
        RealVector x = solver.solve(b);
        Assert.assertEquals(1.0, x.getEntry(0), 0.01);
        Assert.assertEquals(2.0, x.getEntry(1), 0.01);
    }
}