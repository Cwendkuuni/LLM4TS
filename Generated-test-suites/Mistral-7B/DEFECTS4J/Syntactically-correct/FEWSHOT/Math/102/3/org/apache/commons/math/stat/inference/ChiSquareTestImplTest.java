package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        assertNotNull(chiSquareTest.getDistribution());
    }

    @Test
    public void testChiSquareTestImplConstructorWithDistributionArg() {
        ChiSquareDistributionImpl chiSquaredDistribution = new ChiSquareDistributionImpl(5.0);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(chiSquaredDistribution);
        assertEquals(chiSquaredDistribution, chiSquareTest.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplConstructorWithNullDistributionArg() {
        new ChiSquareTestImpl(null);
    }

    @Test
    public void testChiSquareTestImplGetDistribution() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = chiSquareTest.getDistribution();
        assertEquals(ChiSquaredDistribution.class, distribution.getClass());
    }

    @Test
    public void testChiSquareTestImplSetDistribution() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = new ChiSquareDistributionImpl(5.0);
        chiSquareTest.setDistribution(distribution);
        assertEquals(distribution, chiSquareTest.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareWithInvalidObservedArg() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {1};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareWithInvalidExpectedArg() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1};
        long[] observed = {};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareWithInvalidArgs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1};
        long[] observed = {1};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestImplChiSquareWithValidArgs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {5, 10};
        long[] observed = {6, 11};
        assertEquals(11.182235337342385, chiSquareTest.chiSquare(expected, observed), 0.001);
    }

    @Test
    public void testChiSquareTestImplChiSquareTestWithValidArgs() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {5, 10};
        long[] observed = {6, 11};
        assertEquals(0.04757322666051445, chiSquareTest.chiSquareTest(expected, observed), 0.001);
    }

    @Test
    public void testChiSquareTestImplChiSquareTestWithInvalidArgs() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1};
        long[] observed = {};
        try {
            chiSquareTest.chiSquareTest(expected, observed);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestImplChiSquareTestWithSignificantLevelLessThanZero() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {5, 10};
        long[] observed = {6, 11};
        try {
            chiSquareTest.chiSquareTest(expected, observed, -0.1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestImplChiSquareTestWithSignificantLevelGreaterThanOne() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {5, 10};
        long[] observed = {6, 11};
        try {
            chiSquareTest.chiSquareTest(expected, observed, 1.1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestImplChiSquareTestDataSetsComparisonWithValidArgs() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 3, 4, 5};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(pValue > 0.0);
        assertTrue(pValue < 1.0);
    }

    @Test
    public void testChiSquareTestImplChiSquareTestDataSetsComparisonWithInvalidArgs() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {};
        long[] observed2 = {};
        try {
            chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestImplChiSquareTestDataSetsComparisonWithInvalidSignificanceLevel() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 3, 4, 5};
        try {
            chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 1.1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestImplChiSquareDataSetsComparisonWithValidArgs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 3, 4, 5};
        assertEquals(3.8414590353390595, chiSquareTest.chiSquareDataSetsComparison(observed1, observed2), 0.001);
    }

    @Test
    public void testChiSquareTestImplChiSquareDataSetsComparisonWithInvalidArgs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {};
        long[] observed2 = {};
        try {
            chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestImplChiSquareDataSetsComparisonWithSignificantLevelLessThanZero() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 3, 4, 5};
        try {
            chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, -0.1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestImplChiSquareDataSetsComparisonWithSignificantLevelGreaterThanOne() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 3, 4, 5};
        try {
            chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 1.1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestImplIsNonNegativeLongArray() {
        long[] array = {};
        assertTrue(ChiSquareTestImpl.isNonNegative(array));

        long[] array1 = {1, 2, 3, 4, 5};
        assertTrue(ChiSquareTestImpl.isNonNegative(array1));

        long[] array2 = {-1, 2, 3, 4, 5};
        assertFalse(ChiSquareTestImpl.isNonNegative(array2));
    }

    @Test
    public void testChiSquareTestImplIsNonNegativeLongArray2D() {
        long[][] array2D = {};
        assertTrue(ChiSquareTestImpl.isNonNegative(array2D));

        long[][] array2D1 = {{1, 2, 3}, {4, 5, 6}};
        assertTrue(ChiSquareTestImpl.isNonNegative(array2D1));

        long[][] array2D2 = {{1, 2, 3}, {4, 5, -6}};
        assertFalse(ChiSquareTestImpl.isNonNegative(array2D2));
    }

    @Test
    public void testChiSquareTestImplIsRectangularLongArray2D() {
        long[][] array2D = {{1}, {2, 3}};
        assertFalse(ChiSquareTestImpl.isRectangular(array2D));

        long[][] array2D1 = {{1, 1}, {1, 1}};
        assertTrue(ChiSquareTestImpl.isRectangular(array2D1));

        long[][] array2D2 = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        assertTrue(ChiSquareTestImpl.isRectangular(array2D2));
    }
}