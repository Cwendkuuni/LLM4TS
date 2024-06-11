package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() {
        try {
            FDistribution fd = new FDistribution(2.5, 4.5);
            assertTrue(fd instanceof FDistribution);
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(3.0, 5.0);

        double density = fd.density(2.0);
        assertEquals(0.200866114334153, density, 1e-8);

        density = fd.density(3.0);
        assertEquals(0.358872858199226, density, 1e-8);

        density = fd.density(5.0);
        assertEquals(0.01320883585994, density, 1e-8);

        density = fd.density(10.0);
        assertEquals(3.5190523613414e-11, density, 1e-11);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(3.0, 5.0);

        double p = fd.cumulativeProbability(0.0);
        assertEquals(0.0, p, 1e-8);

        p = fd.cumulativeProbability(0.5);
        assertEquals(0.5, p, 1e-8);

        p = fd.cumulativeProbability(1.0);
        assertEquals(0.81100845082123, p, 1e-8);

        p = fd.cumulativeProbability(2.0);
        assertEquals(0.95141380262548, p, 1e-8);

        p = fd.cumulativeProbability(3.0);
        assertEquals(0.99670488637978, p, 1e-8);

        p = fd.cumulativeProbability(10.0);
        assertEquals(1.0, p, 1e-8);
    }

    @Test
    public void testFDistributionMean() {
        FDistribution fd = new FDistribution(3.0, 5.0);

        double mean = fd.getNumericalMean();
        assertEquals(3.2, mean, 1e-6);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution fd = new FDistribution(3.0, 5.0);

        double variance = fd.getNumericalVariance();
        assertEquals(2.22360679774998, variance, 1e-6);

        variance = fd.getNumericalVariance(); // check that it is cached
        assertEquals(2.22360679774998, variance, 1e-6);
    }

    @Test
    public void testFDistributionRandomGeneration() {
        FDistribution fd = new FDistribution(3.0, 5.0, new Well19937c());

        double x = fd.inverseCumulativeProbability(0.1);
        assertEquals(1.4182949869456, x, 1e-8);

        x = fd.inverseCumulativeProbability(0.5);
        assertEquals(2.4374472851611, x, 1e-8);

        x = fd.inverseCumulativeProbability(0.9);
        assertEquals(5.1935141928571, x, 1e-8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionIllegalArguments() {
        FDistribution fd = new FDistribution(-3.0, 5.0);
    }
}