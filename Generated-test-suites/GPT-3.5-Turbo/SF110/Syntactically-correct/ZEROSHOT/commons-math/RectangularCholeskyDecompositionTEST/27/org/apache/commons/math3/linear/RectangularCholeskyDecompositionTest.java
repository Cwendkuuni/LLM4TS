package org.apache.commons.math3.linear;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    private RectangularCholeskyDecomposition decomposition;
    
    @Before
    public void setup() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2, 6, -8}, {0, 1, -5}, {0, 0, 3}});
        RealMatrix root = decomposition.getRootMatrix();
        
        for (int i = 0; i < expected.getRowDimension(); i++) {
            for (int j = 0; j < expected.getColumnDimension(); j++) {
                Assert.assertEquals(expected.getEntry(i, j), root.getEntry(i, j), 0.0001);
            }
        }
    }
    
    @Test
    public void testGetRank() {
        int expected = 3;
        int rank = decomposition.getRank();
        
        Assert.assertEquals(expected, rank);
    }
}