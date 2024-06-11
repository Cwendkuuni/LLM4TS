import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 2},
                {2, 3, 1},
                {2, 1, 3}
        });
        double small = 0.001;

        // When
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Then
        RealMatrix root = decomposition.getRootMatrix();
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {FastMath.sqrt(1), 0, -FastMath.sqrt(2 / 3)},
                {FastMath.sqrt(2), FastMath.sqrt(2 / 3), 0},
                {0, -FastMath.sqrt(2 / 3), FastMath.sqrt(3)}
        });

        Assert.assertEquals("The roots should be equal.", expectedRoot, root);
        Assert.assertEquals("The rank should be 3.", 3, decomposition.getRank());

        // Given
        matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 2},
                {2, 3, 1},
                {2, 1, 3},
                {0, 0, 0}
        });

        // Then
        Assert.throwsException(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    @Test
    public void testGetRootMatrix() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 2},
                {2, 3, 1},
                {2, 1, 3}
        });
        double small = 0.001;

        // When
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Then
        RealMatrix root = decomposition.getRootMatrix();
        RealVector vector = root.getColumnVector(0);
        Assert.assertArrayEquals("The first column of the root matrix should be (sqrt(1), 0, -sqrt(2/3))", new double[]{FastMath.sqrt(1), 0, -FastMath.sqrt(2 / 3)}, vector.toArray());
    }

    @Test
    public void testGetRank() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 2},
                {2, 3, 1},
                {2, 1, 3}
        });
        double small = 0.001;

        // When
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Then
        int rank = decomposition.getRank();
        Assert.assertEquals("The rank should be 3.", 3, rank);

        // Given
        matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 2},
                {2, 3, 1},
                {2, 1, 3},
                {0, 0, 0}
        });

        // Then
        Assert.assertEquals("The rank should be 2.", 2, new RectangularCholeskyDecomposition(matrix, small).getRank());
    }

}