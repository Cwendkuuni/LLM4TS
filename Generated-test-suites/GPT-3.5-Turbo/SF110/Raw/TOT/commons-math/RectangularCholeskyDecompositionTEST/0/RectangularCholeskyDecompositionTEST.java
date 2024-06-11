import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] inputMatrixData = {{5, 2}, {2, 1}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputMatrixData);

        try {
            decomposition = new RectangularCholeskyDecomposition(inputMatrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{2.236, 0}, {0.894, 0.447}});
        RealMatrix actualRoot = decomposition.getRootMatrix();

        Assert.assertEquals(expectedRoot, actualRoot);
    }

    @Test
    public void testGetRank() {
        int expectedRank = 2;
        int actualRank = decomposition.getRank();

        Assert.assertEquals(expectedRank, actualRank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        double[][] nonPositiveDefiniteMatrixData = {{-1, 2}, {2, 5}};
        RealMatrix nonPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(nonPositiveDefiniteMatrixData);

        RectangularCholeskyDecomposition decompositionWithNonPositiveDefiniteMatrix =
                new RectangularCholeskyDecomposition(nonPositiveDefiniteMatrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionWithSmall() throws NonPositiveDefiniteMatrixException {
        double[][] nonPositiveDefiniteMatrixData = {{1, 2}, {2, 1}};
        RealMatrix nonPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(nonPositiveDefiniteMatrixData);

        RectangularCholeskyDecomposition decompositionWithNonPositiveDefiniteMatrix =
                new RectangularCholeskyDecomposition(nonPositiveDefiniteMatrix, 0.1);
    }
}