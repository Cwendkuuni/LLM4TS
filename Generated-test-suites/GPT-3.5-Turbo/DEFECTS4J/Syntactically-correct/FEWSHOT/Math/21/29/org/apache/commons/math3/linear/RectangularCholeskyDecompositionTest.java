package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    private RectangularCholeskyDecomposition decomposition;
    private double[][] testData;

    @Before
    public void setUp() {
        double[][] data = {
                {10, 1, 2, 3},
                {1, 10, 3, 4},
                {2, 3, 10, 5},
                {3, 4, 5, 10}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-20;
        decomposition = new RectangularCholeskyDecomposition(matrix, small);
        testData = matrix.multiply(decomposition.getRootMatrix().transpose()).getData();
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(4, rootMatrix.getRowDimension());
        assertEquals(4, rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertNotEquals(0, rank);
    }
}