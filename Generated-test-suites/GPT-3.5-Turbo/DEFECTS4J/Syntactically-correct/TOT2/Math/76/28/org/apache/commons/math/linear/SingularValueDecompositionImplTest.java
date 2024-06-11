import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.linear.*;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.util.MathUtils;

public class SingularValueDecompositionImplTest {

    private SingularValueDecompositionImpl svd;
    private RealMatrix matrix;

    @Before
    public void setUp() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        matrix = MatrixUtils.createRealMatrix(data);
        svd = new SingularValueDecompositionImpl(matrix);
    }

    // Test for typical case
    @Test
    public void testGetU() {
        RealMatrix result = svd.getU();
        // Assert the dimensions of the result matrix
        Assert.assertEquals(3, result.getRowDimension());
        Assert.assertEquals(3, result.getColumnDimension());
        // Assert the values of the result matrix
        // Needs to be manually calculated based on the input matrix
        double[][] expectedData = {{-0.21483724, -0.88723069, -0.40824829},
                                   {-0.52058739, -0.24964395, 0.81649658},
                                   {-0.82633754, 0.38794278, -0.40824829}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                Assert.assertEquals(expected.getEntry(i, j), result.getEntry(i, j), 1e-10);
            }
        }
    }

    // Test for edge case
    @Test
    public void testGetV() {
        RealMatrix result = svd.getV();
        // Assert the dimensions of the result matrix
        Assert.assertEquals(3, result.getRowDimension());
        Assert.assertEquals(3, result.getColumnDimension());
        // Assert the values of the result matrix
        // Needs to be manually calculated based on the input matrix
        double[][] expectedData = {{-0.21483724, -0.52058739, -0.82633754},
                                   {0.88723069, 0.24964395, -0.38794278},
                                   {0.40824829, -0.81649658, 0.40824829}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                Assert.assertEquals(expected.getEntry(i, j), result.getEntry(i, j), 1e-10);
            }
        }
    }

    // Test for exception handling
    @Test(expected = InvalidMatrixException.class)
    public void testInvalidMatrixException() {
        RealMatrix emptyMatrix = MatrixUtils.createRealMatrix(0, 0);
        SingularValueDecompositionImpl svdEmpty = new SingularValueDecompositionImpl(emptyMatrix);
    }

    // Add more test cases as needed

}