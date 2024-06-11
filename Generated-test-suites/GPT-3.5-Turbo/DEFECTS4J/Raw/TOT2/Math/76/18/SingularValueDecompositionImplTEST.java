import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    // Test cases for constructor with full rank matrix
    @Test
    public void testConstructorFullRank() {
        // Create a full rank matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertNotNull(svd);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    // Test cases for constructor with rank-deficient matrix
    @Test
    public void testConstructorRankDeficient() {
        // Create a rank-deficient matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {1.0, 2.0}});
        
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertNotNull(svd);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    // Test cases for getU method
    @Test
    public void testGetU() {
        // Create a test matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            assertEquals(2, u.getRowDimension());
            assertEquals(2, u.getColumnDimension());
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Add more test cases for other methods...

}