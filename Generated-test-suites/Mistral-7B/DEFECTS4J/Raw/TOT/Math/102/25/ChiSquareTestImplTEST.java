package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        long[] observed = {10, 15, 5, 12};
        double[] expected = {15.0, 10.0, 5.0, 12.0};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        try {
            double actual = test.chiSquare(expected, observed);
            assertEquals(2.9999999999999995, actual, 0.0000001);
            long[][] counts = {{10, 15}, {5, 12}};
            actual = test.chiSquare(counts);
            assertEquals(2.9999999999999995, actual, 0.0000001);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidObserved() {
        long[] observed = {1, 1};
        double[] expected = {1.0, 1.0};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidExpected() {
        long[] observed = {1, 1};
        double[] expected = {1.0, 0.0};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTest() {
        long[] observed = {10, 15, 5, 12};
        double[] expected = {15.0, 10.0, 5.0, 12.0};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        try {
            double actual = test.chiSquareTest(expected, observed);
            assertEquals(0.0261, actual, 0.0001);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidObserved() {
        long[] observed = {1, 1};
        double[] expected = {1.0, 1.0};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidExpected() {
        long[] observed = {1, 1};
        double[] expected = {1.0, 0.0};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestAlpha() {
        long[] observed = {10, 15, 5, 12};
        double[] expected = {15.0, 10.0, 5.0, 12.0};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        try {
            boolean actual = test.chiSquareTest(expected, observed, alpha);
            assertFalse(actual);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testChiSquareDataSetComparison() {
        long[] observed1 = {10, 15};
        long[] observed2 = {5, 12};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        try {
            double actual = test.chiSquareDataSetsComparison(observed1, observed2);
            assertEquals(0.8807976489053693, actual, 0.0000001);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetComparisonInvalidObserved1() {
        long[] observed1 = {1, 1};
        long[] observed2 = {5, 12};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetComparisonInvalidObserved2() {
        long[] observed1 = {10, 15};
        long[] observed2 = {1, 1};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetComparison() {
        long[] observed1 = {10, 15};
        long[] observed2 = {5, 12};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        try {
            double actual = test.chiSquareTestDataSetsComparison(observed1, observed2);
            assertEquals(0.015679893832984643, actual, 0.0000001);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testChiSquareTestDataSetComparisonAlpha() {
        long[] observed1 = {10, 15};
        long[] observed2 = {5, 12};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        try {
            boolean actual = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
            assertFalse(actual);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}