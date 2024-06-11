package org.apache.commons.math3.stat.inference.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.Ranking;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /** Ranking algorithm. */
    private final Ranking naturalRanking;

    /**
     * Create a test instance using where NaN's are left in place and ties get
     * the average of applicable ranks.
     */
    public MannWhitneyUTestTest() {
        this.naturalRanking = new NaturalRanking(NaNStrategy.FIXED,
                TiesStrategy.AVERAGE);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        final double[] x = {1.0, 2.0, 3.0, 4.0};
        final double[] y = {2.5, 3.5, 5.0, 6.0};
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(7.0, mannWhitneyU);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}
     * with custom strategies.
     */
    @Test
    public void testCustomMannWhitneyUTest() {
        final double[] x = {1.0, 2.0, 3.0, 4.0};
        final double[] y = {2.5, 3.5, 5.0, 6.0};
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                TiesStrategy.LOW, TiesStrategy.DENSE);
        final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(7.0, mannWhitneyU);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test
    public void testEnsureDataConformance() {
        final double[] x = null;
        final double[] y = {2.5, 3.5, 5.0, 6.0};
        assertThrows(NullArgumentException.class,
                () -> new MannWhitneyUTest().ensureDataConformance(x, y));

        final double[] emptyX = {};
        final double[] y = {2.5, 3.5, 5.0, 6.0};
        assertThrows(NoDataException.class,
                () -> new MannWhitneyUTest().ensureDataConformance(emptyX, y));

        final double[] x = {1.0, 2.0, 3.0, 4.0};
        final double[] emptyY = {};
        assertThrows(NoDataException.class,
                () -> new MannWhitneyUTest().ensureDataConformance(x, emptyY));

        final double[] x = {1.0, 2.0, 3.0, 4.0};
        final double[] y = null;
        assertThrows(NullArgumentException.class,
                () -> new MannWhitneyUTest().ensureDataConformance(x, y));
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        final double[] x = {1.0, 2.0, 3.0};
        final double[] y = {4.0, 5.0, 6.0};
        final double[] z = new MannWhitneyUTest().concatenateSamples(x, y);
        assertEquals(6, z.length);
        assertEquals(1.0, z[0]);
        assertEquals(1.0, z[1]);
        assertEquals(1.0, z[2]);
        assertEquals(4.0, z[3]);
        assertEquals(4.0, z[4]);
        assertEquals(4.0, z[5]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     * Test with tied values.
     */
    @Test
    public void testMannWhitneyUTiedValues() {
        final double epsilon = 1e-9;
        final double[] x = {1.0, 2.0, 2.0, 3.0, 3.0};
        final double[] y = {2.5, 3.5, 3.5, 3.75, 4.0};
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(5.5, mannWhitneyU, epsilon);
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}
     */
    @Test
    public void testCalculateAsymptoticPValue() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double Umin = 5;
        final int n1 = 3;
        final int n2 = 4;
        final double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.0148, pValue, 0.0001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * Test with zero-length arrays.
     */
    @Test
    public void testZeroLengthArrays() {
        final double[] x = {};
        final double[] y = {};
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NoDataException.class,
                () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * Test with NaN values in samples.
     */
    @Test
    public void testNaNValues() {
        final double[] x = {1.0, Double.NaN, 2.0};
        final double[] y = {2.5, 3.5, Double.NaN};
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(ConvergenceException.class,
                () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * Test with infinite values in samples.
     */
    @Test
    public void testInfiniteValues() {
        final double[] x = {1.0, Double.POSITIVE_INFINITY, 2.0};
        final double[] y = {2.5, Double.NEGATIVE_INFINITY, 3.5};
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(ConvergenceException.class,
                () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}
     * with max iterations.
     */
    @Test
    public void testCalculateAsymptoticPValueMaxIterations() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double Umin = 5;
        final int n1 = 3;
        final int n2 = 4;
        assertThrows(MaxCountExceededException.class,
                () -> mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2, 1));
    }
}