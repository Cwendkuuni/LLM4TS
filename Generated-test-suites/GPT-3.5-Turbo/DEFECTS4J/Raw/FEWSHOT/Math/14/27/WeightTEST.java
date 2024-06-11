package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testConstructor() {
        double[] weightArray = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightArray);
        RealMatrix weightMatrix = weight.getWeight();
        
        assertNotNull(weightMatrix);
        assertEquals(3, weightMatrix.getColumnDimension());
        assertEquals(3, weightMatrix.getRowDimension());
        assertEquals(1.0, weightMatrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, weightMatrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, weightMatrix.getEntry(2, 2), 0.0);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrixException() {
        double[][] weightArray = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightArray);
        new Weight(weightMatrix);
    }
    
    @Test
    public void testConstructorCopy() {
        double[][] weightArray = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightArray);
        Weight weight = new Weight(weightMatrix);
        
        RealMatrix copiedMatrix = weight.getWeight();
        assertNotNull(copiedMatrix);
        assertEquals(3, copiedMatrix.getColumnDimension());
        assertEquals(3, copiedMatrix.getRowDimension());
        assertEquals(1.0, copiedMatrix.getEntry(0, 0), 0.0);
        assertEquals(5.0, copiedMatrix.getEntry(1, 1), 0.0);
        assertEquals(9.0, copiedMatrix.getEntry(2, 2), 0.0);
    }
}