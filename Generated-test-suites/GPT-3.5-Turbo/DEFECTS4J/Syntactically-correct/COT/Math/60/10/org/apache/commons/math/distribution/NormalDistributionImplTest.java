package org.apache.commons.math.distribution;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
        assertEquals(0.24197072451914337, normalDistribution.density(-1), DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
        assertEquals(0.24197072451914337, normalDistribution.density(1), DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0, normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0);
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
        assertEquals(1, normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0);
        assertEquals(0.022750131948179195, normalDistribution.cumulativeProbability(-2), DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
        assertEquals(0.9772498680518208, normalDistribution.cumulativeProbability(2), DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
        assertEquals(0, normalDistribution.inverseCumulativeProbability(0.5), 0);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0);
        assertEquals(-2.3263478740408408, normalDistribution.inverseCumulativeProbability(0.01), DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
        assertEquals(2.3263478740408408, normalDistribution.inverseCumulativeProbability(0.99), DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(DEFAULT_INVERSE_ABSOLUTE_ACCURACY, normalDistribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.4), 0);
        assertEquals(0, normalDistribution.getDomainLowerBound(0.6), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0, normalDistribution.getDomainUpperBound(0.4), 0);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.6), 0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistribution.getInitialDomain(0.4), 0);
        assertEquals(1, normalDistribution.getInitialDomain(0.6), 0);
    }
}