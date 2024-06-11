package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.getDistribution());
    }

    @Test
    public void testConstructorWithDistribution() {
        ChiSquareDistribution distribution = new ChiSquaredDistributionImpl(1);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, test.getDistribution());
    }

    @Test
    public void testChiSquareWithValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double result = test.chiSquare(expected, observed);
        double expectedResult = 2.0;
        assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testChiSquareWithInvalidInputsLengthMismatch() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2, 3};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareWithInvalidInputsNegativeCounts() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {-1, 3};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareTestWithValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double alpha = 0.05;
        double result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result < alpha);
    }

    @Test
    public void testChiSquareTestWithInvalidInputsInvalidAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTest(expected, observed, -0.1));
    }

    @Test
    public void testChiSquareTestWithValidInputsAndZeroExpected() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {0.0};
        long[] observed = {1, 1};
        double alpha = 0.05;
        boolean result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestWithValidInputsAndZeroObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {0};
        double alpha = 0.05;
        boolean result = test.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 2.665;
        assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithInvalidInputsDifferentLengths() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double alpha = 0.05;
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithInvalidInputsInvalidAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTestDataSetsComparison(observed1, observed2, -0.1));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithZeroObservedForBoth() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {0};
        long[] observed2 = {0};
        double alpha = 0.05;
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(result);
    }

    private void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError();
        }
    }

    private void assertArrayEquals(double[] expected, double[] actual) {
        assertArrayEquals(expected, actual, 0.0);
    }

    private void assertArrayEquals(double[] expected, double[] actual, double delta) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], delta);
        }
    }

    private void assertEquals(Double expected, Double actual) {
        assertEquals(expected.doubleValue(), actual.doubleValue(), 0.0);
    }

    private void assertEquals(double expected, double actual, double delta) {
        double diff = Math.abs(expected - actual);
        assertTrue(diff <= delta);
    }

    private void assertThrows(Class<? extends Throwable> expectedException, Runnable runnable) {
        try {
            runnable.run();
            fail("Expected exception: " + expectedException.getName());
        } catch (Throwable e) {
            if (expectedException.isInstance(e)) {
                return;
            }
            fail("Unexpected exception: " + e.getClass().getName());
        }
    }
}