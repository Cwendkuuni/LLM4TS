import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl_Constructor() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testSingularValueDecompositionImpl_Constructor_MaxSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImpl_Constructor_InvalidMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 3.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();

        Assert.assertNotNull(u);
        Assert.assertEquals(2, u.getRowDimension());
        Assert.assertEquals(2, u.getColumnDimension());
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = svd.getUT();

        Assert.assertNotNull(ut);
        Assert.assertEquals(2, ut.getRowDimension());
        Assert.assertEquals(2, ut.getColumnDimension());
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix s = svd.getS();

        Assert.assertNotNull(s);
        Assert.assertEquals(2, s.getRowDimension());
        Assert.assertEquals(2, s.getColumnDimension());
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();

        Assert.assertNotNull(singularValues);
        Assert.assertEquals(2, singularValues.length);
        Assert.assertEquals(Math.sqrt(5.0), singularValues[0], 0.001);
        Assert.assertEquals(Math.sqrt(3.0), singularValues[1], 0.001);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();

        Assert.assertNotNull(v);
        Assert.assertEquals(2, v.getRowDimension());
        Assert.assertEquals(2, v.getColumnDimension());
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix vt = svd.getVT();

        Assert.assertNotNull(vt);
        Assert.assertEquals(2, vt.getRowDimension());
        Assert.assertEquals(2, vt.getColumnDimension());
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.5);

        Assert.assertNotNull(covariance);
        Assert.assertEquals(2, covariance.getRowDimension());
        Assert.assertEquals(2, covariance.getColumnDimension());
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();

        Assert.assertEquals(Math.sqrt(5.0), norm, 0.001);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();

        Assert.assertEquals(Math.sqrt(5.0) / Math.sqrt(3.0), conditionNumber, 0.001);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();

        Assert.assertEquals(2, rank);
    }

    @Test
    public void testSolver() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        double[] b = new double[]{2.0, 3.0};
        double[] x = solver.solve(b);

        Assert.assertEquals(2, x.length);
        Assert.assertEquals(1.5, x[0], 0.001);
        Assert.assertEquals(2.5, x[1], 0.001);
    }

    @Test
    public void testSolver_NonSingular() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        double[] b = new double[]{1.0, 1.0};
        double[] x = solver.solve(b);

        Assert.assertEquals(2, x.length);
        Assert.assertEquals(1.0, x[0], 0.001);
        Assert.assertEquals(1.0, x[1], 0.001);
    }
}