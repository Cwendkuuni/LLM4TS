package org.apache.commons.math3.stat.inference.test;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        final double[] x = {1.0, 2.0, 3.0};
        final double[] y = {4.0, 5.0, 5.0};

        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double result = test.mannWhitneyU(x, y);
        final double expected = 6.0;
        Assert.assertEquals(expected, result, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest()}.
     */
    @Test
    public void testDefaultConstructor() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[]{1.0, 2.0, 3.0}, new double[]{4.0, 5.0, 5.0});
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testCustomConstructor() {
        final MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.MEDIAN, TiesStrategy.DUPLICATE_AVERAGE);
        test.mannWhitneyUTest(new double[]{1.0, 2.0, 3.0}, new double[]{4.0, 4.0, 5.0, 5.0});
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test (expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgException() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(null, new double[]{1.0, 2.0, 3.0});
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test (expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(new double[0], new double[]{1.0, 2.0, 3.0});
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithZeroLengthArrays() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.mannWhitneyUTest(new double[0], new double[0]);
            Assert.fail();
        } catch (NullArgumentException | NoDataException e) {
            // expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test (expected = ConvergenceException.class)
    public void testMannWhitneyUTestConvergenceException() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[]{Double.NaN, Double.NaN, Double.NaN}, new double[]{Double.NaN, Double.NaN, Double.NaN});
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test (expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestMaxCountExceededException() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[Integer.MAX_VALUE], new double[Integer.MAX_VALUE]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestPositiveValues() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = {1.0, 2.0, 3.0};
        final double[] y = {4.0, 5.0, 5.0};
        final double result = test.mannWhitneyUTest(x, y);
        final double expected = 0.05;
        Assert.assertEquals(expected, result, 0.01);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestNegativeValues() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = {-1.0, -2.0, -3.0};
        final double[] y = {-4.0, -5.0, -5.0};
        final double result = test.mannWhitneyUTest(x, y);
        final double expected = 0.05;
        Assert.assertEquals(expected, result, 0.01);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestEqualValues() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = {1.0, 1.0, 1.0};
        final double[] y = {1.0, 1.0, 1.0};
        final double result = test.mannWhitneyUTest(x, y);
        final double expected = 1.0;
        Assert.assertEquals(expected, result, 0.01);
    }
}