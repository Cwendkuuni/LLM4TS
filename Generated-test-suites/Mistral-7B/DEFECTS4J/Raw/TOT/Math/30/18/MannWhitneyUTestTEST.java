package org.apache.commons.math3.stat.inference.test;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Tests {@code mannWhitneyU} method with two empty arrays.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUEmptyArrays() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        mwu.mannWhitneyU(x, y);
    }

    /**
     * Tests {@code mannWhitneyU} method with two null arrays.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullArrays() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;
        mwu.mannWhitneyU(x, y);
    }

    /**
     * Tests {@code mannWhitneyU} method with two arrays of equal length and
     * same elements.
     */
    @Test
    public void testMannWhitneyUSameArrays() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 1.0, 1.0, 1.0};
        double[] y = new double[]{1.0, 1.0, 1.0, 1.0};
        double result = mwu.mannWhitneyU(x, y);
        double expected = 3.0;
        Assert.assertEquals(expected, result, 0.001);
    }

    /**
     * Tests {@code mannWhitneyU} method with two arrays of equal length but
     * different elements.
     */
    @Test
    public void testMannWhitneyUDifferentArrays() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{5.0, 6.0, 7.0, 8.0};
        double result = mwu.mannWhitneyU(x, y);
        double expected = 10.0;
        Assert.assertEquals(expected, result, 0.001);
    }

    /**
     * Tests {@code mannWhitneyU} method with two arrays of different lengths.
     */
    @Test
    public void testMannWhitneyUDifferentLengths() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{5.0, 6.0};
        double result = mwu.mannWhitneyU(x, y);
        double expected = 7.0;
        Assert.assertEquals(expected, result, 0.001);
    }

    /**
     * Tests {@code mannWhitneyUTest} method with two empty arrays.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyArrays() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        mwu.mannWhitneyUTest(x, y);
    }

    /**
     * Tests {@code mannWhitneyUTest} method with two null arrays.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArrays() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;
        mwu.mannWhitneyUTest(x, y);
    }

    /**
     * Tests {@code mannWhitneyUTest} method with two arrays of equal length and
     * same elements.
     */
    @Test
    public void testMannWhitneyUTestSameArrays() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 1.0, 1.0, 1.0};
        double[] y = new double[]{1.0, 1.0, 1.0, 1.0};
        double result = mwu.mannWhitneyUTest(x, y);
        double pValue = 1.0; // expected p-value is 1 for identical arrays
        Assert.assertEquals(pValue, result, 0.001);
    }

    /**
     * Tests {@code mannWhitneyUTest} method with two arrays of equal length but
     * different elements.
     */
    @Test
    public void testMannWhitneyUTestDifferentArrays() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{5.0, 6.0, 7.0, 8.0};
        double result = mwu.mannWhitneyUTest(x, y);
        double pValue = 0.05; // expected p-value is around 0.05 for this example
        Assert.assertTrue(result <= pValue);
    }

    /**
     * Tests {@code mannWhitneyUTest} method with two arrays of different lengths.
     */
    @Test
    public void testMannWhitneyUTestDifferentLengths() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{5.0, 6.0};
        double result = mwu.mannWhitneyUTest(x, y);
        double pValue = 0.01; // expected p-value is around 0.01 for this example
        Assert.assertTrue(result <= pValue);
    }

    /**
     * Tests {@code mannWhitneyUTest} method with arrays containing NaNs.
     */
    @Test
    public void testMannWhitneyUTestNaN() {
        MannWhitneyUTest mwu = new MannWhitneyUTest(NaNStrategy.REMOVE,
                TiesStrategy.MIN);
        double[] x = new double[]{1.0, 2.0, Double.NaN, 4.0};
        double[] y = new double[]{5.0, 6.0, 7.0, Double.NaN};
        try {
            mwu.mannWhitneyUTest(x, y);
        } catch (MaxCountExceededException e) {
            // expected result: test does not converge with the given NaN strategies
        }
    }
}