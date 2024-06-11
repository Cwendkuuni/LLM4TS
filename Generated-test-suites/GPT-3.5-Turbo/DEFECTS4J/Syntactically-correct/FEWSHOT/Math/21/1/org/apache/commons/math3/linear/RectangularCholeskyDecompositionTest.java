import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private static final double SMALL = 1e-15;

    @Test
    public void testConstructor_InvalidMatrix() {
        double[][] matrixData = {{1, 2}, {2, -1}, {3, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, SMALL);
            fail("Expected NonPositiveDefiniteMatrixException to be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1, e.getRow());
            assertEquals(-1, e.getMainDiagonalElement(), 0.0);
        }
    }

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{4, -1}, {-1, 4}, {2, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, SMALL);
        RealMatrix root = decomposition.getRootMatrix();
        double[][] rootData = root.getData();
        assertEquals(3, rootData.length);
        assertEquals(2, rootData[0].length);
        assertEquals(2, rootData[1].length);
        assertEquals(2, rootData[2].length);

        double[][] expectedData = {
                {2, 0},
                {-0.5, 1},
                {-0.5, -1}
        };
        assertTrue(MathArrays.equals(expectedData, rootData, SMALL));
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = {{4, -1}, {-1, 4}, {2, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, SMALL);
        assertEquals(3, decomposition.getRank());
    }
}