import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Assert;

public class RectangularCholeskyDecompositionTest {
    
    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] matrixData = {{4, 1, 1}, {1, 4, 1}, {1, 1, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        } catch (NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();
        double[][] expectedData = {{2, 0, 0}, {0.5, 1.9364916731037094, 0}, {0.5, 0.12909944487358055, 1.9595917942265428}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        Assert.assertArrayEquals(expected.getData(), root.getData());
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        Assert.assertEquals(3, rank);
    }

}