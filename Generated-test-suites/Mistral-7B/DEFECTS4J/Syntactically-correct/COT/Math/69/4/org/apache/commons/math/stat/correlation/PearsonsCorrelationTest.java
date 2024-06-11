package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        Assert.assertNull(correlation.correlationMatrix);
        Assert.assertEquals(0, correlation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataConstructor() {
        double[][] data = new double[][]{{1}, {2, 3, 4}};
        new PearsonsCorrelation(data);
    }

    @Test
    public void testDataConstructor() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8571428571, 0.8320985505},
                                                             {0.8571428571, 1.0, 0.8571428571},
                                                             {0.8320985505, 0.8571428571, 1.0}});
        RealMatrix actual = correlation.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(3, correlation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRealMatrixConstructor() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1, 2}, {3, 4}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8164966}, {0.8164966, 1.0}});
        RealMatrix actual = correlation.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(2, correlation.nObs);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testCovarianceConstructorWithNullCovariance() {
        new PearsonsCorrelation(null);
    }

    @Test
    public void testCovarianceConstructor() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1, 2}, {2, 3}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.6}, {0.6, 1.0}});
        RealMatrix actual = correlation.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(3, correlation.nObs);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.08256356, 0.06228214, 0.0416},
                                                             {0.06228214, 0.08256356, 0.0526},
                                                             {0.0416, 0.0526, 0.0343}});
        RealMatrix actual = correlation.getCorrelationStandardErrors();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.10, 0.27, 0.37},
                                                             {0.27, 0.10, 0.27},
                                                             {0.37, 0.27, 0.10}});
        RealMatrix actual = correlation.getCorrelationPValues();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = new double[]{1, 2, 3};
        double[] yArray = new double[]{4, 5, 6};
        double expected = 0.8164966;
        double actual = PearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataForCorrelation() {
        double[] xArray = new double[]{1};
        double[] yArray = new double[]{4, 5};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{});
        PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
    }
}