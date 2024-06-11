package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.MathException;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelationMatrix() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix correlationMatrix = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationMatrix();
        double[] expected = {0.8165, 0.8165, 1.0};
        assertArrayEquals(expected, correlationMatrix.getData(0), 0.00001);
        assertArrayEquals(expected, correlationMatrix.getData(0).clone(), 0.00001);
        assertArrayEquals(expected, correlationMatrix.getData(1), 0.00001);
        assertArrayEquals(expected, correlationMatrix.getData(1).clone(), 0.00001);
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, correlationMatrix.getData(2), 0.00001);
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, correlationMatrix.getData(2).clone(), 0.00001);
    }

    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix correlationMatrix = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationMatrix();
        RealMatrix correlationStandardErrors = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationStandardErrors();

        double r11 = correlationMatrix.getEntry(0, 0);
        double r12 = correlationMatrix.getEntry(0, 1);
        double r21 = correlationMatrix.getEntry(1, 0);
        double r22 = correlationMatrix.getEntry(1, 1);
        double r31 = correlationMatrix.getEntry(2, 0);
        double r32 = correlationMatrix.getEntry(2, 1);

        assertEquals(0.4161, correlationStandardErrors.getEntry(0, 0), 0.0001);
        assertEquals(0.4161, correlationStandardErrors.getEntry(1, 0), 0.0001);
        assertEquals(0.4161, correlationStandardErrors.getEntry(0, 1), 0.0001);
        assertEquals(0.4161, correlationStandardErrors.getEntry(1, 1), 0.0001);
        assertEquals(0.0, correlationStandardErrors.getEntry(0, 2), 0.0);
        assertEquals(0.0, correlationStandardErrors.getEntry(1, 2), 0.0);
        assertEquals(0.0, correlationStandardErrors.getEntry(2, 0), 0.0);
        assertEquals(0.0, correlationStandardErrors.getEntry(2, 1), 0.0);
        assertEquals(1.0, correlationStandardErrors.getEntry(2, 2), 0.0);

        TDistribution tDistribution = new TDistributionImpl(data.length * 2 - 2);
        assertEquals(1.0 / Math.sqrt(2.0), correlationStandardErrors.getEntry(0, 1), 0.0001);
        assertEquals(1.0 / Math.sqrt(2.0), correlationStandardErrors.getEntry(1, 0), 0.0001);
        assertEquals(1.0 / Math.sqrt(2.0 * 2.0 / 3.0), correlationStandardErrors.getEntry(0, 2), 0.0001);
        assertEquals(1.0 / Math.sqrt(2.0 * 2.0 / 3.0), correlationStandardErrors.getEntry(1, 2), 0.0001);
        assertEquals(1.0 / Math.sqrt(2.0 * 2.0 / 3.0), correlationStandardErrors.getEntry(2, 0), 0.0001);
        assertEquals(1.0 / Math.sqrt(2.0 * 2.0 / 3.0), correlationStandardErrors.getEntry(2, 1), 0.0001);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix correlationMatrix = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationMatrix();
        RealMatrix correlationPValues = new PearsonsCorrelation(new BlockRealMatrix(data)).getCorrelationPValues();

        double r11 = correlationMatrix.getEntry(0, 0);
        double r12 = correlationMatrix.getEntry(0, 1);
        double r21 = correlationMatrix.getEntry(1, 0);
        double r22 = correlationMatrix.getEntry(1, 1);
        double r31 = correlationMatrix.getEntry(2, 0);
        double r32 = correlationMatrix.getEntry(2, 1);

        double pValue11 = correlationPValues.getEntry(0, 0);
        double pValue12 = correlationPValues.getEntry(0, 1);
        double pValue21 = correlationPValues.getEntry(1, 0);
        double pValue22 = correlationPValues.getEntry(1, 1);
        double pValue31 = correlationPValues.getEntry(2, 0);
        double pValue32 = correlationPValues.getEntry(2, 1);

        assertTrue(pValue11 > 0.0 && pValue11 < 1.0, "pValue11 should be between 0 and 1: " + pValue11);
        assertTrue(pValue12 > 0.0 && pValue12 < 1.0, "pValue12 should be between 0 and 1: " + pValue12);
        assertTrue(pValue21 > 0.0 && pValue21 < 1.0, "pValue21 should be between 0 and 1: " + pValue21);
        assertTrue(pValue22 > 0.0 && pValue22 < 1.0, "pValue22 should be between 0 and 1: " + pValue22);
        assertTrue(pValue31 > 0.0 && pValue31 < 1.0, "pValue31 should be between 0 and 1: " + pValue31);
        assertTrue(pValue32 > 0.0 && pValue32 < 1.0, "pValue32 should be between 0 and 1: " + pValue32);

        assertEquals(2.0 * (1.0 - r11 * r11) / (data.length * 2.0 - 2.0), pValue11, 0.0001);
        assertEquals(2.0 * (1.0 - r12 * r12) / (data.length * 2.0 - 2.0), pValue12, 0.0001);
        assertEquals(2.0 * (1.0 - r21 * r21) / (data.length * 2.0 - 2.0), pValue21, 0.0001);
        assertEquals(2.0 * (1.0 - r22 * r22) / (data.length * 2.0 - 2.0), pValue22, 0.0001);
        assertEquals(2.0 * (1.0 - r31 * r31) / (data.length * 2.0 - 2.0), pValue31, 0.0001);
        assertEquals(2.0 * (1.0 - r32 * r32) / (data.length * 2.0 - 2.0), pValue32, 0.0001);
    }

    @Test
    public void testEmptyMatrix() {
        RealMatrix correlationMatrix = new PearsonsCorrelation().getCorrelationMatrix();
        assertNull(correlationMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyData() {
        new PearsonsCorrelation(new double[0][0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidData() {
        new PearsonsCorrelation(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0},
            {7.0, 8.0}
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidData2() {
        new PearsonsCorrelation(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 7.0},
            {7.0, 8.0}
        });
    }
}