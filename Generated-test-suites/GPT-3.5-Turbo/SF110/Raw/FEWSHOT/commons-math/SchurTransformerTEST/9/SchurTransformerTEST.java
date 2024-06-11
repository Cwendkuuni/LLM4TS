package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

public class SchurTransformerTest {
    @Test
    public void testGetP() {
        // Create test matrix
        double[][] testData = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        
        // Create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        
        // Get P matrix
        RealMatrix P = transformer.getP();
        
        // Verify P matrix values
        double[][] expectedData = {
            {-0.231970687246286, -0.785830238742807, 0.408248290463866},
            {-0.525322093301234, -0.086751339184962, -0.816496580927726},
            {-0.818673499356182, 0.612327560372883, 0.408248290463865}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertEquals(expected, P);
    }
    
    @Test
    public void testGetPT() {
        // Create test matrix
        double[][] testData = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        
        // Create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        
        // Get PT matrix
        RealMatrix PT = transformer.getPT();
        
        // Verify PT matrix values
        double[][] expectedData = {
            {-0.231970687246286, -0.525322093301234, -0.818673499356182},
            {-0.785830238742807, -0.086751339184962, 0.612327560372883},
            {0.408248290463866, -0.816496580927726, 0.408248290463865}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertEquals(expected, PT);
    }
    
    @Test
    public void testGetT() {
        // Create test matrix
        double[][] testData = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        
        // Create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        
        // Get T matrix
        RealMatrix T = transformer.getT();
        
        // Verify T matrix values
        double[][] expectedData = {
            {-0.231970687246286, -7.12094840549715, 0.408248290463866},
            {0,  0.794091966120907, -0.622800566540951},
            {0, 0, 14.3378577193737}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertEquals(expected, T);
    }
    
    // Add additional test methods here
    
}