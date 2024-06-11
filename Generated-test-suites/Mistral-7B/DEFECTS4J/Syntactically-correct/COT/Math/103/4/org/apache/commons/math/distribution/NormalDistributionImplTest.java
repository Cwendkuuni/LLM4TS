package org.apache.commons.math.distribution.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test class for NormalDistributionImpl.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor NormalDistributionImpl(double mean, double sd).
     */
    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("mean", 0.0, distribution.getMean(), 1e-6);
        assertEquals("standard deviation", 1.0, distribution.getStandardDeviation(), 1e-6);

        // test with negative standard deviation
        try {
            new NormalDistributionImpl(0.0, -1.0);
            fail("Expected IllegalArgumentException with negative standard deviation");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test with zero standard deviation
        try {
            new NormalDistributionImpl(0.0, 0.0);
            fail("Expected IllegalArgumentException with zero standard deviation");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method getMean().
     */
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("mean", 0.0, distribution.getMean(), 1e-6);

        NormalDistributionImpl distributionWithMean = new NormalDistributionImpl(5.0, 1.0);
        assertEquals("mean", 5.0, distributionWithMean.getMean(), 1e-6);

        // test with negative mean
        try {
            new NormalDistributionImpl(-1.0, 1.0);
            fail("Expected IllegalArgumentException with negative mean");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method setMean().
     */
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setMean(5.0);
        assertEquals("mean", 5.0, distribution.getMean(), 1e-6);

        NormalDistributionImpl distributionWithNegativeMean = new NormalDistributionImpl(0.0, 1.0);
        try {
            distributionWithNegativeMean.setMean(-1.0);
            fail("Expected IllegalArgumentException with negative mean");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method getStandardDeviation().
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("standard deviation", 1.0, distribution.getStandardDeviation(), 1e-6);

        NormalDistributionImpl distributionWithStandardDeviation = new NormalDistributionImpl(0.0, 2.0);
        assertEquals("standard deviation", 2.0, distributionWithStandardDeviation.getStandardDeviation(), 1e-6);

        // test with zero standard deviation
        try {
            new NormalDistributionImpl(0.0, 0.0);
            fail("Expected IllegalArgumentException with zero standard deviation");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test with negative standard deviation
        try {
            new NormalDistributionImpl(0.0, -1.0);
            fail("Expected IllegalArgumentException with negative standard deviation");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method setStandardDeviation().
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(2.0);
        assertEquals("standard deviation", 2.0, distribution.getStandardDeviation(), 1e-6);

        NormalDistributionImpl distributionWithNegativeStandardDeviation = new NormalDistributionImpl(0.0, 1.0);
        try {
            distributionWithNegativeStandardDeviation.setStandardDeviation(-1.0);
            fail("Expected IllegalArgumentException with negative standard deviation");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method cumulativeProbability().
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        // test values close to mean
        double[] values = new double[]{-1.0, -0.5, 0.0, 0.5, 1.0};
        double[] expectedCumulativeProbabilities = new double[] {0.15645, 0.31726, 0.5, 0.68274, 0.84355};
        for (int i = 0; i < values.length; i++) {
            double result = distribution.cumulativeProbability(values[i]);
            assertEquals("cumulativeProbability(" + values[i] + ")", expectedCumulativeProbabilities[i], result, 1e-6);
        }

        // test values very close to mean
        distribution = new NormalDistributionImpl(0.0, 1.0e-6);
        values = new double[]{-1.0e-6, -1.0e-9, 0.0, 1.0e-6, 1.0e-9};
        expectedCumulativeProbabilities = new double[] {0.50000, 0.50000, 0.50000, 0.50000, 0.50000};
        for (int i = 0; i < values.length; i++) {
            double result = distribution.cumulativeProbability(values[i]);
            assertEquals("cumulativeProbability(" + values[i] + ")", expectedCumulativeProbabilities[i], result, 1e-10);
        }

        // test values very far from mean
        distribution = new NormalDistributionImpl(1.0, 1.0);
        values = new double[]{-5.0, -3.0, 3.0, 5.0};
        expectedCumulativeProbabilities = new double[] {0.00000, 0.15866, 0.84134, 1.0};
        for (int i = 0; i < values.length; i++) {
            double result = distribution.cumulativeProbability(values[i]);
            assertEquals("cumulativeProbability(" + values[i] + ")", expectedCumulativeProbabilities[i], result, 1e-6);
        }

        // test exception when x is more than 20 standard deviations from mean
        try {
            distribution = new NormalDistributionImpl(0.0, 1.0);
            double x = 100.0 * distribution.getStandardDeviation();
            distribution.cumulativeProbability(x);
            fail("Expected MathException when x is more than 20 standard deviations from mean");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test method inverseCumulativeProbability().
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException, IllegalArgumentException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        // test probabilities close to 0 and 1
        double[] probabilities = new double[] {0.0, 0.1, 0.5, 0.9, 1.0};
        double[] expectedValues = new double[] {-3.097377119235, -1.0, 0.0, 0.0, 1.0};
        for (int i = 0; i < probabilities.length; i++) {
            double result = distribution.inverseCumulativeProbability(probabilities[i]);
            assertEquals("inverseCumulativeProbability(" + probabilities[i] + ")", expectedValues[i], result, 1e-6);
        }

        // test probabilities in between
        double probability = 0.4;
        double result = distribution.inverseCumulativeProbability(probability);
        double expectedValue = StatUtils.percentPointNormal(probability, 0.0, 1.0);
        assertEquals("inverseCumulativeProbability(" + probability + ")", expectedValue, result, 1e-6);

        // test negative probability
        try {
            distribution.inverseCumulativeProbability(-0.1);
            fail("Expected IllegalArgumentException with negative probability");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test probability greater than 1
        try {
            distribution.inverseCumulativeProbability(1.1);
            fail("Expected MathException with probability greater than 1");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test method inverseCumulativeProbability(double) with different means and standard deviations.
     */
    @Test
    public void testInverseCumulativeProbabilityWithDifferentMeansAndStandardDeviations() throws MathException, IllegalArgumentException {
        NormalDistributionImpl distributionWithMeanOne = new NormalDistributionImpl(1.0, 1.0);
        NormalDistributionImpl distributionWithMeanTwo = new NormalDistributionImpl(2.0, 1.0);

        // test probabilities close to 0 and 1
        double[] probabilities = new double[] {0.0, 0.1, 0.5, 0.9, 1.0};
        for (int i = 0; i < probabilities.length; i++) {
            double[] results = new double[] {distributionWithMeanOne.inverseCumulativeProbability(probabilities[i]),
                                            distributionWithMeanTwo.inverseCumulativeProbability(probabilities[i])};
            double[] expectedValues = new double[] {StatUtils.percentPointNormal(probabilities[i], 1.0, 1.0),
                                                   StatUtils.percentPointNormal(probabilities[i], 2.0, 1.0)};
            for (int j = 0; j < results.length; j++) {
                assertEquals("inverseCumulativeProbability(" + probabilities[i] + ") with different means and standard deviations",
                             expectedValues[j], results[j], 1e-6);
            }
        }

        // test probabilities in between
        double probability = 0.4;
        double[] results = new double[] {distributionWithMeanOne.inverseCumulativeProbability(probability),
                                         distributionWithMeanTwo.inverseCumulativeProbability(probability)};
        double expectedValueOne = StatUtils.percentPointNormal(probability, 1.0, 1.0);
        double expectedValueTwo = StatUtils.percentPointNormal(probability, 2.0, 1.0);
        for (int i = 0; i < results.length; i++) {
            assertEquals("inverseCumulativeProbability(" + probability + ") with different means and standard deviations",
                         expectedValueOne, results[0], 1e-6);
            assertEquals("inverseCumulativeProbability(" + probability + ") with different means and standard deviations",
                         expectedValueTwo, results[1], 1e-6);
        }
    }

    /**
     * Test method inverseCumulativeProbability() with MathException when x is more than 20 standard deviations from mean.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithMathException() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double x = 100.0 * distribution.getStandardDeviation();
        distribution.inverseCumulativeProbability(0.5);
    }

    /**
     * Test method inverseCumulativeProbability() with IllegalArgumentException when p is not a valid probability.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithIllegalArgumentException() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(-0.1);
    }
}