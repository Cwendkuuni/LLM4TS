package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.Beta;
import org.apache.commons.math.MathException;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_validInitialization() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 6.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionImpl_nullInitialization() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(null, 6.0));
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(5.0, null));
    }

    @Test
    public void testFDistributionImpl_invalidInitialization() {
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(-5.0, 6.0));
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(5.0, -6.0));
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(0.0, 6.0));
        assertThrows(IllegalArgumentException.class, () -> new FDistributionImpl(5.0, 0.0));
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 6.0);
        assertEquals(0.0, distribution.cumulativeProbability(0.0), 0.0);
        assertEquals(0.5, distribution.cumulativeProbability(1.0), 0.01);
        assertEquals(1.0, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 6.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(1.45, distribution.inverseCumulativeProbability(0.95), 0.01);
    }

    @Test
    public void testInverseCumulativeProbability_edgeCases() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 6.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_invalidProbability()
            throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 6.0);
        distribution.inverseCumulativeProbability(-0.1);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 6.0);
        distribution.setNumeratorDegreesOfFreedom(10.0);
        assertEquals(10.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_negative() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 6.0);
        distribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 6.0);
        distribution.setDenominatorDegreesOfFreedom(10.0);
        assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_negative() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 6.0);
        distribution.setDenominatorDegreesOfFreedom(-1.0);
    }
}