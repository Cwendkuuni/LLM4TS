package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ChiSquareTestImplTest {

    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.distribution);
        assertEquals(1.0, test.distribution.getDegreesOfFreedom(), 0.01);
    }

    @Test
    public void testChiSquareWithEqualExpectedAndObserved() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 1};
        long[] observed = {1, 1};
        double chiSquareStat = test.chiSquare(expected, observed);
        assertEquals(0.0, chiSquareStat, 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithUnequalExpectedAndObservedLengths() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 1};
        long[] observed = {1};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeExpectedOrObserved() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {-1, 1};
        long[] observed = {1, 1};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNullExpectedOrObserved() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = null;
        long[] observed = {1, 1};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithUnexpectedExpectedOrObservedValues() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 1, 1};
        long[] observed = {1, 1};
        test.chiSquare(expected, observed);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestWithNaNExpectedOrObserved() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, Double.NaN};
        long[] observed = {1, 1};
        test.chiSquareTest(expected, observed);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestWithNaNChiSquareStat() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 1};
        long[] observed = {1, 1};
        double chiSquareStat = Double.NaN;
        test.chiSquareTest(expected, observed, chiSquareStat);
    }

    @Test
    public void testChiSquareTestWithExpectedAndObservedValues() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {2, 1};
        long[] observed = {3, 1};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.045, pValue, 1e-6);
    }

    @Test
    public void testChiSquareTestWithAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {2, 1};
        long[] observed = {3, 1};
        boolean rejectNullHypothesis = test.chiSquareTest(expected, observed, 0.05);
        assertFalse(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithEqualObservedValues() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 1};
        long[] observed2 = {1, 1};
        double chiSquareStat = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.0, chiSquareStat, 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithUnequalObservedLengths() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 1};
        long[] observed2 = {1};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithExpectedAndObservedValues()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 1};
        long[] observed2 = {3, 1};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.045, pValue, 1e-6);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 1};
        long[] observed2 = {3, 1};
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestWithEmptyObservedOrExpectedArrays() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {};
        long[] observed = {};
        try {
            test.chiSquareTest(expected, observed);
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
    }

    @Test
    public void testChiSquareWithEmptyExpectedArray() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {};
        long[] observed = {1, 1};
        try {
            test.chiSquare(expected, observed);
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
    }

    @Test
    public void testChiSquareTestWithEmptyObservedArray() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 1};
        long[] observed = {};
        try {
            test.chiSquareTest(expected, observed);
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
    }

    @Test
    public void testChiSquareWithTwoWayTable() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        double chiSquareStat = test.chiSquare(counts);
        assertEquals(2.0, chiSquareStat, 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithTwoWayTableWithNegativeValues() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, -2}, {2, 1}};
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithTwoWayTableWithNonRectangularArray() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2}, {3, 4}};
        test.chiSquare(counts);
    }

    @Test
    public void testChiSquareTestWithTwoWayTable() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        double pValue = test.chiSquareTest(counts);
        assertEquals(0.3527442358877183, pValue, 1e-6);
    }

    @Test
    public void testChiSquareTestWithTwoWayTableAndAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        boolean rejectNullHypothesis = test.chiSquareTest(counts, 0.05);
        assertTrue(rejectNullHypothesis);
    }
}