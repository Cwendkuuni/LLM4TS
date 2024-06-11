import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertNotNull(decomposition);
    }

    @Test
    public void testConstructorWithMatrixAndSmall() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        Assert.assertNotNull(decomposition);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        Assert.assertNotNull(rootMatrix);
    }

    @Test
    public void testGetRank() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        int rank = decomposition.getRank();

        Assert.assertEquals(3, rank);
    }

    // Test edge cases and exception handling scenarios
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] data = {{-4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

}