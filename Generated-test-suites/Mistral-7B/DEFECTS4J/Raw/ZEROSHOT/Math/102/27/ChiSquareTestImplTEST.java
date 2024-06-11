package org.apache.commons.math.stat.inference;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.distribution.DistributionFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl} class
 */
public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImpl() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        Assert.assertNotNull(test);
    }

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        Assert.assertNotNull(test);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplConstructorInvalidArguments() {
        new ChiSquareTestImpl(null);
    }

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 3};
        try {
            double result = test.chiSquare(expected, observed);
            Assert.assertEquals(2.0, result, 0.001);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidArguments() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0};
        long[] observed = new long[]{1, 3, 2};
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTest() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 3};
        double pValue = test.chiSquareTest(expected, observed);
        Assert.assertEquals(0.1329, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestInvalidArguments() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0};
        long[] observed = new long[]{1, 3, 2};
        test.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestWithAlpha() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 3};
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidAlpha() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 3};
        test.chiSquareTest(expected, observed, 1.2);
    }

    @Test
    public void testChiSquareLongArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 1}, {3, 1, 2}};
        try {
            double result = test.chiSquare(counts);
            Assert.assertEquals(7.0, result, 0.001);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareLongArrayInvalidArguments() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {2, 3, 3}};
        test.chiSquare(counts);
    }

    @Test
    public void testChiSquareTestLongArray() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 1}, {3, 1, 2}};
        double pValue = test.chiSquareTest(counts);
        Assert.assertEquals(0.1329, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestLongArrayInvalidArguments() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {2, 3, 3}};
        test.chiSquareTest(counts);
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{3, 2, 1};
        try {
            double result = test.chiSquareDataSetsComparison(observed1, observed2);
            Assert.assertEquals(2.0, result, 0.001);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidArguments() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{3, 2, 1, 1};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{3, 2, 1};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertEquals(0.1329, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidArguments() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{3, 2, 1, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{3, 2, 1};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidAlpha() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{3, 2, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2, 1.2);
    }
}