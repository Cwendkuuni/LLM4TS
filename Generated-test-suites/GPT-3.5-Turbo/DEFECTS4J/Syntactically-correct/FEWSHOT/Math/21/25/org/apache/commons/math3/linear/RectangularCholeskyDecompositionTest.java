package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    private RectangularCholeskyDecomposition decomposition;
    private double[][] matrixData;
    private RealMatrix matrix;
    private double small;
    
    @Before
    public void setup() {
        small = 0.001;
        
        matrixData = new double[][]{{1, 0}, {0, 1}};
        matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();
        
        assertNotNull(root);
        assertEquals(root.getRowDimension(), matrix.getRowDimension());
        assertEquals(root.getColumnDimension(), decomposition.getRank());
        
        double[][] rootData = root.getData();
        
        assertEquals(rootData[0][0], FastMath.sqrt(matrixData[0][0]), 0.0001);
        assertEquals(rootData[1][1], FastMath.sqrt(matrixData[1][1]), 0.0001);
    }
    
    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(rank, matrix.getRowDimension());
    }
}