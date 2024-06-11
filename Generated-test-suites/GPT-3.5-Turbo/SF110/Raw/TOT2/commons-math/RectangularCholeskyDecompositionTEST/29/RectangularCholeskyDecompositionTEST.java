
import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithPositiveMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 4}});
        new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(4.0, root.getEntry(0, 0), 0.0);
        assertEquals(0.0, root.getEntry(0, 1), 0.0);
        assertEquals(6.0, root.getEntry(1, 0), 0.0);
        assertEquals(3.0, root.getEntry(1, 1), 0.0);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12}, {12, 37}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, decomposition.getRank());
    }

}
