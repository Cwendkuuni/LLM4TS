package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] expected = {1.0, 2.0};
        assertArrayEquals(expected, distribution.getMeans(), 0.00001);
    }

    @Test
    public void testGetCovariances() {
        double[][] expected = {{1.0, 0.5}, {0.5, 1.0}};
        RealMatrix cov = distribution.getCovariances();
        assertArrayEquals(expected, cov.getData(), 0.00001);
    }

    @Test
    public void testDensity() {
        double[] vals = {1.0, 2.0};
        assertEquals(0.15915, distribution.density(vals), 0.00001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1.0, 1.0};
        assertArrayEquals(expected, distribution.getStandardDeviations(), 0.00001);
    }

    @Test
    public void testSample() {
        double[] vals = distribution.sample();
        assertNotNull(vals);
        assertEquals(2, vals.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {1.0, 2.0};
        assertEquals(0.99473, distribution.getExponentTerm(values), 0.00001);
    }
}