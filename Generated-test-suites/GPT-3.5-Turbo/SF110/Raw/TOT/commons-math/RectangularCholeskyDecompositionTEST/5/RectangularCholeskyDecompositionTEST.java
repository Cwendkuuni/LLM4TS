import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RealMatrix matrix;
    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() throws Exception {
        double[][] data = {{4, 12, -16},
                           {12, 37, -43},
                           {-16, -43, 98}};
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorWithValidMatrix() {
        assertNotNull(decomposition.getRootMatrix());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 12, -16},
                           {12, 37, -43},
                           {-16, -43, -98}}; // Non-positive diagonal element
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorWithZeroSmallValue() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 12, -16},
                           {12, 37, -43},
                           {-16, -43, 98}};
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);

        assertNotNull(decomposition.getRootMatrix());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrixAndZeroSmallValue() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 12, -16},
                           {12, 37, -43},
                           {-16, -43, -98}}; // Non-positive diagonal element
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();

        assertNotNull(root);
        assertEquals(matrix.getRowDimension(), root.getRowDimension());
        assertEquals(decomposition.getRank(), root.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(matrix.getRowDimension(), rank);
    }

    @Test
    public void testGetRankWithNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 12, -16},
                           {12, 37, -43},
                           {-16, -43, -98}}; // Non-positive diagonal element
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix);

        int rank = decomposition.getRank();
        assertEquals(0, rank);
    }

}