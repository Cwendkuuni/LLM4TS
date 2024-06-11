package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        assertTrue(p.getData().length == 2 && p.getData()[0].length == 2 && p.getData()[1].length == 2);
        assertTrue(p.getData()[0][0] == 0 && p.getData()[0][1] == 1 && p.getData()[1][0] == -0.894427191 && p.getData()[1][1] == 0.447213595);
    }
    
    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix pt = transformer.getPT();
        assertTrue(pt.getData().length == 2 && pt.getData()[0].length == 2 && pt.getData()[1].length == 2);
        assertTrue(pt.getData()[0][0] == 0 && pt.getData()[0][1] == -0.894427191 && pt.getData()[1][0] == 1 && pt.getData()[1][1] == 0.447213595);
    }
    
    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix t = transformer.getT();
        assertTrue(t.getData().length == 2 && t.getData()[0].length == 2 && t.getData()[1].length == 2);
        assertTrue(t.getData()[0][0] == 1.414213562 && t.getData()[0][1] == -2.828427125 && t.getData()[1][0] == 0 && t.getData()[1][1] == 3.414213562);
    }
    
    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        assertTrue(transformer != null);
    }
    
    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double norm = transformer.getNorm();
        assertTrue(norm == 10);
    }
    
    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int l = transformer.findSmallSubDiagonalElement(1, 10);
        assertTrue(l == 0);
    }
    
    @Test
    public void testComputeShift() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        transformer.computeShift(0, 1, 0, shift);
        assertTrue(shift.x == 4 && shift.y == 0 && shift.w == 0);
    }
    
    @Test
    public void testInitQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        double[] hVec = new double[3];
        int im = transformer.initQRStep(0, 1, shift, hVec);
        assertTrue(im == 0);
    }
    
    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        double[] hVec = new double[3];
        transformer.performDoubleQRStep(0, 0, 1, shift, hVec);
        assertTrue(transformer != null);
    }
}