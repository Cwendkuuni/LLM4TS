
package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

public class SchurTransformerTest {
    
    @Test
    public void testGetP() {
        double[][] inputData = {{1, 2}, {3, 4}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputData);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        
        double[][] expectedData = {{0.2361, 0.9701}, {0.9701, -0.2361}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        
        assertArrayEquals(expectedMatrix.getData(), transformer.getP().getData(), 0.0001);
    }
    
    @Test
    public void testGetPT() {
        double[][] inputData = {{1, 2}, {3, 4}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputData);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        
        double[][] expectedData = {{0.2361, 0.9701}, {0.9701, -0.2361}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        
        assertArrayEquals(expectedMatrix.getData(), transformer.getPT().getData(), 0.0001);
    }
    
    @Test
    public void testGetT() {
        double[][] inputData = {{1, 2}, {3, 4}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputData);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        
        double[][] expectedData = {{5.3723, 0}, {0, -0.3723}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        
        assertArrayEquals(expectedMatrix.getData(), transformer.getT().getData(), 0.0001);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] inputData = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputData);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
    }
    
    @Test
    public void testConvergenceFailed() {
        double[][] inputData = {{1, 0}, {0, 1}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputData);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
    }
    
}

