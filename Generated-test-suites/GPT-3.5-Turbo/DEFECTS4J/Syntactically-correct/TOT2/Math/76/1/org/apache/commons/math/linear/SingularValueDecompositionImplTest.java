import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testGetU() {
        // Test case for obtaining U matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        try {
            RealMatrix result = svd.getU();
            assertEquals(2, result.getRowDimension());
            assertEquals(2, result.getColumnDimension());
        } catch (InvalidMatrixException e) {
            fail("InvalidMatrixException thrown");
        }
    }

    @Test
    public void testGetSingularValues() {
        // Test case for obtaining singular values
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        try {
            double[] result = svd.getSingularValues();
            assertEquals(2, result.length);
        } catch (InvalidMatrixException e) {
            fail("InvalidMatrixException thrown");
        }
    }

    @Test
    public void testGetRank() {
        // Test case for obtaining rank
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}, {5, 6}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        try {
            int result = svd.getRank();
            assertEquals(2, result);
        } catch (IllegalStateException e) {
            fail("IllegalStateException thrown");
        }
    }

    // Add more test cases for other methods if necessary

}