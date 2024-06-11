
package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SchurTransformerTest {
    @Test
    public void testGetP() {
        // create test matrix
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // assert getP() method
        double[][] expectedP = {{-0.5657, -0.8246}, {-0.8246, 0.5657}};
        RealMatrix expectedMatrixP = MatrixUtils.createRealMatrix(expectedP);
        assertEquals(expectedMatrixP, transformer.getP());
    }
    
    @Test
    public void testGetPT() {
        // create test matrix
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // assert getPT() method
        double[][] expectedPT = {{-0.5657, -0.8246}, {-0.8246, 0.5657}};
        RealMatrix expectedMatrixPT = MatrixUtils.createRealMatrix(expectedPT).transpose();
        assertEquals(expectedMatrixPT, transformer.getPT());
    }
    
    @Test
    public void testGetT() {
        // create test matrix
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // assert getT() method
        double[][] expectedT = {{3.4142, -0.4574}, {0, 0.5858}};
        RealMatrix expectedMatrixT = MatrixUtils.createRealMatrix(expectedT);
        assertEquals(expectedMatrixT, transformer.getT());
    }
}
