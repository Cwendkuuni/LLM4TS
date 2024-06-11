package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplCreation() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        assertNotNull(fDist);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);

        assertEquals(0.0, fDist.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(0.5, fDist.cumulativeProbability(0.5), 0.001);
        assertEquals(1.0, fDist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);

        assertEquals(0.0, fDist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(1.4161268, fDist.inverseCumulativeProbability(0.95), 0.001);
        assertThrows(MathException.class, () -> fDist.inverseCumulativeProbability(-0.1));
        assertThrows(MathException.class, () -> fDist.inverseCumulativeProbability(1.1));
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        fDist.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        fDist.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(4.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }
}