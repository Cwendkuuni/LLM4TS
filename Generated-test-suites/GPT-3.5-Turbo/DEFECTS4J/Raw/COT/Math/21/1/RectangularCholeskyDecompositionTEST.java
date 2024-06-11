
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] data = {{4, -1, 2}, {-1, 5, -3}, {2, -3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);
            assertNotNull(rcd.getRootMatrix());
            assertTrue(rcd.getRank() > 0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Error decomposing matrix.");
        }
    }
    
    @Test
    public void testRectangularCholeskyDecompositionWithSmallDiagonalElements() {
        double[][] data = {{4, -1, 2}, {-1, 5, -3}, {2, -3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1);
            assertNotNull(rcd.getRootMatrix());
            assertTrue(rcd.getRank() > 0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Error decomposing matrix.");
        }
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{-4, -1, 2}, {-1, 5, -3}, {2, -3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithZeroDiagonalElement() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, -1, 2}, {-1, 0, -3}, {2, -3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithNegativeDiagonalElement() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, -1, 2}, {-1, -5, -3}, {2, -3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithDependentColumns() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, -1, 2}, {-1, 5, -1}, {2, -1, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
}


