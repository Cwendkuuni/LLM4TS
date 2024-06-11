package org.apache.commons.math3.distribution;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class MultivariateNormalDistributionTest {
    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] expectedMeans = {1.0, 2.0, 3.0};
        double[] actualMeans = distribution.getMeans();
        assertArrayEquals(expectedMeans, actualMeans, 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(covariances);
        RealMatrix actualCovariances = distribution.getCovariances();
        assertEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 2.0, 3.0};
        double expectedDensity = 0.06349363593424097;
        double actualDensity = distribution.density(values);
        assertEquals(expectedDensity, actualDensity, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] expectedStd = {1.0, 1.0, 1.0};
        double[] actualStd = distribution.getStandardDeviations();
        assertArrayEquals(expectedStd, actualStd, 0.0001);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        assertNotNull(sample);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 2.0, 3.0};
        double expectedTerm = 1.0;
        double actualTerm = distribution.getExponentTerm(values);
        assertEquals(expectedTerm, actualTerm, 0.0001);
    }
}