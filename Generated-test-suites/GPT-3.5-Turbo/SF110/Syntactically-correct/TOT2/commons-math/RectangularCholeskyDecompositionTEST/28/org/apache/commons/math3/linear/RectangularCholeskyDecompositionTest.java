package org.apache.commons.math3.linear;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testConstructor() {
        double[][] data = {{4, 1, 2}, {1, 2, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(cholesky);
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 1, 2}, {1, 2, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(cholesky.getRootMatrix());
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{4, 1, 2}, {1, 2, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, cholesky.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorException() {
        double[][] data = {{-1, 2}, {2, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
    }
    
    // Add more test cases here for edge cases and exception handling scenarios
    
}