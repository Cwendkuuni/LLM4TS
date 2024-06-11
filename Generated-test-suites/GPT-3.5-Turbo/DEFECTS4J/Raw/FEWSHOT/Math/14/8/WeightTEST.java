package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {
    
    @Test
    public void testConstructor_withArray() {
        double[] weight = { 1.0, 2.0, 3.0 };
        Weight w = new Weight(weight);
        
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);
        
        assertEquals(expectedMatrix, w.getWeight());
    }
    
    @Test
    public void testConstructor_withMatrix() {
        double[][] weight = { { 1.0, 0.0 }, { 0.0, 1.0 } };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight w = new Weight(weightMatrix);
        
        assertEquals(weightMatrix, w.getWeight());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_withNonSquareMatrix() {
        double[][] weight = { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 } };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight w = new Weight(weightMatrix);
    }
}