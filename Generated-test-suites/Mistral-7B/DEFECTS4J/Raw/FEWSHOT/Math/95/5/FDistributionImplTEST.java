package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        dist.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        dist.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        dist.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        dist.setDenominatorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        assertEquals(0.0, dist.cumulativeProbability(0.0), 0.0);
        assertEquals(0.5, dist.cumulativeProbability(1.0), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalidX() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        dist.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(0.5, dist.inverseCumulativeProbability(0.5), 0.001);
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.0));
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.1));
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(-1.0));
        assertThrows(IllegalArgumentException.class, () -> dist.inverseCumulativeProbability(2.0));
    }
}