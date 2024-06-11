package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SchurTransformerTest {
    
    @Test
    public void testConstructor() {
        double[][] testData = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        assertNotNull(transformer.getT());
        assertNotNull(transformer.getP());
        assertNotNull(transformer.getPT());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrix() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
    
    @Test
    public void testGetP() {
        double[][] testData = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix result = transformer.getP();
        
        assertEquals(1, result.getEntry(0, 0), 0);
        assertEquals(0, result.getEntry(1, 0), 0);
        assertEquals(0, result.getEntry(0, 1), 0);
        assertEquals(1, result.getEntry(1, 1), 0);
    }
    
    @Test
    public void testGetPT() {
        double[][] testData = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix result = transformer.getPT();
        
        assertEquals(1, result.getEntry(0, 0), 0);
        assertEquals(0, result.getEntry(0, 1), 0);
        assertEquals(0, result.getEntry(1, 0), 0);
        assertEquals(1, result.getEntry(1, 1), 0);
    }
    
    @Test
    public void testGetT() {
        double[][] testData = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix result = transformer.getT();
        
        assertEquals(1.5615528128088315, result.getEntry(0, 0), 0);
        assertEquals(3.6141027171816047, result.getEntry(0, 1), 0);
        assertEquals(0, result.getEntry(1, 0), 0);
        assertEquals(-0.5615528128088316, result.getEntry(1, 1), 0);
    }
}