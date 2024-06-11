
package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructorWithMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        double[][] expectedRootData = {{1, 0}, {3, -2}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);
        assertEquals(expectedRoot, rcd.getRootMatrix());
        
        int expectedRank = 2;
        assertEquals(expectedRank, rcd.getRank());
    }
    
    @Test
    public void testConstructorWithMatrixAndSmall() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.5;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        
        double[][] expectedRootData = {{1, 0}, {2, -1}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);
        assertEquals(expectedRoot, rcd.getRootMatrix());
        
        int expectedRank = 2;
        assertEquals(expectedRank, rcd.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithMatrixAndSmall_NonPositiveDefiniteException() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{1, 2}, {2, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.5;
        new RectangularCholeskyDecomposition(matrix, small);
    }

}
