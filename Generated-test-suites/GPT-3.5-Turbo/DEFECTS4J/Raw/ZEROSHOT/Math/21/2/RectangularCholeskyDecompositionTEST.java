import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        Assert.assertEquals(3, rootMatrix.getRowDimension());
        Assert.assertEquals(3, rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        int rank = decomposition.getRank();
        Assert.assertEquals(3, rank);
    }

}