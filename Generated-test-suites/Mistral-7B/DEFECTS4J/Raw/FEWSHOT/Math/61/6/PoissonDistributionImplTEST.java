package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testPoissonDistributionIllegalArgumentExceptionWithNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.0);
        int domainLowerBound = poissonDistribution.getDomainLowerBound(0.0);
        assertEquals(0, domainLowerBound);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.0);
        int domainUpperBound = poissonDistribution.getDomainUpperBound(0.0);
        assertEquals(Integer.MAX_VALUE, domainUpperBound);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double probabilityZero = poissonDistribution.probability(0);
        assertTrue(probabilityZero > 0);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double probabilityPositive = poissonDistribution.probability(1);
        assertTrue(probabilityPositive > 0);
    }

    @Test
    public void testProbabilityNegative() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double probabilityNegative = poissonDistribution.probability(-1);
        assertEquals(0.0, probabilityNegative);
    }

    @Test
    public void testProbabilityMaxValue() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double probabilityMaxValue = poissonDistribution.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probabilityMaxValue);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbabilityNegativeValue() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.cumulativeProbability(-1);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbabilityZero = poissonDistribution.cumulativeProbability(0);
        assertEquals(0.0, cumulativeProbabilityZero);
    }

    @Test
    public void testCumulativeProbabilityOne() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbabilityOne = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.3678794411503954, cumulativeProbabilityOne, 0.0000000000001);
    }

    @Test
    public void testCumulativeProbabilityLargeValue() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(10.0);
        double cumulativeProbabilityLargeValue = poissonDistribution.cumulativeProbability(15);
        assertEquals(1.0, cumulativeProbabilityLargeValue, 0.0000000000001);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double normalApproximateProbabilityZero = poissonDistribution.normalApproximateProbability(0);
        assertEquals(0.3678794411503954, normalApproximateProbabilityZero, 0.0000000000001);
    }

    @Test
    public void testNormalApproximateProbabilityOne() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double normalApproximateProbabilityOne = poissonDistribution.normalApproximateProbability(1);
        assertEquals(0.3192857142857143, normalApproximateProbabilityOne, 0.0000000000001);
    }

    @Test
    public void testNormalApproximateProbabilityTwo() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double normalApproximateProbabilityTwo = poissonDistribution.normalApproximateProbability(2);
        assertEquals(0.2301270463641572, normalApproximateProbabilityTwo, 0.0000000000001);
    }

    @Test
    public void testSampleZero() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        int sampleZero = poissonDistribution.sample();
        assertEquals(0, sampleZero);
    }

    @Test
    public void testSampleOne() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        int sampleOne = poissonDistribution.sample();
        assertEquals(1, sampleOne);
    }

    @Test
    public void testSampleTwo() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        int sampleTwo = poissonDistribution.sample();
        assertEquals(2, sampleTwo);
    }

    @Test
    public void testSampleMaxValue() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        int sampleMaxValue = poissonDistribution.sample();
        assertEquals(Integer.MAX_VALUE, sampleMaxValue);
    }

    @Test
    public void testSampleMaxValueWithLargeMean() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100000.0);
        int sampleMaxValueWithLargeMean = poissonDistribution.sample();
        assertTrue(sampleMaxValueWithLargeMean < 100000 * 10);
    }

    @Test
    public void testSampleWithRandomDataGenerator() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        PoissonDistribution poissonDistributionWithRandomDataGenerator = new PoissonDistribution() {
            @Override
            public int sample() throws MathException {
                return (int) FastMath.min(randomDataGenerator.nextPoisson(mean), Integer.MAX_VALUE);
            }
        };
        int sampleWithRandomDataGenerator = poissonDistributionWithRandomDataGenerator.sample();
        assertEquals(sampleWithRandomDataGenerator, poissonDistribution.sample());
    }

    @Test
    public void testSampleWithRejectionAlgorithm() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100.0);
        int sampleWithRejectionAlgorithm = poissonDistribution.sample();
        assertTrue(sampleWithRejectionAlgorithm < 1000 * 100);
    }

    @Test
    public void testSampleWithLargeMeanAndPoissonProcess() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(10000.0);
        int sampleWithLargeMeanAndPoissonProcess = poissonDistribution.sample();
        assertTrue(sampleWithLargeMeanAndPoissonProcess < 10000 * 10);
    }

    @Test
    public void testSampleWithLargeMeanAndRejectionAlgorithm() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100000.0);
        int sampleWithLargeMeanAndRejectionAlgorithm = poissonDistribution.sample();
        assertTrue(sampleWithLargeMeanAndRejectionAlgorithm < 100000 * 10);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double inverseCumulativeProbabilityZero = poissonDistribution.inverseCumulativeProbability(0);
        assertEquals(0.0, inverseCumulativeProbabilityZero);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double inverseCumulativeProbabilityOne = poissonDistribution.inverseCumulativeProbability(1);
        assertEquals(1.0, inverseCumulativeProbabilityOne);
    }

    @Test
    public void testInverseCumulativeProbabilityLargeValue() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double inverseCumulativeProbabilityLargeValue = poissonDistribution.inverseCumulativeProbability(0.9);
        assertTrue(inverseCumulativeProbabilityLargeValue > 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testInverseCumulativeProbabilityNullArgument() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.inverseCumulativeProbability(null);
    }
}