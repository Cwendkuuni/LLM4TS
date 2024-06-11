import static org.junit.Assert.*;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testConstructor() {
        // Test constructor with valid matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertNotNull(svd);
        
        // Test constructor with invalid matrix (not square)
        RealMatrix invalidMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        try {
            SingularValueDecompositionImpl invalidSvd = new SingularValueDecompositionImpl(invalidMatrix);
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetU() {
        // Test getting U matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        assertNotNull(u);
    }

    @Test
    public void testGetV() {
        // Test getting V matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();
        assertNotNull(v);
    }

    @Test
    public void testSolve() {
        // Test solving linear equations
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] b = {1, 2};
        double[] solution = svd.getSolver().solve(b);
        assertNotNull(solution);
    }

    // Add more test cases for other methods as needed

}