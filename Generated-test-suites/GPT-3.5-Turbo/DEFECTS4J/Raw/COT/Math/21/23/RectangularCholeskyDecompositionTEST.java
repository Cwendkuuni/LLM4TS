
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    private RealMatrix positiveSemiDefiniteMatrix;
    private RectangularCholeskyDecomposition decomposition;
    
    @Before
    public void setUp() {
        positiveSemiDefiniteMatrix = MatrixUtils.createRealMatrix(new double[][] {{1, 0}, {0, 1}});
        decomposition = new RectangularCholeskyDecomposition(positiveSemiDefiniteMatrix, 1e-9);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        // Compare dimensions
        assertEquals(positiveSemiDefiniteMatrix.getRowDimension(), rootMatrix.getRowDimension());
        assertEquals(decomposition.getRank(), rootMatrix.getColumnDimension());
        
        // Calculate B.B^T
        RealMatrix calculatedCovariance = rootMatrix.multiply(rootMatrix.transpose());
        
        // Compare matrices
        for (int i = 0; i < positiveSemiDefiniteMatrix.getRowDimension(); i++) {
            for (int j = 0; j < positiveSemiDefiniteMatrix.getRowDimension(); j++) {
                assertEquals(positiveSemiDefiniteMatrix.getEntry(i, j), calculatedCovariance.getEntry(i, j), 1e-9);
            }
        }
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix nonPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(new double[][] {{-1, 0}, {0, 1}});
        decomposition = new RectangularCholeskyDecomposition(nonPositiveDefiniteMatrix, 1e-9);
    }
    
    @Test
    public void testGetRank() {
        assertEquals(2, decomposition.getRank());
    }
}
