package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor1() {
        // Test constructor with normal input
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertNotNull(decomposition);
        assertEquals(2, decomposition.getRank());
        assertEquals(matrix, decomposition.getRootMatrix());
    }

    @Test
    public void testConstructor2() {
        // Test constructor with small value
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1, 2}, {2, 3}});
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getOffendingValue(), 0.0);
            assertEquals(0, e.getRow());
            assertEquals(0.0, e.getSmallValue(), 0.0);
        }
    }

    @Test
    public void testGetRootMatrix() {
        // Test getRootMatrix method
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertEquals(Math.sqrt(4), root.getEntry(0, 0), 0.0);
    }

    @Test
    public void testGetRank() {
        // Test getRank method
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(2, decomposition.getRank());
    }
}