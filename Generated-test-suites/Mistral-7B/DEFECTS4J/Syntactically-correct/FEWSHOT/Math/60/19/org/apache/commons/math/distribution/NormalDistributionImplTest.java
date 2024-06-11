package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {
    @Test
    public void testNormalDistributionImplCreation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0, normalDist.getMean(), 0.001);
        assertEquals(1, normalDist.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(1, -1);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        double density;

        // Test normal distribution
        density = normalDist.density(0);
        assertEquals(0.398942280401433, density, 0.0001);

        density = normalDist.density(1);
        assertEquals(0.398942280401433, density, 0.0001);

        density = normalDist.density(2);
        assertEquals(0.228118215513298, density, 0.0001);

        density = normalDist.density(-2);
        assertEquals(0.228118215513298, density, 0.0001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        double cumulativeProbability;

        // Test values near mean
        cumulativeProbability = normalDist.cumulativeProbability(0.1);
        assertEquals(0.154430669873442, cumulativeProbability, 0.0001);

        cumulativeProbability = normalDist.cumulativeProbability(0.5);
        assertEquals(0.5, cumulativeProbability, 0.0001);

        cumulativeProbability = normalDist.cumulativeProbability(0.85);
        assertEquals(0.841344346626923, cumulativeProbability, 0.0001);

        // Test extreme values
        cumulativeProbability = normalDist.cumulativeProbability(-5);
        assertEquals(0, cumulativeProbability, 0.0001);

        cumulativeProbability = normalDist.cumulativeProbability(5);
        assertEquals(1, cumulativeProbability, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        normalDist.cumulativeProbability(-1);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException, OutOfRangeException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability;

        inverseCumulativeProbability = normalDist.inverseCumulativeProbability(0.154430669873442);
        assertEquals(-0.975325425882736, inverseCumulativeProbability, 0.0001);

        inverseCumulativeProbability = normalDist.inverseCumulativeProbability(0.5);
        assertEquals(0, inverseCumulativeProbability, 0.0001);

        inverseCumulativeProbability = normalDist.inverseCumulativeProbability(0.841344346626923);
        assertEquals(0.841344346626923, inverseCumulativeProbability, 0.0001);

        inverseCumulativeProbability = normalDist.inverseCumulativeProbability(1);
        assertEquals(1.644934066848244, inverseCumulativeProbability, 0.0001);

        inverseCumulativeProbability = normalDist.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability);

        inverseCumulativeProbability = normalDist.inverseCumulativeProbability(1.0001);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability);

        inverseCumulativeProbability = normalDist.inverseCumulativeProbability(-1);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability);

        inverseCumulativeProbability = normalDist.inverseCumulativeProbability(-5);
        assertEquals(-5.85335632326047, inverseCumulativeProbability, 0.0001);

        inverseCumulativeProbability = normalDist.inverseCumulativeProbability(5);
        assertEquals(5.85335632326047, inverseCumulativeProbability, 0.0001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityOutOfRange() throws MathException, OutOfRangeException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        normalDist.inverseCumulativeProbability(-0.5);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        double sample;

        for (int i = 0; i < 10; i++) {
            sample = normalDist.sample();
            assertTrue(sample >= -3.5 && sample <= 3.5);
        }
    }

    @Test
    public void testNormalDistributionImplDomainBounds() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);

        double lowerBound, upperBound, initialValue;

        // Test p = 0
        lowerBound = normalDist.getDomainLowerBound(0);
        assertEquals(-Double.MAX_VALUE, lowerBound);
        upperBound = normalDist.getDomainUpperBound(0);
        assertEquals(Double.MAX_VALUE, upperBound);
        initialValue = normalDist.getInitialDomain(0);
        assertEquals(-1, initialValue);

        // Test p = 0.5
        lowerBound = normalDist.getDomainLowerBound(0.5);
        assertEquals(0, lowerBound);
        upperBound = normalDist.getDomainUpperBound(0.5);
        assertEquals(0, upperBound);
        initialValue = normalDist.getInitialDomain(0.5);
        assertEquals(0, initialValue);

        // Test p = 1
        lowerBound = normalDist.getDomainLowerBound(1);
        assertEquals(Double.MAX_VALUE, lowerBound);
        upperBound = normalDist.getDomainUpperBound(1);
        assertEquals(Double.MAX_VALUE, upperBound);
        initialValue = normalDist.getInitialDomain(1);
        assertEquals(1, initialValue);
    }
}