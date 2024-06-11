package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.Rankers;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.junit.Test;

/**
 * Test for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test with two equal samples.
     */
    @Test
    public void testSameSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.1, 1.2, 1.3, 1.4};
        double[] y = x.clone();
        double expected = 0.0;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e-13);
        double pValue = test.mannnWhitneyUTest(x, y);
        assertEquals(expected, pValue, 1e-13);
    }

    /**
     * Test with two different samples with same mean.
     */
    @Test
    public void testSameMeanSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.5, 1.6, 1.7, 1.8, 1.9};
        double[] y = {1.5, 1.6, 1.7, 1.8, 1.9, 2.0};
        double expected = 0.5;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e-13);
        double pValue = test.mannnWhitneyUTest(x, y);
        assertEquals(expected, pValue, 1e-13);
    }

    /**
     * Test with two different samples with different means.
     */
    @Test
    public void testDifferentMeansSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.5, 1.6, 1.7, 1.8, 1.9};
        double[] y = {2.5, 2.6, 2.7, 2.8, 2.9};
        double expected = 0.05;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e-13);
        double pValue = test.mannnWhitneyUTest(x, y);
        assertEquals(expected, pValue, 1e-13);
    }

    /**
     * Test with zero-length samples.
     */
    @Test(expected = NoDataException.class)
    public void testZeroLengthSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    /**
     * Test with null arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    /**
     * Test with different data types for samples.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDifferentDataTypes() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.1, 1.2, 1.3, 1.4};
        int[] y = {1, 2, 3, 4, 5};
        test.mannWhitneyU(x, y);
    }

    /**
     * Test with NaN values in samples.
     */
    @Test
    public void testNaNValues() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.MISSING,
                TiesStrategy.MIN);
        double[] x = {1.0, 1.1, 1.2, Double.NaN, 1.4};
        double[] y = {1.5, 1.6, 1.7, Double.NaN, 1.9};
        double expected = 0.125;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e-13);
        double pValue = test.mannnWhitneyUTest(x, y);
        /*
         * The pValue may be different due to floating point arithmetic
         * inaccuracy. Accept a small tolerance here.
         */
        assertTrue(pValue > expected - 0.001 && pValue < expected + 0.001);
    }

    /**
     * Test with different ranking strategies.
     */
    @Test
    public void testDifferentRankingStrategies() {
        MannWhitneyUTest test1 = new MannWhitneyUTest();
        MannWhitneyUTest test2 = new MannWhitneyUTest(
                NaturalRanking.MIN, TiesStrategy.FIRST);
        double[] x = {1.5, 1.6, 1.7, 1.8, 1.9};
        double[] y = {1.5, 1.6, 1.7, 1.8, 1.9, 2.0};
        double expectedU = 3.0;
        double actualU1 = test1.mannWhitneyU(x, y);
        assertEquals(expectedU, actualU1, 1e-13);
        double expectedU2 = 2.5;
        double actualU2 = test2.mannWhitneyU(x, y);
        assertEquals(expectedU, actualU2, 1e-13);

        double expectedPValue1 = 0.05;
        double actualPValue1 = test1.mannnWhitneyUTest(x, y);
        assertEquals(expectedPValue1, actualPValue1, 1e-13);

        double expectedPValue2 = 0.0625;
        double actualPValue2 = test2.mannnWhitneyUTest(x, y);
        assertEquals(expectedPValue2, actualPValue2, 1e-13);
    }

    /**
     * Test with large samples.
     */
    @Test
    public void testLargeSamples() throws ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[100_000];
        double[] y = new double[100_000];
        Mean meanX = new Mean();
        Mean meanY = new Mean();
        for (int i = 0; i < x.length; ++i) {
            x[i] = FastMath.nextGaussian();
            y[i] = FastMath.nextGaussian();
            meanX.addValue(x[i]);
            meanY.addValue(y[i]);
        }
        double expectedU = test.mannWhitneyU(x, y);
        double actualU = test.mannWhitneyU(x, y);
        assertEquals(expectedU, actualU, 1e-13);
        double expectedPValue = test.mannnWhitneyUTest(x, y);
        double actualPValue = test.mannnWhitneyUTest(x, y);
        assertEquals(expectedPValue, actualPValue, 1e-13);
        assertEquals(meanX.getValue(), meanY.getValue(), 1e-9);
    }

}