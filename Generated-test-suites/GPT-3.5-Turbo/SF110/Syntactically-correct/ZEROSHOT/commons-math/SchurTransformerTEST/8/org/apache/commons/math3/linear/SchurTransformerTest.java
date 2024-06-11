package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SchurTransformerTest {
    
    private SchurTransformer schurTransformer;
    
    @Before
    public void setUp() {
        double[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        schurTransformer = new SchurTransformer(realMatrix);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] matrix = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        new SchurTransformer(realMatrix);
    }
    
    @Test
    public void testGetP() {
        double[][] expectedMatrix = {{-0.301511344578586, -0.904534033733291, 0.301511344578586},
                                     {-0.904534033733291, 0.301511344578587, 0.301511344578586},
                                     {0.301511344578586, -0.301511344578586, 0.904534033733291}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedMatrix);
        Assert.assertEquals(expected, schurTransformer.getP());
    }
    
    @Test
    public void testGetPT() {
        double[][] expectedMatrix = {{-0.301511344578586, -0.904534033733291, 0.301511344578586},
                                     {-0.904534033733291, 0.301511344578587, -0.301511344578586},
                                     {0.301511344578586, 0.301511344578586, 0.904534033733291}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedMatrix);
        Assert.assertEquals(expected, schurTransformer.getPT());
    }
    
    @Test
    public void testGetT() {
        double[][] expectedMatrix = {{1.561552812808831, -0.536414228008557, 0.0},
                                     {-2.046594297542656, 0.278081700724076, -0.072349705512961},
                                     {0.0, 0.0, 0.159961813762091}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedMatrix);
        Assert.assertEquals(expected, schurTransformer.getT());
    }
    
    @Test
    public void testGetNorm() {
        double expected = 61;
        Assert.assertEquals(expected, schurTransformer.getNorm(), 0);
    }
    
    @Test
    public void testFindSmallSubDiagonalElement() {
        int expected = 1;
        Assert.assertEquals(expected, schurTransformer.findSmallSubDiagonalElement(2, 61));
    }
    
    @Test
    public void testComputeShift() {
        SchurTransformer.ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        shiftInfo.exShift = 0;
        
        schurTransformer.computeShift(0, 2, 10, shiftInfo);
        
        double expectedX = 0.159961813762091;
        double expectedY = 0;
        double expectedW = 0;
        Assert.assertEquals(expectedX, shiftInfo.x, 0);
        Assert.assertEquals(expectedY, shiftInfo.y, 0);
        Assert.assertEquals(expectedW, shiftInfo.w, 0);
    }
    
    @Test
    public void testInitQRStep() {
        double[] hVec = new double[3];
        
        int expected = 1;
        Assert.assertEquals(expected, schurTransformer.initQRStep(0, 2, new SchurTransformer.ShiftInfo(), hVec));
    }
    
    @Test
    public void testPerformDoubleQRStep() {
        double[] hVec = new double[3];
        SchurTransformer.ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        shiftInfo.x = 0.159961813762091;
        shiftInfo.y = 0;
        shiftInfo.w = 0;
        
        schurTransformer.performDoubleQRStep(0, 1, 2, shiftInfo, hVec);
        
        double[][] expectedMatrix = {{1.561552812808831, -0.536414228008557, 0},
                                     {-2.046594297542656, 0.278081700724076, 0},
                                     {0, 0, 0}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedMatrix);
        Assert.assertEquals(expected, schurTransformer.getT());
    }
}