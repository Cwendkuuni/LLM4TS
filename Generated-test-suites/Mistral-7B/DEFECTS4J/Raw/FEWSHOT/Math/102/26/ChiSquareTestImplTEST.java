package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        ChiSquaredDistributionImpl distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        ChiSquaredDistribution expected = test.getDistribution();
        assertEquals(distribution, expected);
    }

    @Test
    public void testChiSquareWithValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4};
        double[] expected = {1.5, 2.5, 1.0, 3.5};
        double result = test.chiSquare(expected, observed);
        assertEquals(7.68, result, 0.001);
    }

    @Test
    public void testChiSquareWithInvalidInputsLengthMismatch() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3};
        double[] expected = {1.5, 2.5, 1.0, 3.5};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareWithInvalidInputsNegativeExpected() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4};
        double[] expected = {-1.5, 2.5, 1.0, 3.5};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareWithInvalidInputsNegativeObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {-2, 3, 1, 4};
        double[] expected = {1.5, 2.5, 1.0, 3.5};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareTestWithValidInputs() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4};
        double[] expected = {1.5, 2.5, 1.0, 3.5};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.0543157393700234, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestWithInvalidInputs() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {-2, 3, 1, 4};
        double[] expected = {1.5, 2.5, 1.0, 3.5};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTestWithInvalidSignificanceLevel() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4};
        double[] expected = {1.5, 2.5, 1.0, 3.5};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTest(expected, observed, 1.5));
    }

    @Test
    public void testChiSquareTestWithValidInputsAndSignificanceLevelRejectNullHypothesis()
            throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4};
        double[] expected = {1.5, 2.5, 1.0, 3.5};
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestWithValidInputsAndSignificanceLevelFailToRejectNullHypothesis()
            throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4};
        double[] expected = {1.5, 2.5, 1.0, 3.5};
        boolean result = test.chiSquareTest(expected, observed, 0.1);
        assertFalse(result);
    }

    @Test
    public void testChiSquareComparisonWithValidInputs() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3};
        long[] observed2 = {2, 4};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.25, result, 0.001);
    }

    @Test
    public void testChiSquareComparisonWithInvalidInputsLengthMismatch() throws MathException,
            IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3};
        long[] observed2 = {2};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInputsAndSignificanceLevelRejectNullHypothesis()
            throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3};
        long[] observed2 = {2, 4};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInputsAndSignificanceLevelFailToRejectNullHypothesis()
            throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3};
        long[] observed2 = {2, 4};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.1);
        assertFalse(result);
    }
}