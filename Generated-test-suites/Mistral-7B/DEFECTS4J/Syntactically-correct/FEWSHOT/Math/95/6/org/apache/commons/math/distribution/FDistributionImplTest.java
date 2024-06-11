package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplCreation() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);
        assertNotNull(fDist);
        assertTrue(fDist instanceof FDistributionImpl);
        assertTrue(fDist instanceof FDistribution);
        assertTrue(fDist instanceof Serializable);
        assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionImplCumulativeProbability() throws MathException {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);

        assertEquals(0.0, fDist.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(0.5, fDist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.95, fDist.cumulativeProbability(1.5), 0.001);
        assertEquals(1.0, fDist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);

        // Test with negative degrees of freedom
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(-1.0, 4.0));
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(2.0, -1.0));
    }

    @Test
    public void testFDistributionImplInverseCumulativeProbability() throws MathException {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);

        assertEquals(0.0, fDist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(1.3347234618379435, fDist.inverseCumulativeProbability(0.95), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, fDist.inverseCumulativeProbability(1.0), 0.0);

        // Test with negative probability
        assertThrows(IllegalArgumentException.class, () -> fDist.inverseCumulativeProbability(-0.1));
        assertThrows(IllegalArgumentException.class, () -> fDist.inverseCumulativeProbability(1.1));
    }

    @Test
    public void testFDistributionImplSetAndGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);
        double degreesOfFreedom = 5.0;

        fDist.setNumeratorDegreesOfFreedom(degreesOfFreedom);
        assertEquals(degreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionImplSetAndGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);
        double degreesOfFreedom = 6.0;

        fDist.setDenominatorDegreesOfFreedom(degreesOfFreedom);
        assertEquals(degreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionImplNegativeDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(2.0, 4.0);

        assertFalse(fDist.isNumeratorDegreesOfFreedomNegative());
        assertFalse(fDist.isDenominatorDegreesOfFreedomNegative());

        fDist.setNumeratorDegreesOfFreedom(-1.0);
        assertTrue(fDist.isNumeratorDegreesOfFreedomNegative());

        fDist.setDenominatorDegreesOfFreedom(-1.0);
        assertTrue(fDist.isDenominatorDegreesOfFreedomNegative());
    }
}