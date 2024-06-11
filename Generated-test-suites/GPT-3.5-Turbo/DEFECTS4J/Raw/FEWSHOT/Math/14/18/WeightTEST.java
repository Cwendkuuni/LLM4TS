package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class WeightTest {
    
    @Test
    public void testConstructor_doubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        
        assertEquals(expected, w.getWeight());
    }
    
    @Test
    public void testConstructor_nonSquareMatrixException() {
        RealMatrix weight = new Array2DRowRealMatrix(2, 3);
        boolean exceptionThrown = false;
        try {
            Weight w = new Weight(weight);
        } catch (NonSquareMatrixException e) {
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testGetWeight() {
        double[] weightValues = {1.0, 2.0, 3.0};
        Weight w = new Weight(weightValues);
        
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        
        assertEquals(expected, w.getWeight());
    }
}