package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Test cases for FDistributionImpl class.
 */
public class FDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        
        // Test for x < 0
        double result = fDist.cumulativeProbability(-1.0);
        assertEquals(0.0, result, 0.0);
        
        // Test for x = 0
        result = fDist.cumulativeProbability(0.0);
        assertEquals(0.0, result, 0.0);
        
        // Test for x < mean
        result = fDist.cumulativeProbability(StatUtils.mean(2.0, 4.0) - 1.0);
        assertEquals(0.0, result, 0.01);
        
        // Test for x = mean
        result = fDist.cumulativeProbability(StatUtils.mean(2.0, 4.0));
        assertTrue(result > 0.0 && result < 0.5);
        
        // Test for x > mean
        result = fDist.cumulativeProbability(StatUtils.mean(2.0, 4.0) + 1.0);
        assertTrue(result > 0.5);
    }

    /**
     * Test inverseCumulativeProbability method.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        fDist.inverseCumulativeProbability(-1.0);
    }

    /**
     * Test inverseCumulativeProbability method with valid probabilities.
     */
    @Test
    public void testInverseCumulativeProbabilityWithValidProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        
        double p = 0.25;
        double x = fDist.inverseCumulativeProbability(p);
        
        double expectedX = Beta.regularizedBeta((2.0 * x) / (4.0 + 2.0 * x), 1.0, 2.0).getResult();
        assertEquals(expectedX, x, 0.01);
    }

    /**
     * Test getDomainLowerBound method.
     */
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        assertEquals(0.0, fDist.getDomainLowerBound(0.1), 0.0);
    }

    /**
     * Test getDomainUpperBound method.
     */
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getDomainUpperBound(0.1), 0.0);
    }

    /**
     * Test getInitialDomain method.
     */
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        double p = 0.1;
        double expectedDomain = Beta.regularizedBeta(4.0 / (4.0 - 2.0), 1.0, 2.0).getResult();
        assertEquals(expectedDomain, fDist.getInitialDomain(p), 0.01);
    }

    /**
     * Test setNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        fDist.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        fDist.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }
}