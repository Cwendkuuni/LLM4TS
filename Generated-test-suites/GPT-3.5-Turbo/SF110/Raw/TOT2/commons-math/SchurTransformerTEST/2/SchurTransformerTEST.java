package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        assertNotNull(transformer.getP());
        assertNotNull(transformer.getPT());
        assertNotNull(transformer.getT());
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        assertEquals(transformer.getNorm(), 10.0, 0.0);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {0, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        assertEquals(transformer.findSmallSubDiagonalElement(1, 10.0), 1);
    }

    @Test
    public void testComputeShift() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {0, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        
        transformer.computeShift(1, 1, 1, shift);
        
        assertEquals(shift.x, 4.0, 0.0);
        assertEquals(shift.y, 0.0, 0.0);
        assertEquals(shift.w, 0.0, 0.0);
        assertEquals(shift.exShift, 0.0, 0.0);
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {0, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        
        int result = transformer.initQRStep(0, 1, new SchurTransformer.ShiftInfo(), hVec);
        
        assertEquals(result, 0);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 0}, {0, 4, 0}, {0, 0, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        
        transformer.performDoubleQRStep(0, 0, 1, shift, hVec);
        
        assertEquals(matrix.getEntry(0, 0), 4.0, 0.0);
        assertEquals(matrix.getEntry(1, 1), 5.0, 0.0);
        assertEquals(matrix.getEntry(2, 2), 3.0, 0.0);
    }

}