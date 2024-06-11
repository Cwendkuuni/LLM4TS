package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.distribution.FDistribution;
import org.apache.commons.math3.stat.distribution.FDistributionImpl;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link FDistributionImpl} class.
 */
public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_constructor() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability_validArgument() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double probability = distribution.cumulativeProbability(1.5);
        assertEquals(0.2408160169627283, probability, 0.00001);
    }

    @Test
    public void testCumulativeProbability_invalidArgument() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(-1.0));
    }

    @Test
    public void testInverseCumulativeProbability_validArguments() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double probability = 0.5;
        double expectedValue = 1.2586182746311524;
        double result = distribution.inverseCumulativeProbability(probability);
        assertEquals(expectedValue, result, 0.00001);
    }

    @Test
    public void testInverseCumulativeProbability_invalidArguments() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(1.2));
        assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(-1.0));
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(1.0));
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom_validArgument() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(4.0);
        assertEquals(4.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom_invalidArgument() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        assertThrows(IllegalArgumentException.class, () -> distribution.setNumeratorDegreesOfFreedom(-2.0));
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom_validArgument() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom_invalidArgument() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        assertThrows(IllegalArgumentException.class, () -> distribution.setDenominatorDegreesOfFreedom(-2.0));
    }

}