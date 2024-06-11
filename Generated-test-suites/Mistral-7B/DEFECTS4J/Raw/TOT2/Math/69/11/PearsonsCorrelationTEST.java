package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {

    private static final double EPSILON = 1e-12;

    private PearsonsCorrelation correlation;

    @Before
    public void setup() {
        double[][] data = {{1, 2, 3, 4}, {1, 2, 3, 4}, {1, 2, 3, 4}, {1, 2, 3, 4}};
        correlation = new PearsonsCorrelation(new Array2DRowRealMatrix(data));
    }

    @Test
    public void testCorrelationMatrix() {
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0.741, 0.741, 0.741},
                {0.741, 1, 0.741, 0.741},
                {0.741, 0.741, 1, 0.741},
                {0.741, 0.741, 0.741, 1}});
        Assert.assertArrayEquals(expected.getData(), correlationMatrix.getData(), EPSILON);
    }

    @Test
    public void testCorrelationStandardErrors() {
        RealMatrix standardErrors = correlation.getCorrelationStandardErrors();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.723, 0.723, 0.723, 0.723},
                {0.723, 0.723, 0.723, 0.723},
                {0.723, 0.723, 0.723, 0.723},
                {0.723, 0.723, 0.723, 0.723}});
        Assert.assertArrayEquals(expected.getData(), standardErrors.getData(), EPSILON);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        RealMatrix pValues = correlation.getCorrelationPValues();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.000, 0.000, 0.000, 0.000},
                {0.000, 0.000, 0.000, 0.000},
                {0.000, 0.000, 0.000, 0.000},
                {0.000, 0.000, 0.000, 0.000}});
        Assert.assertArrayEquals(expected.getData(), pValues.getData(), EPSILON);
    }

    @Test
    public void testCorrelation_Array() {
        double[] xArray = {1, 2, 3, 4};
        double[] yArray = {2, 3, 3, 4};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(0.8, correlation, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelation_InvalidArrays() {
        double[] xArray = {1, 2};
        double[] yArray = {3, 4, 5};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testComputeCorrelationMatrix_Array() {
        double[][] data = {{1, 2, 3, 4}, {1, 2, 3, 4}, {1, 2, 3, 4}, {1, 2, 3, 4}};
        RealMatrix correlationMatrix = new PearsonsCorrelation().computeCorrelationMatrix(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0.741, 0.741, 0.741},
                {0.741, 1, 0.741, 0.741},
                {0.741, 0.741, 1, 0.741},
                {0.741, 0.741, 0.741, 1}});
        Assert.assertArrayEquals(expected.getData(), correlationMatrix.getData(), EPSILON);
    }
}