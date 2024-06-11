import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{1, 2, 3}, {2, 5, 7}, {3, 7, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        RealMatrix root = decomposition.getRootMatrix();
        // Add assertions here to test root matrix
    }

    @Test
    public void testGetRank() {
        double[][] data = {{1, 2, 3}, {2, 5, 7}, {3, 7, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        int rank = decomposition.getRank();
        // Add assertions here to test rank
    }

}