package org.apache.commons.math3.stat.inference.test;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        final double[] x = {1.0, 2.0, 2.5, 3.0};
        final double[] y = {2.5, 3.0, 3.5};

        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(7.5, result, 1e-10);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest()}.
     */
    @Test
    public void testDefaultConstructor() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testCustomConstructor() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaNStrategy.REMOVE, TiesStrategy.HIGHEST);
        assertNotNull(mannWhittyUTest);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws NullArgumentException,
            NoDataException, ConvergenceException, MaxCountExceededException {

        final double[] x = {1.0, 2.0, 2.5, 3.0};
        final double[] y = {2.5, 3.0, 3.5};

        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.3125, pValue, 1e-10);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testNullArrays() {
        try {
            final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(null, null);
            fail();
        } catch (NullArgumentException e) {
            // expected
        }

        try {
            final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(null, new double[0]);
            fail();
        } catch (NoDataException e) {
            // expected
        }

        try {
            final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(new double[0], null);
            fail();
        } catch (NoDataException e) {
            // expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testEmptyArrays() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double pValue = mannWhitneyUTest.mannWhitneyUTest(
                new double[0], new double[0]);
        assertEquals(1.0, pValue, 1e-10);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testIdenticalArrays() {
        final double[] x = {1.0, 2.0, 2.5, 3.0, 3.0};
        final double[] y = {2.5, 3.0, 3.5, 3.0, 3.0};

        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testSameLengthArraysWithIdenticalValues() {
        final double[] x = {1.0, 2.0, 2.5, 3.0};
        final double[] y = {2.5, 2.5, 3.0, 3.0};

        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(1.0 / 4.0, pValue, 1e-10);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testOneSample() {
        final double[] x = {1.0, 1.0, 1.0, 1.0, 1.0};
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double pValue = mannWhitneyUTest.mannWhitneyUTest(x, new double[0]);
        assertEquals(0.0, pValue, 1e-10);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testOneSampleWithNaN() {
        final double[] x = {1.0, 1.0, 1.0, 1.0, Double.NaN};
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest. MannWhitneyUTest(x, new double[0]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}