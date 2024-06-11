package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class WeightTest {
    
    @Test
    public void testConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);
        
        assertEquals(expectedMatrix, w.getWeight());
    }
    
    @Test
    public void testConstructorWithMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 2.0);
        weightMatrix.setEntry(2, 2, 3.0);
        
        Weight w = new Weight(weightMatrix);
        
        assertEquals(weightMatrix, w.getWeight());
    }
}