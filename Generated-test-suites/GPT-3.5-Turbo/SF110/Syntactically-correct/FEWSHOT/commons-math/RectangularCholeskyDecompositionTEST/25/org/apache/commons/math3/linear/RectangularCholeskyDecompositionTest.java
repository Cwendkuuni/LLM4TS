
package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructorWithMatrix() {
        double[][] data = {{1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }
    
    @Test
    public void testConstructorWithMatrixAndSmall() {
        double[][] data = {{4, 1}, {1, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }
    
    @Test
    public void testConstructorWithInvalidMatrix() {
        double[][] data = {{1, -1}, {-1, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        
        try {
            new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1.0, e.getMatrixEntryValue(), 0.0);
            assertEquals(0, e.getRow(), 0);
            assertEquals(small, e.getSmallValue(), 0.0);
        }
    }
    
    @Test
    public void testConstructorWithInvalidMatrixOnFirstIteration() {
        double[][] data = {{0, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(0.0, e.getMatrixEntryValue(), 0.0);
            assertEquals(0, e.getRow(), 0);
            assertEquals(0.0, e.getSmallValue(), 0.0);
        }
    }
}
