package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * MannWhitneyUTest class tests all methods in MannWhitneyUTest.java
 */
public class MannWhitneyUTestTest {

    /**
     * Ranking algorithm.
     */
    private NaturalRanking naturalRanking;

    /**
     * Creates a test instance using where NaN's are left in place and ties get
     * the average of applicable ranks.
     */
    @SuppressWarnings("static-method")
    public MannWhitneyUTestTest() {
        naturalRanking = new NaturalRanking(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     * Tests both samples with same length and different length.
     * @throws Exception
     */
    @Test
    public void testMannWhitneyU() throws Exception {
        // Test with same length samples
        double[] sample1 = {1.0, 1.5, 2.0, 2.5, 3.0};
        double[] sample2 = {1.1, 1.6, 2.1, 2.6, 3.1};
        double expected = 6.0;
        double actual = new MannWhitneyUTest().mannWhitneyU(sample1, sample2);
        assertEquals(expected, actual, 0.0);

        // Test with different length samples
        double[] sample3 = {1.0, 1.5, 2.0};
        double[] sample4 = {1.1, 1.6, 2.1, 2.6, 3.1};
        double expected2 = 7.0;
        double actual2 = new MannWhitneyUTest().mannWhitneyU(sample3, sample4);
        assertEquals(expected2, actual2, 0.0);

        // Test with empty samples
        double[] emptySample1 = {};
        double[] emptySample2 = {};
        try {
            new MannWhitneyUTest().mannWhitneyU(emptySample1, emptySample2);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null samples
        double[] nullSample1 = null;
        double[] nullSample2 = null;
        try {
            new MannWhitneyUTest().mannWhitneyU(nullSample1, nullSample2);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with samples containing NaN values
        double[] nanSample1 = {1.0, Double.NaN, 2.0, 2.5, 3.0};
        double[] nanSample2 = {1.1, 1.6, 2.1, 2.6, Double.NaN};
        try {
            new MannWhitneyUTest().mannWhitneyU(nanSample1, nanSample2);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testMannWhitneyUTest() {
        // Test with default strategies
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(mannWhitneyUTest.getNaturalRanking().getNaNStrategy(), NaNStrategy.FIXED);
        assertEquals(mannWhitneyUTest.getNaturalRanking().getTiesStrategy(), TiesStrategy.AVERAGE);

        // Test with custom strategies
        NaNStrategy customNaNStrategy = new NaNStrategy() {
            @Override
            public double rankNaN(double value) {
                return Double.NaN;
            }
        };
        TiesStrategy customTiesStrategy = new TiesStrategy() {
            @Override
            public int getRank(int index, int total, double[] data) {
                return index + 1;
            }
        };
        MannWhitneyUTest mannWhitneyUTestCustom = new MannWhitneyUTest(customNaNStrategy, customTiesStrategy);
        assertEquals(mannWhitneyUTestCustom.getNaturalRanking().getNaNStrategy(), customNaNStrategy);
        assertEquals(mannWhitneyUTestCustom.getNaturalRanking().getTiesStrategy(), customTiesStrategy);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     * @throws Exception
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptySamples() throws Exception {
        double[] emptySample1 = {};
        double[] emptySample2 = {};
        new MannWhitneyUTest().ensureDataConformance(emptySample1, emptySample2);
        fail("Expected NoDataException");
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     * @throws Exception
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullSamples() throws Exception {
        double[] nullSample1 = null;
        double[] nullSample2 = null;
        new MannWhitneyUTest().ensureDataConformance(nullSample1, nullSample2);
        fail("Expected NullArgumentException");
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        double[] sample1 = {1.0, 1.5, 2.0};
        double[] sample2 = {1.1, 1.6, 2.1, 2.6, 3.1};
        double[] expected = {1.0, 1.5, 2.0, 1.1, 1.6, 2.1, 2.6, 3.1};
        double[] actual = new MannWhitneyUTest().concatenateSamples(sample1, sample2);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testCalculateAsymptoticPValue() throws Exception {
        double Umin = 1.0;
        int n1 = 3;
        int n2 = 5;
        double expected = 0.01732953618115101;
        double actual = new MannWhitneyUTest().calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(expected, actual, 0.00001);

        Umin = 10.0;
        n1 = 3;
        n2 = 5;
        expected = 0.9826704638188502;
        actual = new MannWhitneyUTest().calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * @throws Exception
     */
    @Test
    public void testMannWhitneyUTestWithAsymptoticPValue() throws Exception {
        double[] sample1 = {1.0, 1.5, 2.0};
        double[] sample2 = {1.1, 1.6, 2.1, 2.6, 3.1};
        double expectedPValue = 0.01732953618115101;
        double actualPValue = new MannWhitneyUTest().mannWhitneyUTest(sample1, sample2);
        assertEquals(expectedPValue, actualPValue, 0.00001);

        // Test with samples containing NaN values
        double[] nanSample1 = {1.0, Double.NaN, 2.0, 2.5, 3.0};
        double[] nanSample2 = {1.1, 1.6, 2.1, 2.6, Double.NaN};
        try {
            new MannWhitneyUTest().mannWhitneyUTest(nanSample1, nanSample2);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }
    }
}