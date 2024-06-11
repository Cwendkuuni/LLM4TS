package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

/**
 * Test cases for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.5, 2.5, 3.5, 4.5};
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double result = MannWhitneyUTest.mannWhitneyU(x, y);
        double expected = 6.0;
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.5, 2.5, 3.5, 4.5};
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double result = MannWhitneyUTest.mannWhitneyUTest(x, y);
        double expected = 0.03663; // Approximately
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * with null arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArguments() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        MannWhitneyUTest.mannWhitneyUTest(null, null);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * with zero-length arguments.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroLength() {
        double[] x = new double[0];
        double[] y = new double[0];
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        MannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * with different sizes.
     */
    @Test
    public void testMannWhitneyUTestDifferentSizes() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.5, 2.5, 3.5};
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(IllegalArgumentException.class, () -> {
            MannWhitneyUTest.mannWhitneyUTest(x, y);
        });
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArguments() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        MannWhitneyUTest.ensureDataConformance(null, null);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     * with zero-length arguments.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceZeroLength() {
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        MannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.5, 2.5, 3.5, 4.5};
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double[] result = MannWhitneyUTest.concatenateSamples(x, y);
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0, 1.5, 2.5, 3.5, 4.5};
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @Test
    public void testCalculateAsymptoticPValue() {
        double Umin = 15.0;
        int n1 = 5;
        int n2 = 7;
        MannWhitneyUTest MannWhitneyUTest = new MannWhitneyUTest();
        double result = MannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        double expected = 0.0463; // Approximately
        assertEquals(expected, result, 0.001);
    }
}