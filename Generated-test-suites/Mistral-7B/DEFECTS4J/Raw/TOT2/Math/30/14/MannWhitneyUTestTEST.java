package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test for {@link MannWhitneyUTest} class.
 *
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()} .
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        // No specific assertions to be made here
    }

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest(org.apache.commons.math3.stat.ranking.NaNStrategy, org.apache.commons.math3.stat.ranking.TiesStrategy)} .
     */
    @Test
    public void testConstructorWithSpecifiedStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaNStrategy.LOWEST, TiesStrategy.DITHERING);
        // No specific assertions to be made here
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullArguments() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 4.0, 5.0, 6.0 };
        double[] z = mannWhitneyUTest.concatenateSamples(x, y);

        assertEquals(x.length + y.length, z.length);
        for (int i = 0; i < x.length; ++i) {
            assertEquals(x[i], z[i]);
        }
        for (int i = 0; i < y.length; ++i) {
            assertEquals(y[i], z[x.length + i]);
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 4.0, 5.0, 6.0 };

        double U = mannWhitneyUTest.mannWhitneyU(x, y);

        // The U statistic is equal to the minimum of U1 and U2
        double U1 = 6.0 * 6.0 / 2.0;
        double U2 = 3.0 * 6.0;
        assertEquals(FastMath.min(U1, U2), U, 0.0);

        // Test with empty arrays
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
        try {
            mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }

        // Test with null arrays
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
        try {
            mannWhitneyUTest.mannWhitneyU(null, new double[0]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}
     * with normal approximation.
     */
    @Test
    public void testCalculateAsymptoticPValueNormalApproximation()
        throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 4.0, 5.0, 6.0 };

        double Umin = mannWhitneyUTest.mannWhitneyU(x, y);
        int n1 = x.length;
        int n2 = y.length;

        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        double expectedPValue = 0.0227; // obtained using R

        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws NoDataException,
            NullArgumentException, ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 2.0, 2.0, 2.0, 4.0, 5.0, 6.0 };

        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

        double expectedPValue = 0.0122; // obtained using R

        assertEquals(expectedPValue, pValue, 0.001);
    }

}