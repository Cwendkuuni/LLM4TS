package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Tests the MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /** Check U-Test with normal distributed data. */
    @Test
    public void testUWithNormalData() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = { 1.5, 2.3, 4.5, 6.3 };
        double[] y = { 1.6, 2.4, 4.4, 6.2 };
        double expected = 7.0;
        double actual = mwu.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e-6);
    }

    /** Check U-Test with non-normal distributed data. */
    @Test
    public void testUWithNonNormalData() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = { 1.5, 2.3, 4.5, 6.3 };
        double[] y = { 1.0, 1.5, 2.0, 3.0, 5.0 };
        double expected = 7.5;
        double actual = mwu.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e-6);
    }

    /** Check U-Test with empty arrays. */
    @Test(expected = NoDataException.class)
    public void testUWithEmptyArrays() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        mwu.mannWhitneyU(x, y);
    }

    /** Check U-Test with NaN values. */
    @Test
    public void testUWithNaNValues() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = { 1.5, Double.NaN, 4.5, 6.3 };
        double[] y = { 1.6, 2.4, 4.4, Double.NaN };
        double expected = 7.0;
        double actual = mwu.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e-6);
    }

    /** Check U-Test with negative values. */
    @Test(expected = IllegalArgumentException.class)
    public void testUWithNegativeValues() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = { -1.5, 2.3, 4.5, 6.3 };
        double[] y = { 1.6, 2.4, 4.4, 6.2 };
        mwu.mannWhitneyU(x, y);
    }

    /** Check U-Test with null values. */
    @Test(expected = NullArgumentException.class)
    public void testUWithNullValues() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = null;
        double[] y = { 1.6, 2.4, 4.4, 6.2 };
        mwu.mannWhitneyU(x, y);
    }

    /**
     * Test U-Test with the given strategies for NaN's and ties.
     */
    @Test
    public void testUWithCustomStrategies() {
        MannWhitneyUTest mwu = new MannWhitneyUTest(
                NASTRATEGY_LAST,
                TIESSTRATEGY_DENSE_RANKS);
        double[] x = { 1.5, 2.3, 4.5, 6.3 };
        double[] y = { 1.6, 2.4, 4.4, 6.2 };
        double expected = 8.5;
        double actual = mwu.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test the U-Test with the given strategies for NaN's and ties.
     *
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     */
    @Test
    public void testUWithCustomStrategiesAsymptoticPValue()
        throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mwu = new MannWhitneyUTest(
                NASTRATEGY_LAST,
                TIESSTRATEGY_DENSE_RANKS);
        double[] x = { 1.5, 2.3, 4.5, 6.3 };
        double[] y = { 1.6, 2.4, 4.4, 6.2 };
        double expected = 0.05;
        double actual = mwu.mannWhitneyUTest(x, y);
        assertEquals(Precision.round(expected, 5), Precision.round(actual, 5), 5);
    }

    /** Test U-Test with large samples. */
    @Test
    public void testUWithLargeSamples() throws Exception {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[100000];
        double[] y = new double[100000];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
            y[i] = i + 1;
        }
        double expected = 5e7;
        double actual = mwu.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e11);
    }

    /** Test U-Test with a large p-value. */
    @Test
    public void testUWithLargePValue() throws Exception {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[100000];
        double[] y = new double[100000];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
            y[i] = i;
        }
        double expected = 1.0;
        double actual = mwu.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 1e-12);
    }

    /** Test U-Test with an invalid U-value. */
    @Test(expected = IllegalArgumentException.class)
    public void testUWithInvalidUValue() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double U = 10.5;
        assertThrows(IllegalArgumentException.class, () -> {
            mwu.mannWhitneyUTest(-1, U);
        });
    }

    /** Test U-Test with an invalid sample size. */
    @Test(expected = IllegalArgumentException.class)
    public void testUWithInvalidSampleSize() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = {};
        double[] y = { 1.6, 2.4, 4.4, 6.2 };
        assertThrows(IllegalArgumentException.class, () -> {
            mwu.mannWhitneyUTest(x, y);
        });
    }

    private static final TiesStrategy TIESSTRATEGY_DENSE_RANKS = new TiesStrategy() {
        @Override
        public double rank(double d) {
            return FastMath.floor(rank(d + 0.0) + 0.5);
        }
    };

    private static final NaNStrategy NASTRATEGY_LAST = new NaNStrategy() {
        @Override
        public double rank(double d) {
            if (Double.isNaN(d)) {
                return Double.POSITIVE_INFINITY;
            } else {
                return super.rank(d);
            }
        }
    };

}