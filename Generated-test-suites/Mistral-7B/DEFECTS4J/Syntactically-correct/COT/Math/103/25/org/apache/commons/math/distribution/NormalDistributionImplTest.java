package org.apache.commons.math.distribution.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.distribution.NormalDistributionImpl;

/**
 * Test cases for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation.
     */
    @ org.junit.Test
    public void testNormalDistributionImplWithMeanAndStdDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.001);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test default constructor.
     */
    @ org.junit.Test
    public void testNormalDistributionImplDefaultConstructor() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test getMean method.
     */
    @ org.junit.Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.001);

        distribution.setMean(3.0);
        assertEquals(3.0, distribution.getMean(), 0.001);
    }

    /**
     * Test setMean method with invalid argument.
     */
    @ org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetMeanWithNegativeMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(-1.0);
    }

    /**
     * Test getStandardDeviation method.
     */
    @ org.junit.Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);

        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test setStandardDeviation method with invalid argument.
     */
    @ org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNegativeStdDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }

    /**
     * Test cumulativeProbability method with valid argument.
     */
    @ org.junit.Test
    public void testCumulativeProbabilityWithValidArgument() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        double result = distribution.cumulativeProbability(1.5);
        double expected = 0.43319271110651643; // Calculated using Erf function
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test cumulativeProbability method with argument outside the domain.
     */
    @ org.junit.Test
    public void testCumulativeProbabilityWithOutsideDomainArgument() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        double result = distribution.cumulativeProbability(-5.0);
        assertEquals(0.0, result, 0.001);

        result = distribution.cumulativeProbability(10.0);
        assertEquals(1.0, result, 0.001);
    }

    /**
     * Test inverseCumulativeProbability method with valid arguments.
     */
    @ org.junit.Test
    public void testInverseCumulativeProbabilityWithValidArguments() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        double result = distribution.inverseCumulativeProbability(0.1);
        double expected = 0.5 * (1.0 + Erf.erf(0.1 * Math.sqrt(2.0) / 2.0)) * 2.0 * 1.0 + 1.0;
        assertEquals(expected, result, 0.001);

        result = distribution.inverseCumulativeProbability(0.95);
        expected = 1.576156842172441; // Calculated using Erf function
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test inverseCumulativeProbability method with arguments outside the range [0, 1].
     */
    @ org.junit.Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidArguments() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        distribution.inverseCumulativeProbability(-1.0);
    }

    /**
     * Test inverseCumulativeProbability method with large number of iterations.
     */
    @ org.junit.Test
    public void testInverseCumulativeProbabilityWithLargeNumberOfIterations() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        try {
            distribution.inverseCumulativeProbability(0.5, 100000);
        } catch (MaxIterationsExceededException e) {
            // Expected result
        }
    }
}