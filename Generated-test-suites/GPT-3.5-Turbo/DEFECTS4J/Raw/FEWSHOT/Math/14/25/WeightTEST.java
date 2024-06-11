
package org.apache.commons.math3.optimization;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class WeightTest {
    @Test
    public void testConstructor_doubleArray() {
        double[] weight = {1.0, 2.0, 3.0, 4.0};
        
        Weight w = new Weight(weight);
        
        RealMatrix result = w.getWeight();
        assertTrue(result instanceof Array2DRowRealMatrix);
        
        double[][] array = result.getData();
        assertEquals(weight.length, array.length);
        assertEquals(weight.length, array[0].length);
        for (int i = 0; i < weight.length; i++) {
            assertEquals(weight[i], array[i][i], 0.0);
        }
    }
    
    @Test
    public void testConstructor_RealMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        
        Weight w = new Weight(weight);
        
        RealMatrix result = w.getWeight();
        assertEquals(weight, result);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_RealMatrix_NonSquareMatrixException() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        
        new Weight(weight);
    }
}
