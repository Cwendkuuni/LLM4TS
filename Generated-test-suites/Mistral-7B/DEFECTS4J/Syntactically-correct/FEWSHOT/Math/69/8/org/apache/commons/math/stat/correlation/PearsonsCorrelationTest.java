package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testCreateWithoutData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertTrue(pearsonsCorrelation.getCorrelationMatrix() == null);
        assertEquals(0, pearsonsCorrelation.nObs);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCreateWithInvalidData() {
        double[][] data = new double[1][1];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testCreateWithRectangularArray() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0}
        };
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertArrayEquals(new double[][]{
            {1.0, 0.8, 0.6},
            {0.8, 1.0, 0.8},
            {0.6, 0.8, 1.0}}, pearsonsCorrelation.getCorrelationMatrix().getData(), 0.001);
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test
    public void testCreateWithRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0}
        });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        assertArrayEquals(new double[][]{
            {1.0, 0.8, 0.6},
            {0.8, 1.0, 0.8},
            {0.6, 0.8, 1.0}}, pearsonsCorrelation.getCorrelationMatrix().getData(), 0.001);
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test
    public void testCreateFromCovariance() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0}
        };
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        assertArrayEquals(new double[][]{
            {1.0, 0.8, 0.6},
            {0.8, 1.0, 0.8},
            {0.6, 0.8, 1.0}}, pearsonsCorrelation.getCorrelationMatrix().getData(), 0.001);
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0}
        };
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertArrayEquals(new double[][]{
            {1.0, 0.8, 0.6},
            {0.8, 1.0, 0.8},
            {0.6, 0.8, 1.0}}, pearsonsCorrelation.getCorrelationMatrix().getData(), 0.001);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0}
        };
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix standardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertArrayEquals(new double[][]{
            {0.4472136, 0.4472136, 0.4472136},
            {0.4472136, 0.4472136, 0.4472136},
            {0.4472136, 0.4472136, 0.4472136}}, standardErrors.getData(), 0.001);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0}
        };
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix pValues = pearsonsCorrelation.getCorrelationPValues();
        assertArrayEquals(new double[][]{
            {1.0, 0.9531514, 0.9760668},
            {0.9531514, 1.0, 0.9649060},
            {0.9760668, 0.9649060, 1.0}}, pValues.getData(), 0.001);
    }

    @Test(expected = MathException.class)
    public void testGetCorrelationPValuesWithInvalidData() throws MathException {
        double[][] data = new double[1][1];
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        pearsonsCorrelation.getCorrelationPValues();
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 3.0, 4.0};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidData1() {
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidData2() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {2.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidData3() {
        double[] xArray = {};
        double[] yArray = {};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidData4() {
        double[] xArray = {};
        double[] yArray = {1.0, 2.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidData5() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 2.0},
            {2.0, 3.0}
        });
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertArrayEquals(new double[][]{
            {1.0, 0.8},
            {0.8, 1.0}}, correlationMatrix.getData(), 0.001);
    }
}