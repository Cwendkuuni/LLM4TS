import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.57355968, 0.81649664}, {-0.81649664, 0.57355968}});
            assertEquals(expected, u);
            assertEquals(new Array2DRowRealMatrix(new double[][]{{2.44948974, 0}, {0, 0.44948974}}), s);
            RealMatrix vt = svd.getVT();
            RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{-0.57355968, -0.81649664}, {-0.81649664, 0.57355968}});
            assertEquals(expectedVt, vt);
            double[] singularValues = svd.getSingularValues();
            assertArrayEquals(new double[]{2.44948974, 0.44948974}, singularValues, 0.0);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionWithIdentityMatrix() {
        RealMatrix matrix = new IdentityRealMatrix(2);
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expected = new IdentityRealMatrix(2);
            assertEquals(expected, u);
            assertEquals(new Array2DRowRealMatrix(new double[][]{{2, 0}, {0, 2}}), s);
            RealMatrix vt = svd.getVT();
            assertEquals(expected.transpose(), vt);
            double[] singularValues = svd.getSingularValues();
            assertArrayEquals(new double[]{2, 2}, singularValues, 0.0);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionWithZeroMatrix() {
        RealMatrix matrix = new ZeroRealMatrix(2, 2);
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            fail("Expected InvalidMatrixException, but it was not thrown.");
        } catch (InvalidMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSingularValueDecompositionWithNaN() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, Double.NaN}, {Double.NaN, 1}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            fail("Expected InvalidMatrixException, but it was not thrown.");
        } catch (InvalidMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSingularValueDecompositionWithLargeMatrix() {
        RealMatrix matrix = MatrixUtils.createRandomRealMatrix(1000, 1000);
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            // Test some values
            double[] singularValues = svd.getSingularValues();
            assertEquals(matrix.getRowDimension(), singularValues.length);
            for (int i = 0; i < singularValues.length; ++i) {
                assertTrue(singularValues[i] > 0);
            }
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}