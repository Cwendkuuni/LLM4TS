package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatFunctions;
import org.junit.Test;

/**
 * JUnit test cases for NormalDistributionImpl class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testNormalDistributionImplConstructorWithValidArguments() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(2.5, distribution.getMean(), 0.0);
        assertEquals(1.5, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with zero mean and unit standard deviation.
     */
    @Test
    public void testNormalDistributionImplConstructorZeroMeanUnitStdDev() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting mean and standard deviation.
     */
    @Test
    public void testSettingMeanAndStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(1.0);
        distribution.setStandardDeviation(2.5);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(2.5, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test mean accessor.
     */
    @Test
    public void testMeanAccessor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(1.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
    }

    /**
     * Test mean setter with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMeanSetterWithNegativeValue() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(-1.0);
    }

    /**
     * Test standard deviation accessor.
     */
    @Test
    public void testStandardDeviationAccessor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test standard deviation setter with zero or negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardDeviationSetterWithZeroOrNegativeValue() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(0.0);
        distribution.setStandardDeviation(-1.0);
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbabilityMethod() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double prob = 0.3;
        double expected = Erf.erf((prob - distribution.getMean()) /
                (distribution.getStandardDeviation() * Math.sqrt(2.0)));
        double result = distribution.cumulativeProbability(prob);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test cumulative probability method with extreme values.
     */
    @Test
    public void testCumulativeProbabilityMethodWithExtremeValues()
            throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double probMin = StatFunctions.PERCENTILE_LOWER_TAIL;
        double probMax = StatFunctions.PERCENTILE_UPPER_TAIL;
        double expectedMin = -Math.abs(StatFunctions.PERCENTILE_LOWER_TAIL);
        double resultMin = distribution.cumulativeProbability(probMin);
        assertEquals(expectedMin, resultMin, 0.001);
        double expectedMax = 1.0 - Math.abs(StatFunctions.PERCENTILE_UPPER_TAIL);
        double resultMax = distribution.cumulativeProbability(probMax);
        assertEquals(expectedMax, resultMax, 0.001);
    }

    /**
     * Test inverse cumulative probability method with valid arguments.
     */
    @Test
    public void testInverseCumulativeProbabilityMethodWithValidArguments()
            throws MathException, IllegalArgumentException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double prob = 0.4;
        double result = distribution.inverseCumulativeProbability(prob);
        double x = StatFunctions.INVERSE_NORMAL_DIST_CDF_INV(prob, distribution.getMean(), distribution.getStandardDeviation());
        assertEquals(x, result, 0.001);
    }

    /**
     * Test inverse cumulative probability method with extreme values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityMethodWithExtremeValues()
            throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double probMin = 0.0;
        double probMax = 1.0;
        distribution.inverseCumulativeProbability(probMin);
        distribution.inverseCumulativeProbability(probMax);
    }
}