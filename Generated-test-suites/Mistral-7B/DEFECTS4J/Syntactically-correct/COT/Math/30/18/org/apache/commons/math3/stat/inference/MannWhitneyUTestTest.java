package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.StatFunctions;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

/**
 * Test class for MannWhitneyUTest
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest()
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test constructor MannWhitneyUTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy)
     */
    @Test
    public void testConstructorWithStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                new NaNStrategy() {
                    @Override
                    public double naValue() {
                        return Double.NaN;
                    }
                }, new TiesStrategy() {
                    @Override
                    public int getRank(int index, double value, double[] data) {
                        return index + 1;
                    }
                });
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method ensureDataConformance
     * @throws Exception
     */
    @Test
    public void testEnsureDataConformance() throws Exception {
        double[] x = new double[0];
        double[] y = new double[0];
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.ensureDataConformance(x, y);
            fail("Expected NullArgumentException or NoDataException");
        } catch (NullArgumentException e) {
            // Expected
        } catch (NoDataException e) {
            // Expected
        }

        x = null;
        y = new double[0];
        try {
            mannWhitneyUTest.ensureDataConformance(x, y);
            fail("Expected NullArgumentException or NoDataException");
        } catch (NullArgumentException e) {
            // Expected
        } catch (NoDataException e) {
            // Expected
        }

        x = new double[1];
        y = new double[1];
        mannWhitneyUTest.ensureDataConformance(x, y);

        x = new double[]{1.0};
        y = new double[]{1.0};
        mannWhitneyUTest.ensureDataConformance(x, y);

        x = new double[]{1.0};
        y = new double[]{2.0};
        mannWhitneyUTest.ensureDataConformance(x, y);

        x = new double[]{1.0, 1.0};
        y = new double[]{1.0, 1.0};
        mannWhitneyUTest.ensureDataConformance(x, y);

        x = new double[]{1.0, 1.0};
        y = new double[]{2.0, 2.0};
        mannWhitneyUTest.ensureDataConformance(x, y);

        x = new double[]{1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0};
        mannWhitneyUTest.ensureDataConformance(x, y);

        x = new double[]{1.0, 1.0, 1.0};
        y = new double[]{2.0, 2.0, 2.0};
        mannWhitneyUTest.ensureDataConformance(x, y);

        x = new double[]{1.0, 1.0, 1.0};
        y = new double[]{1.0, 2.0, 2.0};
        mannWhitneyUTest.ensureDataConformance(x, y);

        x = new double[]{1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 2.0, 3.0, 3.0};
        mannWhitneyUTest.ensureDataConformance(x, y);
    }

    /**
     * Test method mannWhitneyU
     * @throws Exception
     */
    @Test
    public void testMannWhitneyU() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{3.0, 2.0, 1.0};
        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, result, 0.0);

        // Test with equal samples
        x = new double[]{1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0};
        result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, result, 0.0);

        // Test with no data
        x = new double[]{};
        y = new double[]{};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null data
        x = null;
        y = new double[]{};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with null second sample
        x = new double[]{};
        y = null;
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with different lengths
        x = new double[]{1.0, 2.0};
        y = new double[]{3.0, 3.0, 3.0};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method calculateAsymptoticPValue
     * @throws Exception
     */
    @Test
    public void testCalculateAsymptoticPValue() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double Umin = 0.0;
        int n1 = 5;
        int n2 = 5;
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(1.0, pValue, 0.0);

        Umin = 0.5 * n1 * n2;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(1.0 / (2.0 * FastMath.PI * n1 * n2 * (n1 + n2 + 1) / 12.0),
                pValue, 0.01);

        // Test with MaxCountExceededException
        Umin = 0.5 * n1 * n2;
        mannWhitneyUTest.setMaxIterations(0);
        try {
            mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    /**
     * Test method mannWhitneyUTest
     * @throws Exception
     */
    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{3.0, 2.0, 1.0};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.05, pValue, 0.01);

        // Test with equal samples
        x = new double[]{1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 0.0);

        // Test with no data
        x = new double[]{};
        y = new double[]{};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null data
        x = null;
        y = new double[]{};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with null second sample
        x = new double[]{};
        y = null;
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with different lengths
        x = new double[]{1.0, 2.0};
        y = new double[]{3.0, 3.0, 3.0};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with infinite pValue
        x = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        y = new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertTrue(Precision.isClose(1.0, pValue, 0.01));

        // Test with zero pValue
        x = new double[]{0.0, 0.0};
        y = new double[]{1.0, 1.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertTrue(Precision.isClose(0.0, pValue, 0.01));

        // Test with pValue close to 1
        x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertTrue(Precision.isClose(1.0, pValue, 0.01));

        // Test with pValue close to 0
        x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        y = new double[]{2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertTrue(Precision.isClose(0.0, pValue, 0.01));

        // Test with ConvergenceException
        mannWhitneyUTest.setMaxIterations(1);
        x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        y = new double[]{2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with MaxCountExceededException
        mannWhitneyUTest.setMaxIterations(Integer.MAX_VALUE);
        x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        y = new double[]{2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0};
        mannWhitneyUTest.setMaxIterations(0);
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }
}