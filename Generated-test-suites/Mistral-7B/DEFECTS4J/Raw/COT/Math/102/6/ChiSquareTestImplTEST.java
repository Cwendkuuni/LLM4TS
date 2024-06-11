package org.apache.commons.math.stat.inference; // make sure to import any necessary classes

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for ChiSquareTestImpl class
 */
@RunWith(JUnit4.class)
public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        assertEquals("Unexpected instance of ChiSquaredDistribution",
                new ChiSquaredDistributionImpl(1.0), chiSquareTestImpl.getDistribution());
    }

    @Test
    public void testChiSquareTestImplConstructorWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(2.0);
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(distribution);
        assertEquals("Unexpected instance of ChiSquaredDistribution", distribution, chiSquareTestImpl.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplThrowsExceptionOnInvalidObservedOrExpectedLength() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[1];
        long[] observed = new long[2];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplThrowsExceptionOnNonPositiveExpectedValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{-1.0, 2.0};
        long[] observed = new long[]{1, 1};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplThrowsExceptionOnNegativeObservedValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 1.0};
        long[] observed = new long[]{-1, 1};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareMethod() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.0, 3.0};
        long[] observed = new long[]{3, 4};
        double chiSquareValue = chiSquareTestImpl.chiSquare(expected, observed);
        double expectedChiSquareValue = 3.277; // calculated using R software
        assertEquals("Unexpected chi-square value", expectedChiSquareValue, chiSquareValue, 1e-6);
    }

    @Test
    public void testChiSquareTestMethod() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.0, 3.0};
        long[] observed = new long[]{3, 4};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        double expectedPValue = 0.118; // calculated using R software
        assertEquals("Unexpected p-value", expectedPValue, pValue, 1e-6);
    }

    @Test
    public void testChiSquareTestMethodWithSignificanceLevel() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.0, 3.0};
        long[] observed = new long[]{3, 4};
        boolean rejectNullHypothesis = chiSquareTestImpl.chiSquareTest(expected, observed, 0.05);
        assertTrue("Null hypothesis should not be rejected", !rejectNullHypothesis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidSignificanceLevel() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.0, 3.0};
        long[] observed = new long[]{3, 4};
        chiSquareTestImpl.chiSquareTest(expected, observed, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonThrowsExceptionOnInvalidObservedLengths() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[1];
        long[] observed2 = new long[1];
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethod() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 3, 4};
        long[] observed2 = new long[]{3, 3, 3, 4};
        double chiSquareValue = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        double expectedChiSquareValue = 0.578; // calculated using R software
        assertEquals("Unexpected chi-square value", expectedChiSquareValue, chiSquareValue, 1e-6);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithSignificanceLevel() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 3, 4};
        long[] observed2 = new long[]{3, 3, 3, 4};
        boolean rejectNullHypothesis = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue("Null hypothesis should be rejected", rejectNullHypothesis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidSignificanceLevel() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 3, 4};
        long[] observed2 = new long[]{3, 3, 3, 4};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithNonRectangular2WayTable() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {1, 1}, {3}};
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithNonRectangular2WayTable() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2}, {1, 1}, {3}};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithNegativeEntryIn2WayTable() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{-1, 2}, {1, 1}};
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithNegativeEntryIn2WayTable() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{-1, 2}, {1, 1}};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(counts);
    }
}