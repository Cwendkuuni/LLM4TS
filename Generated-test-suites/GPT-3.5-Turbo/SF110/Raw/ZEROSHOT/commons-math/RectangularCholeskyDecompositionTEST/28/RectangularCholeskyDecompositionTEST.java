import org.apache.commons.math3.linear.*;
import org.junit.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 1.0);
        matrix.setEntry(1, 0, 2.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 3.0);
        matrix.setEntry(2, 0, 1.0);
        matrix.setEntry(2, 1, 3.0);
        matrix.setEntry(2, 2, 6.0);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        RealMatrix rootMatrix = decomposition.getRootMatrix();

        Assert.assertEquals(3.0, rootMatrix.getEntry(0, 0), 0.00001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(0, 1), 0.00001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(0, 2), 0.00001);
        Assert.assertEquals(1.0, rootMatrix.getEntry(1, 0), 0.00001);
        Assert.assertEquals(2.0, rootMatrix.getEntry(1, 1), 0.00001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(1, 2), 0.00001);
        Assert.assertEquals(0.5, rootMatrix.getEntry(2, 0), 0.00001);
        Assert.assertEquals(1.0, rootMatrix.getEntry(2, 1), 0.00001);
        Assert.assertEquals(1.0, rootMatrix.getEntry(2, 2), 0.00001);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 1.0);
        matrix.setEntry(1, 0, 2.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 3.0);
        matrix.setEntry(2, 0, 1.0);
        matrix.setEntry(2, 1, 3.0);
        matrix.setEntry(2, 2, 6.0);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        int rank = decomposition.getRank();

        Assert.assertEquals(3, rank);
    }
}