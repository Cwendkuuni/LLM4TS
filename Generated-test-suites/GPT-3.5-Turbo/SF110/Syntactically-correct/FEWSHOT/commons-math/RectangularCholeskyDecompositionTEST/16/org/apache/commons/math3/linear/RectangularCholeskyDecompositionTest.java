package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    private RectangularCholeskyDecomposition decomposition;
    private double[][] testData;
    
    @Before
    public void setup() {
        testData = new double[][]{{4.0, 12.0, -16.0}, {12.0, 37.0, -43.0}, {-16.0, -43.0, 98.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(decomposition.getRootMatrix());
        assertTrue(decomposition.getRank() > 0);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(testData.length, rootMatrix.getRowDimension());
        assertEquals(decomposition.getRank(), rootMatrix.getColumnDimension());
    }
    
    @Test
    public void testGetRank() {
        assertEquals(testData.length, decomposition.getRank());
    }
}