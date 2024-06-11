package org.apache.commons.math.distribution;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_validConstruction() {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        assertTrue(distribution instanceof FDistribution);
        assertTrue(distribution instanceof Serializable);
        assertTrue(distribution instanceof AbstractContinuousDistribution);
    }

    @Test
    public void testFDistributionImpl_negativeDegreesOfFreedom() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FDistributionImpl(-5.0, 7.0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new FDistributionImpl(5.0, -7.0);
        });
    }

    @Test
    public void testCumulativeProbability_validValues() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cdf = distribution.cumulativeProbability(x);
            assertTrue(cdf >= 0.0 && cdf <= 1.0);
        }
    }

    @Test
    public void testCumulativeProbability_invalidValue() {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(-1.0));
    }

    @Test
    public void testInverseCumulativeProbability_validValues() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = distribution.inverseCumulativeProbability(p);
            assertTrue(x > 0.0);
        }
    }

    @Test
    public void testInverseCumulativeProbability_invalidValue() {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(-1.0));
        assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(1.1));
    }

    @Test
    public void testSetGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        distribution.setNumeratorDegreesOfFreedom(10.0);
        assertEquals(10.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        distribution.setDenominatorDegreesOfFreedom(11.0);
        assertEquals(11.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}