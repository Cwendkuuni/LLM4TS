package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4};
        double[] expected = {3.0, 2.5, 1.5, 3.5};
        try {
            double chiSquareStatistic = test.chiSquare(expected, observed);
            double pValue = test.chiSquareTest(expected, observed);
            Assert.assertEquals("chiSquareStatistic wrong", 6.67236842105263, chiSquareStatistic, 0.001);
            Assert.assertEquals("pValue wrong", 0.01421357, pValue, 0.0001);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        observed = new long[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        expected = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        try {
            test.chiSquare(expected, observed);
            test.chiSquareTest(expected, observed);
            Assert.fail("Expected IllegalArgumentException with message 'observed, expected array lengths incorrect'");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Expected message: 'observed, expected array lengths incorrect'", "observed, expected array lengths incorrect", e.getMessage());
        }

        observed = new long[]{2, 3, 1, 4};
        expected = new double[]{5.0, 5.5, 1.5, 4.5};
        try {
            test.chiSquareTest(expected, observed);
            Assert.fail("Expected MathException with message");
        } catch (MathException e) {
            Assert.assertEquals("Expected message: 'Invalid value for degree of freedom: -1.0' or similar", "Invalid value for degree of freedom: -1.0", e.getMessage());
        }
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2};
        long[] observed2 = {3, 1, 2, 2};
        try {
            double chiSquareStatistic = test.chiSquareDataSetsComparison(observed1, observed2);
            double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
            Assert.assertEquals("chiSquareStatistic wrong", 2.0, chiSquareStatistic, 0.001);
            Assert.assertEquals("pValue wrong", 0.25, pValue, 0.0001);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        observed1 = new long[]{2, 3, 1, 4};
        observed2 = new long[]{3, 2, 1, 3};
        try {
            test.chiSquareTestDataSetsComparison(observed1, observed2);
            Assert.fail("Expected IllegalArgumentException with message 'observed1, observed2 array lengths incorrect'");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Expected message: 'observed1, observed2 array lengths incorrect'", "observed1, observed2 array lengths incorrect", e.getMessage());
        }

        observed1 = new long[]{2, 3, 1, 4};
        observed2 = new long[]{3, 2, 1, 3};
        try {
            test.chiSquareDataSetsComparison(observed1, observed2);
            Assert.fail("Expected IllegalArgumentException with message 'observed counts must both be non-zero'");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Expected message: 'observed counts must both be non-zero'", "observed counts must both be non-zero", e.getMessage());
        }
    }
}