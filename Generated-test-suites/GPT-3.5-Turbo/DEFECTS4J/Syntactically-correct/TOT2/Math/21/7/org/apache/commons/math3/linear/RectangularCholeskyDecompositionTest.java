import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] matrixData = {{4, 1}, {1, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix root = decomposition.getRootMatrix();

        Assert.assertEquals(root.getRowDimension(), matrixData.length);
        Assert.assertEquals(root.getColumnDimension(), 1);  // Because the rank of the matrix is 1
    }

    @Test
    public void testRectangularCholeskyDecompositionException() {
        double[][] matrixData = {{-1, 1}, {1, 5}};  // Matrix with negative diagonal element
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-10;

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException was not thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception expected
        }
    }

    // Add more test cases as needed for other methods

}