import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor1() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);

        assertEquals(3, decomp.getRootMatrix().getRowDimension());
        assertEquals(3, decomp.getRootMatrix().getColumnDimension());
        assertEquals(3, decomp.getRank());
    }

    @Test
    public void testConstructor2() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        double small = 0.1;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        assertEquals(3, decomp.getRootMatrix().getRowDimension());
        assertEquals(3, decomp.getRootMatrix().getColumnDimension());
        assertEquals(3, decomp.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor3() {
        double[][] data = {{-1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor4() {
        double[][] data = {{1, 2, 3}, {4, -5, 6}, {7, 8, 9}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor5() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, -9}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
    }

}