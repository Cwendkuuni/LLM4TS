package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SchurTransformerTest {
    
    @Test
    public void testConstructor() {
        double[][] inputMatrix = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        assertNotNull(transformer);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_NonSquareMatrixException() {
        double[][] inputMatrix = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
    
    @Test
    public void testGetP() {
        double[][] inputMatrix = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix pMatrix = transformer.getP();
        
        assertNotNull(pMatrix);
        assertEquals(matrix.getRowDimension(), pMatrix.getRowDimension());
        assertEquals(matrix.getColumnDimension(), pMatrix.getColumnDimension());
    }
    
    @Test
    public void testGetPT() {
        double[][] inputMatrix = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix ptMatrix = transformer.getPT();
        
        assertNotNull(ptMatrix);
        assertEquals(matrix.getRowDimension(), ptMatrix.getRowDimension());
        assertEquals(matrix.getColumnDimension(), ptMatrix.getColumnDimension());
        assertTrue(ptMatrix.equals(transformer.getP().transpose()));
    }
    
    @Test
    public void testGetT() {
        double[][] inputMatrix = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix tMatrix = transformer.getT();
        
        assertNotNull(tMatrix);
        assertEquals(matrix.getRowDimension(), tMatrix.getRowDimension());
        assertEquals(matrix.getColumnDimension(), tMatrix.getColumnDimension());
    }
    
}