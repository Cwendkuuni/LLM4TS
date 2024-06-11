import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.exception.NonPositiveDefiniteMatrixException;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecompositionPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 0.86602540378444, 0.43301270189218}, {0.5, 0.43301270189218, 0.5}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, -0.5, 0.5}, {-0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    @Test
    public void testDecompositionSmallDiagonalElements() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.5}, {0.1, 1.0, 0.1}, {0.5, 0.1, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.5}, {0.1, 1.0, 0}, {0.5, 0, 1.0}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
        Assert.assertEquals(decomposition.getRank(), 2);
    }

    @Test
    public void testDecompositionIdentityMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
        Assert.assertEquals(decomposition.getRank(), 2);
    }

    @Test
    public void testDecompositionSymmetricMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
        Assert.assertEquals(decomposition.getRank(), 2);
    }

    @Test
    public void testGetRank() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        int actualRank = decomposition.getRank();
        Assert.assertEquals(3, actualRank);
    }
}