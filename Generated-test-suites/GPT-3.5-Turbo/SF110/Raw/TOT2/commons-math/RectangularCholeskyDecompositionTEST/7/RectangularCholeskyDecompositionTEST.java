import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 5}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomp);
    }
    
    @Test
    public void testConstructorWithMatrixAndSmall() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{6, 1}, {1, 8}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        assertNotNull(decomp);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomp.getRootMatrix();
        assertNotNull(root);
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        int rank = decomp.getRank();
        assertEquals(2, rank);
    }
}