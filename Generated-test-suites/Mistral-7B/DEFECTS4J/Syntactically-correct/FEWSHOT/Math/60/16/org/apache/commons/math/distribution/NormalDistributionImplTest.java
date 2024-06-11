package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoSuchElementException;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0.001);
        assertEquals(1, distribution.getStandardDeviation(), 0.001);

        // Test with negative standard deviation
        try {
            new NormalDistributionImpl(0, -1);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNormalDistributionImplWithInvalidMean() {
        new NormalDistributionImpl(0, 1, "invalid");
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNormalDistributionImplWithInvalidStandardDeviation() {
        new NormalDistributionImpl(0, 1, 0);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double density1 = distribution.density(0.5);
        assertEquals(0.1257335634518783, density1, 0.000001);

        double density2 = distribution.density(10);
        assertEquals(0.00001140651923428363, density2, 0.00000001);

        double density3 = distribution.density(-10);
        assertEquals(0.00001140651923428363, density3, 0.00000001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double cdf1 = distribution.cumulativeProbability(0);
        assertEquals(0.5, cdf1, 0.00001);

        double cdf2 = distribution.cumulativeProbability(1);
        assertEquals(0.8413447460947461, cdf2, 0.00001);

        double cdf3 = distribution.cumulativeProbability(-1);
        assertEquals(0.1586552539052539, cdf3, 0.00001);

        double cdf4 = distribution.cumulativeProbability(3);
        assertEquals(0.9973428430713457, cdf4, 0.00001);

        double cdf5 = distribution.cumulativeProbability(-3);
        assertEquals(0.0026571569286543, cdf5, 0.00001);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double icdf1 = distribution.inverseCumulativeProbability(0.25);
        assertEquals(-0.6794575819331232, icdf1, 0.00001);

        double icdf2 = distribution.inverseCumulativeProbability(0.75);
        assertEquals(0.6794575819331232, icdf2, 0.00001);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityZero() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(0);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityOne() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(1);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double sample1 = distribution.sample();
        assertEquals(-0.351606264825824, sample1, 0.001);

        double sample2 = distribution.sample();
        assertEquals(0.6158333426527975, sample2, 0.001);

        double sample3 = distribution.sample();
        assertEquals(0.1936212159533329, sample3, 0.001);
    }
}