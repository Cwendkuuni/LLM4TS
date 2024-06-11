package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Before;
import org.junit.Test;

public class ChiSquareTestImplTest {

    ChiSquareTestImpl testImpl;

    @Before
    public void setUp() {
        testImpl = new ChiSquareTestImpl();
    }

    @Test
    public void testConstructor() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, chiSquareTest.getDistribution());
    }

    @Test
    public void testChiSquare_1_array() throws IllegalArgumentException {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double chiSquare = testImpl.chiSquare(expected, observed);
        double expectedChiSquare = 2.0 * (Math.pow(2.0 - 1.5, 2.0) / 1.5 +
                Math.pow(3.0 - 2.5, 2.0) / 2.5);
        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_2_invalid_length_expected() throws IllegalArgumentException {
        double[] expected = {1.0};
        long[] observed = {1, 2, 3};
        testImpl.chiSquare(expected, observed);
        // exception should be thrown here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_3_invalid_length_observed() throws IllegalArgumentException {
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2};
        testImpl.chiSquare(expected, observed);
        // exception should be thrown here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_4_invalid_length_mismatch() throws IllegalArgumentException {
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2, 3};
        testImpl.chiSquare(expected, observed);
        // exception should be thrown here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_5_negative_expected() throws IllegalArgumentException {
        double[] expected = {-1.0, 2.0};
        long[] observed = {1, 2};
        testImpl.chiSquare(expected, observed);
        // exception should be thrown here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_6_negative_observed() throws IllegalArgumentException {
        double[] expected = {1.0, 2.0};
        long[] observed = {-1, 2};
        testImpl.chiSquare(expected, observed);
        // exception should be thrown here
    }

    @Test
    public void testChiSquare_7_chiSquareTest() throws IllegalArgumentException, MathException {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double pValue = testImpl.chiSquareTest(expected, observed);
        ChiSquaredDistribution chiSquareDistribution = new ChiSquaredDistributionImpl(2.0);
        double df = 1.0;
        double expectedPValue = 1.0 - chiSquareDistribution.cumulativeProbability(2.0);
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest_1_invalid_length_expected() throws IllegalArgumentException, MathException {
        double[] expected = {1.0};
        long[] observed = {1, 2, 3};
        testImpl.chiSquareTest(expected, observed);
        // exception should be thrown here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest_2_invalid_length_observed() throws IllegalArgumentException, MathException {
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2};
        testImpl.chiSquareTest(expected, observed);
        // exception should be thrown here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest_3_invalid_length_mismatch() throws IllegalArgumentException, MathException {
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2, 3};
        testImpl.chiSquareTest(expected, observed);
        // exception should be thrown here
    }

    @Test
    public void testChiSquareTest_4_alpha_greater_than_0_5() throws IllegalArgumentException, MathException {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double alpha = 0.6;
        boolean result = testImpl.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTest_5_alpha_less_than_0() throws IllegalArgumentException, MathException {
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double alpha = -0.1;
        try {
            testImpl.chiSquareTest(expected, observed, alpha);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquare_2D_array() throws IllegalArgumentException {
        long[][] observed = {{1, 2}, {3, 4}};
        double chiSquare = testImpl.chiSquare(observed);
        double expectedChiSquare = Math.pow(Math.sqrt(6.0) / 2, 2) *
                (Math.pow(1.5, 2.0) / 1.5 + Math.pow(2.5, 2.0) / 2.5 +
                Math.pow(3.5, 2.0) / 3.5 + Math.pow(4.5, 2.0) / 4.5);
        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare_2D_array_invalid_length() throws IllegalArgumentException {
        long[][] observed = {{1}};
        testImpl.chiSquare(observed);
        // exception should be thrown here
    }

    @Test
    public void testChiSquareTest_2D_array() throws IllegalArgumentException, MathException {
        long[][] observed = {{1, 2}, {3, 4}};
        double pValue = testImpl.chiSquareTest(observed);
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(6.0);
        double df = 4.0;
        double expectedPValue = 1.0 - chiSquaredDistribution.cumulativeProbability(4.0);
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest_2D_array_invalid_length() throws IllegalArgumentException, MathException {
        long[][] observed = {{1}};
        testImpl.chiSquareTest(observed);
        // exception should be thrown here
    }

    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 3, 4, 5};
        double chiSquare = testImpl.chiSquareDataSetsComparison(observed1, observed2);
        double expectedChiSquare = Math.pow(Math.sqrt(5.0) / 2, 2) *
                (Math.pow(0.5, 2.0) / 1.5 + Math.pow(1.0, 2.0) / 3.0 +
                Math.pow(1.0, 2.0) / 3.0 + Math.pow(2.0, 2.0) / 4.0 +
                Math.pow(3.0, 2.0) / 5.0 + Math.pow(3.0, 2.0) / 5.0 +
                Math.pow(4.0, 2.0) / 5.0 + Math.pow(5.0, 2.0) / 5.0);
        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparison_invalid_length() throws IllegalArgumentException {
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3, 3};
        testImpl.chiSquareDataSetsComparison(observed1, observed2);
        // exception should be thrown here
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 3, 4, 5};
        double pValue = testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(5.0);
        double df = 4.0;
        double expectedPValue = 1.0 - chiSquaredDistribution.cumulativeProbability(4.0);
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparison_invalid_length() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3, 3};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        // exception should be thrown here
    }

    @Test
    public void testChiSquareTestDataSetsComparison_alpha_less_than_0() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 3, 4, 5};
        double alpha = -0.1;
        try {
            testImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}