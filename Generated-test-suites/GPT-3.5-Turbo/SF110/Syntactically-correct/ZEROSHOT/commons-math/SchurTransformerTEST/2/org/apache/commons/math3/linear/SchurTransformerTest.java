package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{transformer.matrixP[0][0], transformer.matrixP[0][1]}, {transformer.matrixP[1][0], transformer.matrixP[1][1]}});
        RealMatrix actual = transformer.getP();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix expected = transformer.getP().transpose();
        RealMatrix actual = transformer.getPT();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{transformer.matrixT[0][0], transformer.matrixT[0][1]}, {transformer.matrixT[1][0], transformer.matrixT[1][1]}});
        RealMatrix actual = transformer.getT();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTransform() {
        // TODO: Implement testTransform
    }
    
    @Test
    public void testGetNorm() {
        // TODO: Implement testGetNorm
    }
    
    @Test
    public void testFindSmallSubDiagonalElement() {
        // TODO: Implement testFindSmallSubDiagonalElement
    }
    
    @Test
    public void testComputeShift() {
        // TODO: Implement testComputeShift
    }
    
    @Test
    public void testInitQRStep() {
        // TODO: Implement testInitQRStep
    }
    
    @Test
    public void testPerformDoubleQRStep() {
        // TODO: Implement testPerformDoubleQRStep
    }
}