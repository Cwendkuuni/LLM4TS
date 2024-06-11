package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {
    
    @Test
    public void testGetP() {
        double[][] testData1 = {{1, 2}, {3, 4}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(testData1);
        SchurTransformer transformer1 = new SchurTransformer(matrix1);
        RealMatrix expectedP1 = MatrixUtils.createRealMatrix(testData1);
        RealMatrix actualP1 = transformer1.getP();
        assertEquals(expectedP1, actualP1);
        
        double[][] testData2 = {{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(testData2);
        SchurTransformer transformer2 = new SchurTransformer(matrix2);
        RealMatrix expectedP2 = MatrixUtils.createRealMatrix(testData2);
        RealMatrix actualP2 = transformer2.getP();
        assertEquals(expectedP2, actualP2);
    }
    
    @Test
    public void testGetPT() {
        double[][] testData1 = {{1, 2}, {3, 4}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(testData1);
        SchurTransformer transformer1 = new SchurTransformer(matrix1);
        RealMatrix expectedPT1 = matrix1.transpose();
        RealMatrix actualPT1 = transformer1.getPT();
        assertEquals(expectedPT1, actualPT1);
        
        double[][] testData2 = {{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(testData2);
        SchurTransformer transformer2 = new SchurTransformer(matrix2);
        RealMatrix expectedPT2 = matrix2.transpose();
        RealMatrix actualPT2 = transformer2.getPT();
        assertEquals(expectedPT2, actualPT2);
    }
    
    @Test
    public void testGetT() {
        double[][] testData1 = {{1, 2}, {3, 4}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(testData1);
        SchurTransformer transformer1 = new SchurTransformer(matrix1);
        RealMatrix expectedT1 = transformer1.getT();
        RealMatrix actualT1 = matrix1;
        assertEquals(expectedT1, actualT1);
        
        double[][] testData2 = {{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(testData2);
        SchurTransformer transformer2 = new SchurTransformer(matrix2);
        RealMatrix expectedT2 = transformer2.getT();
        RealMatrix actualT2 = matrix2;
        assertEquals(expectedT2, actualT2);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.getP();
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        double[][] testData = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.getT();
    }
}