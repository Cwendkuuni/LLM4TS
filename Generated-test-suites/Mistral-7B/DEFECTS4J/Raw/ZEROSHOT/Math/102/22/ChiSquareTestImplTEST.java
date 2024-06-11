package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

/**
 * Test class for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstruction() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assert null != test.getDistribution();
    }

    @Test
    public void testChiSquareTestImplConstructionWithDistribution() {
        ChiSquaredDistributionImpl dist = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertEquals(dist, test.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { -1, 3 };
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidExpected() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { -1.0, 2.0 };
        long[] observed = new long[] { 1, 3 };
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidObservedLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 1 };
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidExpectedLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 2, 2 };
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 2, 3 };
        double result = test.chiSquare(expected, observed);
        assertEquals(3.0, result, 1e-10);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 2, 3 };
        double alpha = 0.05;
        double result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result < alpha);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 2, 3 };
        test.chiSquareTest(expected, observed, -1.0);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestNegativeAlpha() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 2, 3 };
        test.chiSquareTest(expected, observed, -0.01);
    }

    @Test
    public void testChiSquareTestLargeAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 2, 3 };
        double alpha = 0.6;
        double result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result > alpha);
    }

    @Test
    public void testChiSquareTestSmallAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 2, 3 };
        double alpha = 0.01;
        double result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result < alpha);
    }

    @Test
    public void testChiSquareTestWithDifferentDistribution() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 2, 3 };
        double alpha = 0.05;
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(2.0);
        test.setDistribution(dist);
        double result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result < alpha);
    }

    @Test
    public void testChiSquareTestWithSameDistribution() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0 };
        long[] observed = new long[] { 2, 3 };
        double alpha = 0.05;
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(1.0);
        test.setDistribution(dist);
        double result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result >= alpha);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidObserved1() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { -1 };
        long[] observed2 = new long[] { 2, 3 };
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidObserved2() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2, 3 };
        long[] observed2 = new long[] { -1 };
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidObservedLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2 };
        long[] observed2 = new long[] { 2, 3 };
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidExpectedLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][] { new long[] { 2, 3 }, new long[] { 1 } };
        test.chiSquareTestDataSetsComparison(counts);
    }

    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2, 3 };
        long[] observed2 = new long[] { 3, 2 };
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(1.0, result, 1e-10);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2, 3 };
        long[] observed2 = new long[] { 3, 2 };
        double alpha = 0.05;
        double result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result < alpha);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2, 3 };
        long[] observed2 = new long[] { 3, 2 };
        test.chiSquareTestDataSetsComparison(observed1, observed2, -1.0);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestDataSetsComparisonNegativeAlpha() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2, 3 };
        long[] observed2 = new long[] { 3, 2 };
        test.chiSquareTestDataSetsComparison(observed1, observed2, -0.01);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonLargeAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2, 3 };
        long[] observed2 = new long[] { 3, 2 };
        double alpha = 0.6;
        double result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result > alpha);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonSmallAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2, 3 };
        long[] observed2 = new long[] { 3, 2 };
        double alpha = 0.01;
        double result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result < alpha);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithDifferentDistribution() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2, 3 };
        long[] observed2 = new long[] { 3, 2 };
        double alpha = 0.05;
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(2.0);
        test.setDistribution(dist);
        double result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result < alpha);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithSameDistribution() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 2, 3 };
        long[] observed2 = new long[] { 3, 2 };
        double alpha = 0.05;
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(1.0);
        test.setDistribution(dist);
        double result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result >= alpha);
    }

}