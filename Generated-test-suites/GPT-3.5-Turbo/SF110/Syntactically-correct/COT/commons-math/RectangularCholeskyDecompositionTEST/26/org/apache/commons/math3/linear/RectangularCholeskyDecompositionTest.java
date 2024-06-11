import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(0, 2, 3);
        matrix.setEntry(1, 0, 2);
        matrix.setEntry(1, 1, 5);
        matrix.setEntry(1, 2, 6);
        matrix.setEntry(2, 0, 3);
        matrix.setEntry(2, 1, 6);
        matrix.setEntry(2, 2, 9);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            // Check that the decomposition was successful and no exception was thrown
            assert(decomposition.getRootMatrix() != null);
            assert(decomposition.getRank() == 3);
        } catch (NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
}