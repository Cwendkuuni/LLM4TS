package org.apache.commons.math3.linear;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        double[][] data = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(transformer.matrixP);
        assertEquals(expectedP, transformer.getP());
    }

    @Test
    public void testGetPT() {
        double[][] data = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPt = transformer.getP().transpose();
        assertEquals(expectedPt, transformer.getPT());
    }

    @Test
    public void testGetT() {
        double[][] data = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(transformer.matrixT);
        assertEquals(expectedT, transformer.getT());
    }

    @Test
    public void testTransform() {
        double[][] data = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(transformer.matrixT);
        assertEquals(expectedT, transformer.getT());
    }

    @Test
    public void testGetNorm() {
        double[][] data = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SchurTransformer transformer = new SchurTransformer(matrix);
        double expectedNorm = 8.0;
        assertEquals(expectedNorm, transformer.getNorm(), 0.0);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] data = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expectedElement = 1;
        double expectedNorm = 8.0;
        assertEquals(expectedElement, transformer.findSmallSubDiagonalElement(1, expectedNorm));
    }

    @Test
    public void testComputeShift() {
        double[][] data = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SchurTransformer transformer = new SchurTransformer(matrix);
        int l = 1;
        int idx = 1;
        int iteration = 1;
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        transformer.computeShift(l, idx, iteration, shift);
        
    }

    @Test
    public void testInitQRStep() {
        double[][] data = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expectedIm = 1;
        int expectedIdx = 1;
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        double[] hVec = new double[3];
        hVec[0] = 0.0;
        hVec[1] = 0.0;
        hVec[2] = 0.0;
        assertEquals(expectedIm, transformer.initQRStep(expectedIm, expectedIdx, shift, hVec));
    }

    @Test
    public void testPerformDoubleQRStep() {
        double[][] data = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        SchurTransformer transformer = new SchurTransformer(matrix);
        int il = 1;
        int im = 1;
        int iu = 1;
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        double[] hVec = new double[3];
        transformer.performDoubleQRStep(il, im, iu, shift, hVec);
    }
}