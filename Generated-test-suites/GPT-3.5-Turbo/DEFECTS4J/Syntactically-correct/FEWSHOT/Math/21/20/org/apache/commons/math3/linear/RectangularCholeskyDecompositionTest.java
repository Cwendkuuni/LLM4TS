package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor() {
        // Test positive semidefinite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            { 4, -1,  1},
            {-1,  2, -1},
            { 1, -1,  2}
        });
        double small = 1e-10;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
        
        // Test non positive semidefinite matrix - should throw exception
        matrix = MatrixUtils.createRealMatrix(new double[][]{
            { 1,  2,  3},
            { 2,  5,  7},
            { 3,  7,  11}
        });
        boolean exceptionThrown = false;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            { 4, -1,  1},
            {-1,  2, -1},
            { 1, -1,  2}
        });
        double small = 1e-10;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        RealMatrix root = decomposition.getRootMatrix();
        
        assertNotNull(root);
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        
        // Verify root matrix
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{
            { 2,  0,  0},
            {-1,  1,  0},
            { 0, -1,  1}
        });
        
        assertTrue(expectedRoot.equals(root, 1e-10));
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            { 4, -1,  1},
            {-1,  2, -1},
            { 1, -1,  2}
        });
        double small = 1e-10;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        int rank = decomposition.getRank();
        
        assertEquals(3, rank);
    }
}