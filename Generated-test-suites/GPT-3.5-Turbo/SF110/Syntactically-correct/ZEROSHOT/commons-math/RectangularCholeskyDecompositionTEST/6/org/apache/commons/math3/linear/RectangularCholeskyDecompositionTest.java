import org.apache.commons.math3.linear.*;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    private RealMatrix matrix;
    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setup() {
        matrix = MatrixUtils.createRealMatrix(new double[][]{{10, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        Assert.assertNotNull(rootMatrix);
        // Add specific assertions for the root matrix
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        Assert.assertEquals(3, rank);
    }
}