
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        double[][] data = {
                {4, 12},
                {12, 37}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.00001;

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            Assert.assertNotNull(decomposition);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        double[][] data = {
                {1, 2},
                {2, 1}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.00001;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] data = {
                {4, 12},
                {12, 37}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.00001;

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            Assert.assertEquals(2, root.getRowDimension());
            Assert.assertEquals(2, root.getColumnDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetRank() {
        double[][] data = {
                {4, 12},
                {12, 37}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.00001;

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomposition.getRank();
            Assert.assertEquals(2, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
   @Test
    public void testGetRankWithZeroRankMatrix() {
        double[][] data = {
                {0, 0},
                {0, 0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.00001;

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomposition.getRank();
            Assert.assertEquals(0, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

}