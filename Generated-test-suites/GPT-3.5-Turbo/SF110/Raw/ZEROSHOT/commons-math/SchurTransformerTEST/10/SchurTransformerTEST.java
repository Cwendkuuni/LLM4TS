package org.apache.commons.math3.linear;

import org.junit.*;
import static org.junit.Assert.*;

public class SchurTransformerTest {
  
  // Test getP() method
  @Test
  public void testGetP() {
    // Create test matrix
    double[][] testData = {{1, 2}, {3, 4}}; 
    RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
    
    // Create SchurTransformer object
    SchurTransformer transformer = new SchurTransformer(testMatrix);
    
    // Get P matrix
    RealMatrix pMatrix = transformer.getP();
    
    // Compare P matrix with expected result
    double[][] expectedData = {{-0.87174, -0.48990}, {-0.48990, 0.87174}};
    RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
    assertEquals(expectedMatrix, pMatrix);
  }
  
  // Test getPT() method
  @Test
  public void testGetPT() {
    // Create test matrix
    double[][] testData = {{1, 2}, {3, 4}}; 
    RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
    
    // Create SchurTransformer object
    SchurTransformer transformer = new SchurTransformer(testMatrix);
    
    // Get PT matrix
    RealMatrix ptMatrix = transformer.getPT();
    
    // Compare PT matrix with expected result
    double[][] expectedData = {{-0.87174, -0.48990}, {-0.48990, 0.87174}};
    RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData).transpose();
    assertEquals(expectedMatrix, ptMatrix);
  }
  
  // Test getT() method
  @Test
  public void testGetT() {
    // Create test matrix
    double[][] testData = {{1, 2}, {3, 4}}; 
    RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
    
    // Create SchurTransformer object
    SchurTransformer transformer = new SchurTransformer(testMatrix);
    
    // Get T matrix
    RealMatrix tMatrix = transformer.getT();
    
    // Compare T matrix with expected result
    double[][] expectedData = {{5.37228, 0.87174}, {-0.48990, -2.37228}};
    RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
    assertEquals(expectedMatrix, tMatrix);
  }
}