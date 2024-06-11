package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareDegreesOfFreedom;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private static final double DELTA = 0.001;

    @Test
    public void testConstructors() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        assertNotNull(instance.getDistribution());
        assertEquals(1.0, instance.getDistribution().getDegreesOfFreedom(), DELTA);

        ChiSquareDistribution x = new ChiSquaredDistributionImpl(2.0);
        ChiSquareTestImpl instance2 = new ChiSquareTestImpl(x);
        assertNotNull(instance2.getDistribution());
        assertEquals(2.0, instance2.getDistribution().getDegreesOfFreedoms(), DELTA);
    }

    @Test
    public void testChiSquare() throws IllegalArgumentException {
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 1, 2};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double result = instance.chiSquare(expected, observed);
        assertEquals(7.918969265534879, result, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidExpectedLength() throws IllegalArgumentException {
        long[] expected = {1, 2};
        long[] observed = {1, 2, 3};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidObservedLength() throws IllegalArgumentException {
        long[] expected = {1, 2, 3, 4, 5};
        long[] observed = {1, 2};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidExpectedAndObservedLengthsMismatch()
            throws IllegalArgumentException {
        long[] expected = {1, 2, 3, 4, 5};
        long[] observed = {1, 2};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidExpectedNonPositive() throws IllegalArgumentException {
        long[] expected = {-1, 2, 3, 4, 5};
        long[] observed = {1, 2, 3, 4, 5};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidObservedNonNonNegative() throws IllegalArgumentException {
        long[] expected = {1, 2, 3, 4, 5};
        long[] observed = {-1, 2, 3, 4, 5};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] expected = {1, 2, 3, 4, 5, 5, 4, 3, 2, 1};
        long[] observed = {2, 3, 3, 4, 5, 5, 4, 3, 1, 2};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double pValue = instance.chiSquareTest(expected, observed);
        double significanceLevel = 0.05;
        assertTrue(pValue < significanceLevel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidExpectedLength() throws IllegalArgumentException, MathException {
        long[] expected = {1, 2};
        long[] observed = {1, 2, 3, 4, 5};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidObservedLength() throws IllegalArgumentException, MathException {
        long[] expected = {1, 2, 3, 4, 5};
        long[] observed = {1, 2};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidExpectedAndObservedLengthsMismatch()
            throws IllegalArgumentException, MathException {
        long[] expected = {1, 2, 3, 4, 5};
        long[] observed = {1, 2};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquareTest(expected, observed);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestInvalidDegreesOfFreedom()
            throws IllegalArgumentException, MathException {
        long[] expected = {1, 2, 3, 4, 5};
        long[] observed = {1, 2};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.setDistribution(new ChiSquaredDistributionImpl(-1.0));
        instance.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {3, 2, 1, 4, 5};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double pValue = instance.chiSquareTestDataSetsComparison(observed1, observed2);
        double significanceLevel = 0.05;
        assertTrue(pValue < significanceLevel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidObserved1Length()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2};
        long[] observed2 = {1, 2, 3, 4, 5};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidObserved2Length()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidExpectedLength()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquareTestDataSetsComparison(new long[0], observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidExpectedAndObservedMismatchLength()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonAlphaRejectHypothesis()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {3, 2, 1, 4, 5};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double significanceLevel = 0.01;
        boolean result = instance.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonAlphaDoNotRejectHypothesis()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {3, 2, 1, 4, 5};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double significanceLevel = 0.02;
        boolean result = instance.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertFalse(result);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestDataSetsComparisonInvalidDegreesOfFreedom()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2};
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.setDistribution(new ChiSquaredDistributionImpl(-1.0));
        instance.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonAlphaRejectHypothesis2()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {3, 2, 1, 4, 5};
        ChiSquareDegreesOfFreedom df = new ChiSquaredDegreesOfFreedom(4.0);
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.setDistribution(new ChiSquaredDistributionImpl(df.getDegreesOfFreedom()));
        double significanceLevel = 0.01;
        boolean result = instance.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonAlphaDoNotRejectHypothesis2()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {3, 2, 1, 4, 5};
        ChiSquareDegreesOfFreedom df = new ChiSquaredDegreesOfFreedom(4.0);
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        instance.setDistribution(new ChiSquaredDistributionImpl(df.getDegreesOfFreedom()));
        double significanceLevel = 0.02;
        boolean result = instance.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertFalse(result);
    }
}