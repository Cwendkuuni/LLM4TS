
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(matrix, root.multiply(root.transpose()));
        
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{-1, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
}
