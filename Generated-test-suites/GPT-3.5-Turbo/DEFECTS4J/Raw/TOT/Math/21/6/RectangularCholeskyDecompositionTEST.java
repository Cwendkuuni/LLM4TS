import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test case 1: Typical use case with a positive semidefinite matrix
        double[][] mData = {
                {4.0, 3.0, 2.0},
                {3.0, 4.0, 1.0},
                {2.0, 1.0, 4.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(mData);
        double small = 1.0e-10;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix root = decomp.getRootMatrix();
        Assert.assertEquals(matrix, root.multiply(root.transpose()));
        Assert.assertEquals(matrix.getRowDimension(), decomp.getRank());

        // Test case 2: Edge case with a matrix of size 1x1
        double[][] mData2 = {{2.0}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(mData2);
        RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(matrix2, small);

        RealMatrix root2 = decomp2.getRootMatrix();
        Assert.assertEquals(matrix2, root2.multiply(root2.transpose()));
        Assert.assertEquals(matrix2.getRowDimension(), decomp2.getRank());

        // Test case 3: Error scenario with a non-positive semidefinite matrix
        double[][] mData3 = {
                {4.0, -3.0, 2.0},
                {-3.0, 4.0, 1.0},
                {2.0, 1.0, -4.0}
        };
        RealMatrix matrix3 = MatrixUtils.createRealMatrix(mData3);

        Exception exception = null;
        try {
            RectangularCholeskyDecomposition decomp3 = new RectangularCholeskyDecomposition(matrix3, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            exception = e;
        }

        Assert.assertNotNull(exception);
    }
}