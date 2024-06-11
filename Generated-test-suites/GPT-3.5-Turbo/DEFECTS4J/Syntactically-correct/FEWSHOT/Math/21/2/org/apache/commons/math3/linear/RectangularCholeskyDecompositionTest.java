package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 1.0);
        
        double small = 0.0001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(4, 4);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 1.0);
        matrix.setEntry(3, 3, 0.5);
        
        double small = 0.0001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(4, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(5, 5);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 1.0);
        matrix.setEntry(3, 3, 0.5);
        matrix.setEntry(4, 4, 0.1);

        double small = 0.0001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        int rank = decomposition.getRank();
        assertEquals(5, rank);
    }
}