package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    private RectangularCholeskyDecomposition decomposition;
    
    @Before
    public void setUp() {
        double[][] matrixData = {{9, 3, 2}, {3, 10, 5}, {2, 5, 8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(decomposition.getRank(), rootMatrix.getColumnDimension());
        
        double[][] expectedData = {{3, 0, 0}, {1, 3, 0}, {2/3, 5/3, 5/3}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        
        for (int i = 0; i < expectedMatrix.getRowDimension(); i++) {
            for (int j = 0; j < expectedMatrix.getColumnDimension(); j++) {
                assertEquals(expectedMatrix.getEntry(i, j), rootMatrix.getEntry(i, j), 1e-10);
            }
        }
    }
    
    @Test
    public void testGetRank() {
        assertEquals(3, decomposition.getRank());
    }
}