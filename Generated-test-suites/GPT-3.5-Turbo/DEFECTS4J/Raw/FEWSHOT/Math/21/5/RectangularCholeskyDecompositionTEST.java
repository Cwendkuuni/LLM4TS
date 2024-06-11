package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    private RealMatrix matrix;
    private double small;
    private RectangularCholeskyDecomposition decomposition;
    
    @Before
    public void setUp() {
        double[][] data = {
            { 4, -2, 0 },
            { -2, 4, -1 },
            { 0, -1, 2 }
        };
        matrix = MatrixUtils.createRealMatrix(data);
        small = 0.01;
        decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();
        double[][] expectedData = {
            { 1.73205, 0.0 },
            { -0.57735, 1.1547 },
            { 0.0, -0.86603 }
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertEquals(expectedMatrix, root);
    }
    
    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }
}