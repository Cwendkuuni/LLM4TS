package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelationMatrix() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8, 0.7}, {0.8, 1.0, 0.8}, {0.7, 0.8, 1.0}});
        Assert.assertArrayEquals("Error in correlation matrix", expected.getData(), correlationMatrix.getData(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testInsufficientData() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidData() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, "not a number"}};
        try {
            new PearsonsCorrelation(data);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Expected IllegalArgumentException with specific message",
                    "invalid data: array contains non-numeric values", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[] xArray = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yArray = {2.0, 3.5, 3.5, 4.5, 5.5};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        pearsonCorrelation.correlation(xArray, yArray);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        double correlation = pearsonCorrelation.correlation(xArray, yArray);
        double pValue = correlationPValues.getEntry(0, 1);
        Assert.assertEquals("Error in p-value calculation for correlation coefficient: x[0]-x[avg] and y[0]-y[avg]", 0.05, pValue, 0.01);
    }
}