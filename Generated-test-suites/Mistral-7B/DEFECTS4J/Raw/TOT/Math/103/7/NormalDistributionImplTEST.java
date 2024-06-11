package org.apache.commons.math.distribution.test;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test cases for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals("Default constructor: mean = 0.0, standard deviation = 1.0", 0.0, distribution.getMean(), 0.001);
        assertEquals("Default constructor: mean = 0.0, standard deviation = 1.0", 1.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test the constructor with specified mean and standard deviation.
     */
    @Test
    public void testSpecifiedConstructor() {
        NormalDistribution distribution = new NormalDistributionImpl(1.5, 2.5);
        assertEquals("Constructor with mean = 1.5 and standard deviation = 2.5", 1.5, distribution.getMean(), 0.001);
        assertEquals("Constructor with mean = 1.5 and standard deviation = 2.5", 2.5, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test setting mean and standard deviation.
     */
    @Test
    public void testSettingMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(2.0);
        distribution.setStandardDeviation(3.0);
        assertEquals("Setting mean and standard deviation", 2.0, distribution.getMean(), 0.001);
        assertEquals("Setting mean and standard deviation", 3.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test getMean() method.
     */
    @Test
    public void testGetMean() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Mean", 1.0, distribution.getMean(), 0.001);
    }

    /**
     * Test setMean() method.
     */
    @Test
    public void testSetMean() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setMean(3.0);
        assertEquals("Mean", 3.0, distribution.getMean(), 0.001);
    }

    /**
     * Test getStandardDeviation() method.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("Standard deviation", 1.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test setStandardDeviation() method.
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(2.0);
        assertEquals("Standard deviation", 2.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test cumulativeProbability() method for various x values.
     * @throws MathException
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        // Normal distribution with mean = 0 and standard deviation = 1
        // P(X < x) = 0.5 for x = 0
        assertEquals("Cumulative probability for x = 0", 0.5, distribution.cumulativeProbability(0.0), 0.001);

        // P(X < x) = 0.5 for x = mean
        assertEquals("Cumulative probability for x = mean", 0.5, distribution.cumulativeProbability(0.0), 0.001);

        // P(X < x) > 0.5 for x > mean
        assertEquals("Cumulative probability for x > mean", 0.51, distribution.cumulativeProbability(1.0), 0.01);

        // P(X < x) < 0.5 for x < mean
        assertEquals("Cumulative probability for x < mean", 0.49, distribution.cumulativeProbability(-1.0), 0.01);
    }

    /**
     * Test inverseCumulativeProbability() method for various p values.
     * @throws MathException
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        // p = 0
        assertEquals("Inverse cumulative probability for p = 0", Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0);

        // p = 1
        assertEquals("Inverse cumulative probability for p = 1", Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);

        // p = 0.25
        assertEquals("Inverse cumulative probability for p = 0.25", -0.675, distribution.inverseCumulativeProbability(0.25), 0.01);

        // p = 0.75
        assertEquals("Inverse cumulative probability for p = 0.75", 0.675, distribution.inverseCumulativeProbability(0.75), 0.01);
    }

    /**
     * Test that the method returns the correct mean and standard deviation.
     */
    @Test
    public void testStatistics() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double[] data = new double[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = distribution.inverseCumulativeProbability(i1.nextDouble()) * standardDeviation.evaluate(data);
        }
        assertEquals("Mean", 0.0, distribution.getMean(), 0.01);
        assertEquals("Standard deviation", 1.0, new StandardDeviation().evaluate(data), 0.01);
    }
}