
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private static final double EPSILON = 1e-10;

    @Test
    public void testGetMeans() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        assertArrayEquals(means, distribution.getMeans(), EPSILON);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        assertArrayEquals(covariances, distribution.getCovariances().getData(), EPSILON);
    }

    @Test
    public void testDensity() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] vals = {1, 2, 3};
        double density = distribution.density(vals);

        assertEquals(0.06349363593424097, density, EPSILON);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] std = {1, 1, 1};
        assertArrayEquals(std, distribution.getStandardDeviations(), EPSILON);
    }

    @Test
    public void testSample() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] sample = distribution.sample();

        assertEquals(3, sample.length);
    }
}
