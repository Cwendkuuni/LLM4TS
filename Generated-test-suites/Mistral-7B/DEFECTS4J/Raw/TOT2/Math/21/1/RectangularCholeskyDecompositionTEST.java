
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.1;
        RectangularCholeskyDecomposition rectangle = new RectangularCholeskyDecomposition(matrix, small);
        // Add assertions here to check if the object is initialized correctly
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, -3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.1;
        RectangularCholeskyDecomposition rectangle = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeThreshold() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = -0.1;
        RectangularCholeskyDecomposition rectangle = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.1;
        RectangularCholeskyDecomposition rectangle = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.44948974278317, 0.89899558442645}, {0.84161468375751, 1.0, 0.44725346338714}, {0.44725346338714, 0.89899558442645, 1.0}});
        RealMatrix actual = rectangle.getRootMatrix();
        assertTrue(MatrixUtils.equal(expected, actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRootMatrixWithNullInput() {
        RectangularCholeskyDecomposition rectangle = new RectangularCholeskyDecomposition(null, 0.1);
        RealMatrix actual = rectangle.getRootMatrix();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRootMatrixWithWrongDimensions() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        double small = 0.1;
        RectangularCholeskyDecomposition rectangle = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix actual = rectangle.getRootMatrix();
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.1;
        RectangularCholeskyDecomposition rectangle = new RectangularCholeskyDecomposition(matrix, small);
        int expectedRank = 3;
        int actualRank = rectangle.getRank();
        assertEquals(expectedRank, actualRank);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRankWithNullInput() {
        RectangularCholeskyDecomposition rectangle = new RectangularCholeskyDecomposition(null, 0.1);
        int actualRank = rectangle.getRank();
    }
}