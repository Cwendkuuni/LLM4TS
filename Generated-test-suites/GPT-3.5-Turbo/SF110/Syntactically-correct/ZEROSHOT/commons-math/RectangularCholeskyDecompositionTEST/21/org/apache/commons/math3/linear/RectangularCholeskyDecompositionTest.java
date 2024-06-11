import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RealMatrix matrix;
    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] data = {{4, 2, 3}, {2, 9, 5}, {3, 5, 7}};
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();
        assertNotNull(root);
        assertTrue(root instanceof RealMatrix);
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] data = {{-4, 2, 3}, {2, 9, 5}, {3, 5, 7}};
        RealMatrix negativeDefiniteMatrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition negativeDecomposition = new RectangularCholeskyDecomposition(negativeDefiniteMatrix);
    }
    
    // Add more test methods to comprehensively test all methods in RectangularCholeskyDecomposition class
    
}

