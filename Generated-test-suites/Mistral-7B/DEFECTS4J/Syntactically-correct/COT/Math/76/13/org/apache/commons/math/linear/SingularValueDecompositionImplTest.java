import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.Array2DRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructionWithIdentityMatrix() {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2X2;
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix v = svd.getV();
            RealMatrix s = svd.getS();
            assertEquals(u.getRowDimension(), u.getColumnDimension());
            assertEquals(v.getRowDimension(), v.getColumnDimension());
            assertEquals(s.getRowDimension(), s.getColumnDimension());
            assertEquals(matrix.getRowDimension(), u.getRowDimension());
            assertEquals(matrix.getColumnDimension(), u.getColumnDimension());
            assertEquals(matrix, u.multiply(s).multiply(v.transpose()));
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplConstructionWithZeroMatrix() {
        RealMatrix matrix = RealMatrixFactory.ZERO_2X2;
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix v = svd.getV();
            RealMatrix s = svd.getS();
            assertEquals(u.getRowDimension(), u.getColumnDimension());
            assertEquals(v.getRowDimension(), v.getColumnDimension());
            assertEquals(s.getRowDimension(), s.getColumnDimension());
            assertEquals(matrix.getRowDimension(), u.getRowDimension());
            assertEquals(matrix.getColumnDimension(), u.getColumnDimension());
            assertTrue(s.isIdentityMatrix());
            assertEquals(RealMatrixFactory.IDENTITY_2X2, u.multiply(s).multiply(v.transpose()));
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplConstructionWithRandomMatrix() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix v = svd.getV();
            RealMatrix s = svd.getS();
            assertEquals(u.getRowDimension(), u.getColumnDimension());
            assertEquals(v.getRowDimension(), v.getColumnDimension());
            assertEquals(s.getRowDimension(), s.getColumnDimension());
            assertEquals(matrix.getRowDimension(), u.getRowDimension());
            assertEquals(matrix.getColumnDimension(), u.getColumnDimension());
            RealMatrix result = u.multiply(s).multiply(v.transpose());
            RealMatrix expected = new Array2DRealMatrix(new double[][]{
                    {1.11, 0.38},
                    {-0.42, 1.93}
            });
            assertEquals(result, expected);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionImplConstructionWithInvalidMatrix() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46},
                {-1.23, 5.46}
        });
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testSingularValueDecompositionImplGetRank() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertEquals(svd.getRank(), 2);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetUT() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix ut = svd.getUT();
            assertEquals(ut.getRowDimension(), matrix.getColumnDimension());
            assertEquals(ut.getColumnDimension(), matrix.getRowDimension());
            RealMatrix result = matrix.transpose();
            assertEquals(result, ut);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetS() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix s = svd.getS();
            assertEquals(s.getRowDimension(), matrix.getRowDimension());
            assertEquals(s.getColumnDimension(), matrix.getColumnDimension());
            RealVector singularValues = s.getColumnVector(0);
            assertEquals(singularValues.getDimension(), matrix.getRowDimension());
            assertEquals(singularValues.getEntry(0), 2.12, 0.01);
            assertEquals(singularValues.getEntry(1), 5.47, 0.01);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetSingularValues() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double[] singularValues = svd.getSingularValues();
            assertEquals(singularValues.length, 2);
            assertEquals(singularValues[0], 2.12, 0.01);
            assertEquals(singularValues[1], 5.47, 0.01);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetV() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix v = svd.getV();
            assertEquals(v.getRowDimension(), matrix.getColumnDimension());
            assertEquals(v.getColumnDimension(), matrix.getRowDimension());
            RealMatrix result = new Array2DRealMatrix(new double[][]{
                    {-0.42, 0.89},
                    {-0.89, -0.42}
            });
            assertEquals(result, v);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetVT() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix vt = svd.getVT();
            assertEquals(vt.getRowDimension(), matrix.getColumnDimension());
            assertEquals(vt.getColumnDimension(), matrix.getRowDimension());
            RealMatrix result = new Array2DRealMatrix(new double[][]{
                    {-0.89, -0.42},
                    {0.89, 0.42}
            });
            assertEquals(result, vt);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetCovariance() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double minSingularValue = 1.0;
            RealMatrix covariance = svd.getCovariance(minSingularValue);
            assertEquals(covariance.getRowDimension(), 2);
            assertEquals(covariance.getColumnDimension(), 2);
            assertEquals(covariance.getEntry(0, 0), 0.1296, 0.01);
            assertEquals(covariance.getEntry(0, 1), 0.0, 0.01);
            assertEquals(covariance.getEntry(1, 0), 0.0, 0.01);
            assertEquals(covariance.getEntry(1, 1), 10.288, 0.01);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetNorm() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double norm = svd.getNorm();
            assertEquals(norm, 2.12, 0.01);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetConditionNumber() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double conditionNumber = svd.getConditionNumber();
            assertEquals(conditionNumber, 25.092, 0.01);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSingularValueDecompositionImplSolver() {
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
                {2.12, 0.35},
                {-1.23, 5.46}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealVector b = new ArrayRealVector(new double[]{-1.0, 1.0});
            Solver solver = svd.getSolver();
            RealVector x = solver.solve(b);
            RealVector expected = new ArrayRealVector(new double[]{1.0 / 3.15, -1.0 / 3.15});
            assertEquals(x, expected);
        } catch (InvalidMatrixException e) {
            // Should not come here
            throw new RuntimeException("Unexpected exception", e);
        }
    }

}