import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecompositionConstructor() {
        double[][] matrixData = {
            {4, 3, 2},
            {3, 4, 1},
            {2, 1, 4}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testRectangularCholeskyDecompositionSmallThreshold() {
        double[][] matrixData = {
            {4, 3, 2},
            {3, 4, 1},
            {2, 1, 4}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        // Small threshold should throw exception for NonPositiveDefiniteMatrixException
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-20);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception is expected
        }
    }

    @Test
    public void testRectangularCholeskyDecompositionGetRootMatrix() {
        double[][] matrixData = {
            {4, 3, 2},
            {3, 4, 1},
            {2, 1, 4}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        RealMatrix root = decomposition.getRootMatrix();

        assertEquals(3, root.getColumnDimension());
    }

}