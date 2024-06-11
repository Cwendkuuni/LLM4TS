package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test chiSquare method with positive expected and observed counts.
     */
    @Test
    public void testChiSquarePositive() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed = {10, 15};
        double[] expected = {12.0, 13.0};

        try {
            // Test method
            double stat = test.chiSquare(expected, observed);
            double pValue = test.chiSquareTest(expected, observed);

            // Verify results
            Assert.assertEquals(9.21034, stat, 0.001); // Chi-square statistic
            Assert.assertEquals(0.02911, pValue, 0.001); // p-value
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquare method with incorrect length expected and observed arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidLength() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed = {10};
        double[] expected = {12.0, 13.0, 14.0};

        // Test method
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with incorrect length observed array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidObservedLength() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed = {10, 15, 16};
        double[] expected = {12.0, 13.0};

        // Test method
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with negative expected and/or observed counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareNegative() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed = {-5, 15};
        double[] expected = {12.0, -7.0};

        // Test method
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with positive expected and observed counts.
     */
    @Test
    public void testChiSquareTestPositive() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed = {10, 15};
        double[] expected = {12.0, 13.0};

        try {
            // Test method
            double pValue = test.chiSquareTest(expected, observed);

            // Verify result
            Assert.assertEquals(0.02911, pValue, 0.001); // p-value
        } catch (IllegalArgumentException | MathException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTest method with incorrect length expected array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidExpectedLength() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed = {10, 15};
        double[] expected = {12.0, 13.0, 14.0};

        // Test method
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with incorrect length observed array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidObservedLength() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed = {10, 15, 16};
        double[] expected = {12.0, 13.0};

        // Test method
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with incorrect expected and observed arrays sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidSizes() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed = {10};
        double[] expected = {12.0, 13.0};

        // Test method
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with negative expected and/or observed counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNegative() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed = {-5, 15};
        double[] expected = {12.0, -7.0};

        // Test method
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with null expected and observed arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNull() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Test method
        test.chiSquareTest(null, null);
    }

    /**
     * Test chiSquareDataSetsComparison method with positive observations and expected counts.
     */
    @Test
    public void testChiSquareDataSetsComparisonPositive() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed1 = {5, 7, 8};
        long[] observed2 = {5, 7, 9};

        try {
            // Test method
            double stat = test.chiSquareDataSetsComparison(observed1, observed2);
            double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);

            // Verify results
            Assert.assertEquals(1.9048, stat, 0.001); // Chi-square statistic
            Assert.assertEquals(0.1327, pValue, 0.001); // p-value
        } catch (IllegalArgumentException | MathException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareDataSetsComparison method with incorrect observation arrays sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidObservationsSizes() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Prepare test data
        long[] observed1 = {5};
        long[] observed2 = {5, 7};

        // Test method
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareDataSetsComparison method with null observations arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonNull() {
        // Initialize test object
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        // Test method
        test.chiSquareDataSetsComparison(null, null);
    }
}