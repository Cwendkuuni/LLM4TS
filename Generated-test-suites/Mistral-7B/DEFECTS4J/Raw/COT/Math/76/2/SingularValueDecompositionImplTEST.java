import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    

    @Test
    public void testConstructorWithIdentityMatrix() {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2x2;
        try {
            SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
            Assert.assertEquals(2, singularValueDecomposition.getRank());
            Assert.assertEquals(2, singularValueDecomposition.getSingularValues().length);
            Assert.assertEquals(1.0, singularValueDecomposition.getSingularValues()[0], 0);
            Assert.assertEquals(1.0, singularValueDecomposition.getSingularValues()[1], 0);
            RealMatrix u = singularValueDecomposition.getU();
            Assert.assertEquals(2, u.getRowDimension());
            Assert.assertEquals(2, u.getColumnDimension());
            RealMatrix v = singularValueDecomposition.getV();
            Assert.assertEquals(2, v.getRowDimension());
            Assert.assertEquals(2, v.getColumnDimension());
            Assert.assertTrue(u.isOrthogonal());
            Assert.assertTrue(v.isOrthogonal());
            RealMatrix ut = singularValueDecomposition.getUT();
            Assert.assertEquals(2, ut.getRowDimension());
            Assert.assertEquals(2, ut.getColumnDimension());
            RealMatrix vt = singularValueDecomposition.getVT();
            Assert.assertEquals(2, vt.getRowDimension());
            Assert.assertEquals(2, vt.getColumnDimension());
            Assert.assertTrue(ut.isIdentityMatrix());
            Assert.assertTrue(vt.isIdentityMatrix());
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithRandomMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        try {
            SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
            Assert.assertEquals(3, singularValueDecomposition.getRank());
            Assert.assertEquals(3, singularValueDecomposition.getSingularValues().length);
            Assert.assertTrue(singularValueDecomposition.getSingularValues()[0] > 0);
            Assert.assertTrue(singularValueDecomposition.getSingularValues()[1] > 0);
            Assert.assertTrue(singularValueDecomposition.getSingularValues()[2] > 0);
            RealMatrix u = singularValueDecomposition.getU();
            Assert.assertEquals(3, u.getRowDimension());
            Assert.assertEquals(3, u.getColumnDimension());
            RealMatrix v = singularValueDecomposition.getV();
            Assert.assertEquals(3, v.getRowDimension());
            Assert.assertEquals(3, v.getColumnDimension());
            Assert.assertTrue(u.isOrthogonal());
            Assert.assertTrue(v.isOrthogonal());
            RealMatrix ut = singularValueDecomposition.getUT();
            Assert.assertEquals(3, ut.getRowDimension());
            Assert.assertEquals(3, ut.getColumnDimension());
            RealMatrix vt = singularValueDecomposition.getVT();
            Assert.assertEquals(3, vt.getRowDimension());
            Assert.assertEquals(3, vt.getColumnDimension());
            Assert.assertTrue(ut.isIdentityMatrix());
            Assert.assertTrue(vt.isIdentityMatrix());
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = InvalidMatrixException.class)
    public void testConstructorWithRectangularMatrix() {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2x3;
        new SingularValueDecompositionImpl(matrix);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testConstructorWithSingularMatrix() {
        RealMatrix matrix = RealMatrixFactory.SINGULAR_3x3;
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2x2;
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = singularValueDecomposition.getUT();
        Assert.assertEquals(2, ut.getRowDimension());
        Assert.assertEquals(2, ut.getColumnDimension());
        Assert.assertTrue(ut.isIdentityMatrix());
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2x2;
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix vt = singularValueDecomposition.getVT();
        Assert.assertEquals(2, vt.getRowDimension());
        Assert.assertEquals(2, vt.getColumnDimension());
        Assert.assertTrue(vt.isIdentityMatrix());
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2x2;
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix s = singularValueDecomposition.getS();
        Assert.assertEquals(2, s.getRowDimension());
        Assert.assertEquals(2, s.getColumnDimension());
        Assert.assertEquals(1.0, s.getEntry(0, 0), 0);
        Assert.assertEquals(1.0, s.getEntry(1, 1), 0);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2x2;
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = singularValueDecomposition.getSingularValues();
        Assert.assertEquals(2, singularValues.length);
        Assert.assertEquals(1.0, singularValues[0], 0);
        Assert.assertEquals(1.0, singularValues[1], 0);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix covariance = singularValueDecomposition.getCovariance(0.1);
        Assert.assertEquals(2, covariance.getRowDimension());
        Assert.assertEquals(2, covariance.getColumnDimension());

        double[][] covarianceData = covariance.getData();
        for (int i = 0; i < covarianceData.length; ++i) {
            for (int j = 0; j < covarianceData[i].length; ++j) {
                Assert.assertTrue(covarianceData[i][j] > 0);
            }
        }
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2x2;
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        double norm = singularValueDecomposition.getNorm();
        Assert.assertEquals(1.0, norm, 0);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix, 2);
        double conditionNumber = singularValueDecomposition.getConditionNumber();
        Assert.assertTrue(conditionNumber > 1);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2x2;
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        int rank = singularValueDecomposition.getRank();
        Assert.assertEquals(2, rank);
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2x2;
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = singularValueDecomposition.getSolver();
        Assert.assertNotNull(solver);
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        });
        RealVector x = solver.solve(b);
        Assert.assertEquals(2, x.getDimension());
        Assert.assertEquals(1.0, x.getEntry(0), 0);
        Assert.assertEquals(2.0, x.getEntry(1), 0);

        RealMatrix x2 = solver.solve(b.transpose());
        Assert.assertEquals(2, x2.getRowDimension());
        Assert.assertEquals(2, x2.getColumnDimension());
        Assert.assertTrue(x2.isIdentityMatrix());

        RealMatrix b2 = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0}
        });
        RealMatrix x3 = solver.solve(b2);
        Assert.assertEquals(2, x3.getRowDimension());
        Assert.assertEquals(3, x3.getColumnDimension());
        Assert.assertFalse(x3.isIdentityMatrix());

        RealVector x4 = solver.solve(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0}
        }));
        Assert.assertEquals(2, x4.getDimension());
        Assert.assertEquals(0.0, x4.getEntry(0), 0);
        Assert.assertEquals(0.0, x4.getEntry(1), 0);
    }

    
}