import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}});
        RealMatrix actualRootMatrix = decomposition.getRootMatrix();

        Assert.assertEquals(expectedRootMatrix, actualRootMatrix);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        int expectedRank = 3;
        int actualRank = decomposition.getRank();

        Assert.assertEquals(expectedRank, actualRank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 97}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    @Test
    public void testSmallDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1e-10, 0, 0}, {0, 1e-10, 0}, {0, 0, 1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        RealMatrix actualRootMatrix = decomposition.getRootMatrix();

        Assert.assertEquals(expectedRootMatrix, actualRootMatrix);
    }

    @Test
    public void testLargeDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1e10, 0, 0}, {0, 1e10, 0}, {0, 0, 1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{1e5, 0, 0}, {0, 1e5, 0}, {0, 0, 1}});
        RealMatrix actualRootMatrix = decomposition.getRootMatrix();

        Assert.assertEquals(expectedRootMatrix, actualRootMatrix);
    }

}