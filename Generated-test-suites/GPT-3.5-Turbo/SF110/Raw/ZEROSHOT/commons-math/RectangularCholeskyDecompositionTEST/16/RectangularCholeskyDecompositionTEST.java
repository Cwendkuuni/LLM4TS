import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithMatrix() {
        double[][] matrixData = { { 4, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull("Root matrix should not be null", decomposition.getRootMatrix());
        assertEquals("Rank should be 2", 2, decomposition.getRank());
    }

    @Test
    public void testConstructorWithMatrixAndSmall() {
        double[][] matrixData = { { 4, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.5);
        assertNotNull("Root matrix should not be null", decomposition.getRootMatrix());
        assertEquals("Rank should be 2", 2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        double[][] matrixData = { { -1, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrixAndSmall() {
        double[][] matrixData = { { -1, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        new RectangularCholeskyDecomposition(matrix, 0.5);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = { { 4, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        
        assertEquals("Root matrix should have the same dimensions as the original matrix", matrix.getRowDimension(), root.getRowDimension());
        assertEquals("Root matrix should have rank number of columns", decomposition.getRank(), root.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = { { 4, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals("Rank should be 2", 2, decomposition.getRank());
    }
}