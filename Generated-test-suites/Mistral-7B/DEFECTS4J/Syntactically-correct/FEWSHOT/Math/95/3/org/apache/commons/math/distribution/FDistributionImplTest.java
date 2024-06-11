package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;

import java.io.Serializable;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        double x = 1.5;
        double probability = distribution.cumulativeProbability(x);
        double expectedProbability = Beta.regularizedBeta((3.0 * x) / (4.0 + 3.0 * x), 1.5, 1.5);
        assertEquals(expectedProbability, probability, 1e-10);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        double p = 0.95;
        double x = distribution.inverseCumulativeProbability(p);
        double expectedX = Beta.incompleteBeta(0.95, 3.0, 4.0) / 3.0;
        assertEquals(expectedX, x, 1e-10);
    }

    @Test
    public void testInverseCumulativeProbabilityWithInvalidProbability() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(-1.0));
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(1.2));
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDegreesOfFreedomWithInvalidValue() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        assertFalse(distribution.getClass().isInstance(new FDistributionImpl(-3.0, 4.0)));
        assertFalse(distribution.getClass().isInstance(new FDistributionImpl(3.0, -4.0)));
    }

    @Test
    public void testCumulativeProbabilityWithInvalidX() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(-1.0));
    }
}