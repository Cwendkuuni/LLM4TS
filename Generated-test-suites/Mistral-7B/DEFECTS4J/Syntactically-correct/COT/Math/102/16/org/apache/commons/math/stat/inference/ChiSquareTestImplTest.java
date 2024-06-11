package org.apache.commons.math.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Test cases for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
    }

    @Test
    public void testChiSquareTestImplWithDistributionConstructor() {
        ChiSquaredDistributionImpl cd = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(cd);
        assertNotNull(test);
        assertEquals(cd, test.getDistribution());
    }

    @Test
    public void testChiSquare() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 2.0};
        long[] observed = {3.0, 2.0, 2.0};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(3.0, chiSquare, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpectedLength() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0};
        long[] observed = {3.0, 2.0, 2.0};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidObservedLength() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 2.0};
        long[] observed = {3.0};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpectedLengthMismatchObservedLength()
            throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0};
        long[] observed = {3.0, 2.0, 2.0};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeExpectedValues() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {-1.0, 3.0, 2.0};
        long[] observed = {3.0, 2.0, 2.0};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeObservedValues() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 2.0};
        long[] observed = {-1, 2.0, 2.0};
        test.chiSquare(expected, observed);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestWithInvalidChiSquare() throws IllegalArgumentException,
            MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 2.0};
        long[] observed = {3.0, 2.0, 2.0};
        double pValue = test.chiSquareTest(expected, observed);
        fail("Expected MathException");
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 2.0};
        long[] observed = {3.0, 2.0, 2.0};
        double pValue = test.chiSquareTest(expected, observed);
        assertNotNull(pValue);
        assertTrue(pValue > 0.0);
        assertTrue(pValue < 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidAlpha() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 2.0};
        long[] observed = {3.0, 2.0, 2.0};
        test.chiSquareTest(expected, observed, -0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithAlphaGreaterThan05()
            throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 2.0};
        long[] observed = {3.0, 2.0, 2.0};
        test.chiSquareTest(expected, observed, 0.6);
    }

    @Test
    public void testChiSquareTestWithAlphaLessThan01() throws IllegalArgumentException,
            MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 2.0};
        long[] observed = {3.0, 2.0, 2.0};
        boolean rejectNullHypothesis = test.chiSquareTest(expected, observed, 0.01);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestWithAlphaEqual01() throws IllegalArgumentException,
            MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 2.0};
        long[] observed = {3.0, 2.0, 2.0};
        boolean rejectNullHypothesis = test.chiSquareTest(expected, observed, 0.1);
        assertFalse(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareDataSetComparison() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(2.0, chiSquare, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataSetComparisonLengthMismatch()
            throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataSetComparisonWithNegativeCounts()
            throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, -2, 3};
        long[] observed2 = {2, 2, 3};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestDataSetComparisonWithInvalidChiSquare()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        fail("Expected MathException");
    }

    @Test
    public void testChiSquareTestDataSetComparison()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertNotNull(pValue);
        assertTrue(pValue > 0.0);
        assertTrue(pValue < 1.0);
    }

    @Test
    public void testChiSquareTestDataSetComparisonWithAlphaLessThan01()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.01);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestDataSetComparisonWithAlphaEqual01()
            throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.1);
        assertFalse(rejectNullHypothesis);
    }

    
}