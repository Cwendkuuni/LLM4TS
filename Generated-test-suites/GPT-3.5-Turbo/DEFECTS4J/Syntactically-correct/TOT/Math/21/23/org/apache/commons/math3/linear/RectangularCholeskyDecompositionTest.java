import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;
import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    private RectangularCholeskyDecomposition rc;

    // Test case for the constructor
    @Test
    public void testConstructor() {
        double[][] data = {{4, -2, 2}, {-2, 2, -4}, {2, -4, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = FastMath.sqrt(Math.pow(10, -12));
        
        try {
            rc = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Failed to initialize RectangularCholeskyDecomposition object");
        }
        
        assertNotNull(rc);
    }
    
    // Test case for getRootMatrix method
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, -2, 2}, {-2, 2, -4}, {2, -4, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = FastMath.sqrt(Math.pow(10, -12));
        
        try {
            rc = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Failed to initialize RectangularCholeskyDecomposition object");
        }
        
        RealMatrix root = rc.getRootMatrix();
        
        assertNotNull(root);
    }
    
    // Test case for getRank method
    @Test
    public void testGetRank() {
        double[][] data = {{4, -2, 2}, {-2, 2, -4}, {2, -4, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = FastMath.sqrt(Math.pow(10, -12));
        
        try {
            rc = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Failed to initialize RectangularCholeskyDecomposition object");
        }
        
        int rank = rc.getRank();
        
        assertEquals(3, rank);
    }
    
    // Test case for constructor with non-positive semidefinite matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveSemidefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, -2, 2}, {-2, 2, -4}, {2, -4, -11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = FastMath.sqrt(Math.pow(10, -12));
        
        rc = new RectangularCholeskyDecomposition(matrix, small);
    }
    
    // Test case for edge case of small diagonal element in constructor
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorSmallDiagonalElement() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, -2, 2}, {-2, 2, -4}, {2, -4, 1e-13}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = FastMath.sqrt(Math.pow(10, -12));
        
        rc = new RectangularCholeskyDecomposition(matrix, small);
    }

}