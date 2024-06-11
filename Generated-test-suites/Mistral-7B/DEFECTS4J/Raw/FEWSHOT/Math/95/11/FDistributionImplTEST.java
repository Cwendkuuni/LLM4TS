package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FServiceRegistry;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {
    @Test
    public void testFDistributionImplConstruction() {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        assertNotNull(fdist);
        assertEquals(2.0, fdist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fdist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        for (double x = 0.0; x <= 5.0; x += 0.5) {
            double prob = fdist.cumulativeProbability(x);
            assertTrue(prob >= 0.0 && prob <= 1.0);
        }
    }

    @Test
    public void testCumulativeProbabilityNegativeX() throws MathException {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        double prob = fdist.cumulativeProbability(-1.0);
        assertEquals(0.0, prob, 0.0);
    }

    @Test
    public void testCumulativeProbabilityNaN() throws MathException {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        double prob = fdist.cumulativeProbability(Double.NaN);
        assertEquals(0.0, prob, 0.0);
    }

    @Test
    public void testCumulativeProbabilityInfinity() throws MathException {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        double prob = fdist.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertEquals(1.0, prob, 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = fdist.inverseCumulativeProbability(p);
            assertTrue(x > 0.0);
        }
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        double x = fdist.inverseCumulativeProbability(0.0);
        assertEquals(0.0, x, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        double x = fdist.inverseCumulativeProbability(1.0);
        assertFalse(Double.isFinite(x));
    }

    @Test
    public void testInverseCumulativeProbabilityNaN() throws MathException {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        assertThrows(MathIllegalArgumentException.class, () -> {
            fdist.inverseCumulativeProbability(Double.NaN);
        });
    }

    @Test
    public void testInverseCumulativeProbabilityNegativeP() throws MathException {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        assertThrows(MathIllegalArgumentException.class, () -> {
            fdist.inverseCumulativeProbability(-0.1);
        });
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        assertEquals(2.0, fdist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        fdist.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, fdist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        fdist.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        assertEquals(4.0, fdist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        fdist.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, fdist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistribution fdist = new FDistributionImpl(2.0, 4.0);
        fdist.setDenominatorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testBetaRegularizedBeta() throws MathException {
        double x = 2.5;
        double a = 3.0;
        double b = 3.5;
        double result = Beta.regularizedBeta(x * a / (a + x * b), a / 2.0, b / 2.0);
        FDistribution fdist = new FDistributionImpl(a, b);
        double prob = fdist.cumulativeProbability(x);
        assertEquals(result, prob, 0.001);
    }

    @Test
    public void testBetaRegularizedBetaNaN() throws MathException {
        assertThrows(MathArithmeticException.class, () -> {
            Beta.regularizedBeta(Double.NaN, 0.5, 0.5);
        });
    }

    @Test
    public void testBetaRegularizedBetaNegativeArguments() throws MathException {
        assertThrows(MathIllegalArgumentException.class, () -> {
            Beta.regularizedBeta(-1.0, 0.5, 0.5);
        });
    }

    @Test
    public void testBetaServiceRegistry() {
        FServiceRegistry registry = FServiceRegistry.getInstance();
        FDistribution fdist = (FDistribution) registry.getDistribution("F", 2.0, 4.0);
        assertEquals(2.0, fdist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fdist.getDenominatorDegreesOfFreedom(), 0.0);
    }
}