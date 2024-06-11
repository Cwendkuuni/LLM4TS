import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor1_NonPositiveDefiniteMatrixException() {
        double[][] data = {{-1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor2_NonPositiveDefiniteMatrixException() {
        double[][] data = {{2, 0}, {0, -1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 12}, {12, 37}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        RealMatrix rootMatrix = decomposition.getRootMatrix();
        double[][] expectedData = {{2, 6}, {0, 1}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);

        assertArrayEquals(expectedMatrix.getData(), rootMatrix.getData());
    }

    @Test
    public void testGetRank() {
        double[][] data = {{4, 12}, {12, 37}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor2_NonPositiveDefiniteMatrixException_EdgeCase() {
        double[][] data = {{0, 0}, {0, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
    }

}