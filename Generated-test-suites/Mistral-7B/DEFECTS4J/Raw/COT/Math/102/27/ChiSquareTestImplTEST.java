package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl testImpl;
    private ChiSquaredDistribution distribution;

    @Before
    public void setUp() {
        testImpl = new ChiSquareTestImpl();
        distribution = new ChiSquaredDistributionImpl(1.0);
    }

    @After
    public void tearDown() {
        testImpl = null;
        distribution = null;
    }

    @Test
    public void testChiSquare() {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};

        try {
            double chiSquareStatistic = testImpl.chiSquare(expected, observed);
            Assert.assertEquals(" chi-square test statistic is not correct.", 3.602, chiSquareStatistic, 0.001);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInput() {
        double[] expected = {};
        long[] observed = {1};

        try {
            testImpl.chiSquare(expected, observed);
            Assert.fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testChiSquareTest() throws MathRuntimeException {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};

        double pValue = testImpl.chiSquareTest(expected, observed);
        Assert.assertEquals(" p-value is not correct.", 0.0912, pValue, 0.001);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInput() {
        double[] expected = {1.0, 2.0};
        long[] observed = {};

        try {
            testImpl.chiSquareTest(expected, observed);
            Assert.fail("Expected MathIllegalArgumentException.");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testChiSquareTestWithSignificantLevel() throws MathRuntimeException {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double significanceLevel = 0.05;

        boolean hypothesisRejected = testImpl.chiSquareTest(expected, observed, significanceLevel);
        Assert.assertTrue("Hypothesis was not rejected when it should have been.", hypothesisRejected);
    }

    @Test
    public void testChiSquareTestWithNonSignificantLevel() throws MathRuntimeException {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double significanceLevel = 0.1;

        boolean hypothesisRejected = testImpl.chiSquareTest(expected, observed, significanceLevel);
        Assert.assertFalse("Hypothesis was rejected when it should not have been.", hypothesisRejected);
    }

    @Test
    public void testChiSquareWith2WayTable() {
        long[][] counts = {{1, 2}, {3, 4}};

        try {
            double chiSquareStatistic = testImpl.chiSquare(counts);
            Assert.assertEquals(" chi-square test statistic is not correct.", 6.0, chiSquareStatistic, 0.001);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalid2WayTable() {
        long[][] counts = {{1, 2}, {3}};

        try {
            testImpl.chiSquare(counts);
            Assert.fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testChiSquareTestWith2WayTable() throws MathRuntimeException {
        long[][] counts = {{1, 2}, {3, 4}};

        double pValue = testImpl.chiSquareTest(counts);
        Assert.assertEquals(" p-value is not correct.", 0.0147, pValue, 0.001);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestWithInvalid2WayTableInput() {
        long[][] counts = {{1, 2}, {3}};

        try {
            testImpl.chiSquareTest(counts);
            Assert.fail("Expected MathIllegalArgumentException.");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathRuntimeException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};

        double pValue = testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertEquals(" p-value is not correct.", 0.0663, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidInput() {
        long[] observed1 = {1, 2};
        long[] observed2 = {};

        try {
            testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
            Assert.fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithSignificantLevel() throws MathRuntimeException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};
        double significanceLevel = 0.05;

        boolean hypothesisRejected = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        Assert.assertTrue("Hypothesis was not rejected when it should have been.", hypothesisRejected);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithNonSignificantLevel() throws MathRuntimeException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};
        double significanceLevel = 0.1;

        boolean hypothesisRejected = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        Assert.assertFalse("Hypothesis was rejected when it should not have been.", hypothesisRejected);
    }
}