import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.util.Precision.EPSILON;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructWithSmallPositiveDiagonalElements() throws Exception {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2, 0.1, 0.1}, {0.1, 2, 0.1}, {0.1, 0.1, 2}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomp.getRootMatrix();

        // Check that the root matrix has the expected shape
        int numRows = matrix.getRowDimension();
        int numColumns = decomp.getRank();
        Assert.assertEquals(numRows, numColumns);
        Assert.assertEquals(numRows, rootMatrix.getRowDimension());
        Assert.assertEquals(numColumns, rootMatrix.getColumnDimension());

        // Check that the root matrix has the expected values
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                double expected = (i == j) ? Math.sqrt(matrix.getEntry(i, i)) : 0;
                Assert.assertEquals(expected, rootMatrix.getEntry(i, j), EPSILON);
            }
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructWithNegativeDiagonalElement() throws Exception {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2, 0.1, 0.1}, {0.1, 1.5, 0.1}, {0.1, 0.1, 2}});
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructWithNonPositiveDefiniteMatrix() throws Exception {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2, 0.1, 0.1}, {0.1, 2, -0.1}, {0.1, 0.1, 2}});
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRank() throws Exception {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2, 0.1, 0.1}, {0.1, 2, 0.1}, {0.1, 0.1, 2}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomp.getRank();
        Assert.assertEquals(3, rank);
    }

    @Test
    public void testGetRootMatrix() throws Exception {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2, 0.1, 0.1}, {0.1, 2, 0.1}, {0.1, 0.1, 2}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomp.getRootMatrix();

        // Check that the root matrix has the expected shape
        int numRows = matrix.getRowDimension();
        int numColumns = decomp.getRank();
        Assert.assertEquals(numRows, numColumns);
        Assert.assertEquals(numRows, rootMatrix.getRowDimension());
        Assert.assertEquals(numColumns, rootMatrix.getColumnDimension());

        // Check that the root matrix has the expected values
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                double expected = (i == j) ? Math.sqrt(matrix.getEntry(i, i)) : 0;
                Assert.assertEquals(expected, rootMatrix.getEntry(i, j), EPSILON);
            }
        }
    }

}