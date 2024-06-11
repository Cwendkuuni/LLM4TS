import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Test case 1
        double[][] data1 = {{1, 2, 3}, {2, 5, 8}, {3, 8, 14}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(data1);
        RectangularCholeskyDecomposition decomp1 = new RectangularCholeskyDecomposition(matrix1, 1e-10);
        RealMatrix rootMatrix1 = decomp1.getRootMatrix();
        double[][] expected1 = {{1.0, 0.0}, {2.0, 1.0}, {3.0, 2.0}};
        assertArrayEquals(expected1, rootMatrix1.getData());

        // Test case 2
        double[][] data2 = {{2, 4, 6}, {4, 10, 16}, {6, 16, 28}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(data2);
        RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(matrix2, 1e-10);
        RealMatrix rootMatrix2 = decomp2.getRootMatrix();
        double[][] expected2 = {{1.4142135623730951, 0.0}, {2.8284271247461903, 1.0}, {4.242640687119285, 2.0}};
        assertArrayEquals(expected2, rootMatrix2.getData());
    }

    @Test
    public void testGetRank() {
        // Test case 1
        double[][] data1 = {{1, 2, 3}, {2, 5, 8}, {3, 8, 14}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(data1);
        RectangularCholeskyDecomposition decomp1 = new RectangularCholeskyDecomposition(matrix1, 1e-10);
        int rank1 = decomp1.getRank();
        assertEquals(2, rank1);

        // Test case 2
        double[][] data2 = {{2, 4, 6}, {4, 10, 16}, {6, 16, 28}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(data2);
        RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(matrix2, 1e-10);
        int rank2 = decomp2.getRank();
        assertEquals(2, rank2);
    }

}