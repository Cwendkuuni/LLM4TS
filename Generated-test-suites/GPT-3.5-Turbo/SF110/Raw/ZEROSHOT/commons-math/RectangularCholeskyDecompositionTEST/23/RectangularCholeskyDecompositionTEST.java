import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {
    private RealMatrix matrix;
    
    @Before
    public void setup() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        matrix = MatrixUtils.createRealMatrix(data);
    }
    
    @Test
    public void testConstructor1() {
        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
            assertNotNull(cholesky.getRootMatrix());
            assertEquals(3, cholesky.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testConstructor2() {
        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertNotNull(cholesky.getRootMatrix());
            assertEquals(3, cholesky.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testConstructorException() {
        double[][] data = {{1, 2, 3}, {2, 5, 8}, {3, 8, 15}};
        RealMatrix nonPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(nonPositiveDefiniteMatrix);
            fail("Exception not thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1.0, e.getOffendingValue(), 0.0);
            assertEquals(0, e.getOffendingIndex());
            assertEquals(0.0, e.getThreshold(), 0.0);
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = cholesky.getRootMatrix();
            assertNotNull(rootMatrix);
            double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, -5, 3}};
            RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
            assertEquals(expected, rootMatrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testGetRank() {
        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
            int rank = cholesky.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown");
        }
    }
}