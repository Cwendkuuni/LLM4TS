package org.apache.commons.math.distribution;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.stats.distribution.BetaDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplCreation() {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        assertEquals(1.0, fd.getNumeratorDegreesOfFreedom(), 0.01);
        assertEquals(2.0, fd.getDenominatorDegreesOfFreedom(), 0.01);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testFDistributionImplNegativeDegreesOfFreedom() {
        new FDistributionImpl(-1.0, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testFDistributionImplZeroNumeratorDegreesOfFreedom() {
        new FDistributionImpl(0.0, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testFDistributionImplZeroDenominatorDegreesOfFreedom() {
        new FDistributionImpl(1.0, 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        double x = 0.5;
        double p = 0.5;
        double expected = 0.435335283236613;
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);

        x = 1.0;
        p = 0.95;
        expected = 0.998912835456021;
        actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);

        x = 3.0;
        p = 0.9999;
        actual = distribution.cumulativeProbability(x);
        assertEquals(1.0, actual, 0.0001);
    }

    @Test(expected = MathUnsupportedOperationException.class)
    public void testInverseCumulativeProbabilityNegativeProbability() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        distribution.inverseCumulativeProbability(-0.1);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        double p = 0.5;
        double expected = 0.5;
        double actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.01);

        p = 0.95;
        double x = 1.7641;
        expected = x;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.01);

        p = 0.999;
        x = 3.3897;
        expected = x;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.01);
    }

    @Test(expected = MathUnsupportedOperationException.class)
    public void testCumulativeProbabilityInvalidProbability() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        distribution.cumulativeProbability(2.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        distribution.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        double degreesOfFreedom = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(1.0, degreesOfFreedom, 0.01);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        double degreesOfFreedom = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(2.0, degreesOfFreedom, 0.01);
    }
}