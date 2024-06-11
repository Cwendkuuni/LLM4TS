package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        assertEquals(0.606512098039264, distribution.probability(0), 0.000001);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.337306514126398, distribution.probability(1), 0.000001);
        assertEquals(0.080525193546031, distribution.probability(2), 0.000001);
    }

    @Test
    public void testProbabilityMaxValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.000001654860332, distribution.probability(Integer.MAX_VALUE), 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNegative() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        distribution.probability(-1);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityPositive() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.598224259261422, distribution.cumulativeProbability(1), 0.00001);
        assertEquals(0.81583672880632, distribution.cumulativeProbability(2), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        assertEquals(0.531308983756172, distribution.normalApproximateProbability(0), 0.000001);
    }

    @Test
    public void testNormalApproximateProbabilityPositive() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.293809516727151, distribution.normalApproximateProbability(1), 0.000001);
        assertEquals(0.118613873831246, distribution.normalApproximateProbability(2), 0.000001);
    }

    @Test
    public void testNormalApproximateProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.00000289633986, distribution.normalApproximateProbability(Integer.MAX_VALUE), 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sampledValue = distribution.sample();
        assertEquals(2, sampledValue, 0);
    }

    @Test
    public void testSampleOutOfRange() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sampledValue = distribution.sample();
        int maxValue = Integer.MAX_VALUE - (int) (1000 * distribution.getMean());
        sampledValue = sampledValue > maxValue ? maxValue : sampledValue;
        assertEquals(maxValue, sampledValue, 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double p = 0.95;
        int expected = (int) Math.round(Gamma.regularizedGammaInv(1 - p, distribution.getMean()));
        int actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual);
    }

}