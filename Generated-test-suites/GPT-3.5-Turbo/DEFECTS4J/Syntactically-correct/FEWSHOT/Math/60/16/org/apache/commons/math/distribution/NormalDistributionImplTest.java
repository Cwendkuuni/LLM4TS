package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private NormalDistributionImpl normalDist;

    @Before
    public void setUp() {
        normalDist = new NormalDistributionImpl(0, 1);
    }

    @After
    public void tearDown() {
        normalDist = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(normalDist);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDist.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDist.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDist.density(0), 0);
        assertEquals(0.24197072451914337, normalDist.density(-1), 0);
        assertEquals(0.05399096651318806, normalDist.density(-2), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDist.cumulativeProbability(0), 0);
        assertEquals(0.8413447460685429, normalDist.cumulativeProbability(1), 0);
        assertEquals(0.9772498680518208, normalDist.cumulativeProbability(2), 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(-0.6744897501960817, normalDist.inverseCumulativeProbability(0.25), 0);
        assertEquals(0, normalDist.inverseCumulativeProbability(0.5), 0);
        assertEquals(0.6744897501960817, normalDist.inverseCumulativeProbability(0.75), 0);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDist.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY);
        assertTrue(sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, normalDist.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDist.getDomainLowerBound(0.25), 0);
        assertEquals(-Double.MAX_VALUE, normalDist.getDomainLowerBound(0.5), 0);
        assertEquals(0, normalDist.getDomainLowerBound(0.75), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0, normalDist.getDomainUpperBound(0.25), 0);
        assertEquals(Double.MAX_VALUE, normalDist.getDomainUpperBound(0.5), 0);
        assertEquals(Double.MAX_VALUE, normalDist.getDomainUpperBound(0.75), 0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDist.getInitialDomain(0.25), 0);
        assertEquals(0, normalDist.getInitialDomain(0.5), 0);
        assertEquals(1, normalDist.getInitialDomain(0.75), 0);
    }
}