package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
    }

    @Test
    public void testChiSquareTestImplWithDistribution() {
        ChiSquareDistribution expectedDistribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(expectedDistribution);
        assertSame(expectedDistribution, test.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidArgs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2};
        double[] expected = {2, 3};
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareMethod() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2};
        double[] expected = {2, 3};
        double result = test.chiSquare(expected, observed);
        assertEquals(1.0, result, 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidArgs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2};
        double[] expected = {2, 3};
        test.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestMethod() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistribution expectedDistribution = new ChiSquaredDistributionImpl(1.0);
        long[] observed = {1, 2};
        double[] expected = {2, 3};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(1.0 - expectedDistribution.cumulativeProbability(1.0), pValue, 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2};
        double[] expected = {2, 3};
        test.chiSquareTest(expected, observed, -0.1);
    }

    @Test
    public void testChiSquareTestMethodWithValidAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistribution expectedDistribution = new ChiSquaredDistributionImpl(1.0);
        long[] observed = {1, 2};
        double[] expected = {2, 3};
        double alpha = 0.05;
        boolean result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTwoWayTableComparisonMethodWithInvalidArgs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {3, 4};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTwoWayTableComparisonMethod() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {3, 4};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(7.0 / 2.0, result, 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTwoWayTableComparisonMethodWithInvalidAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {3, 4};
        test.chiSquareTestDataSetsComparison(observed1, observed2, -0.1);
    }

    @Test
    public void testChiSquareTwoWayTableComparisonMethodWithValidAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {3, 4};
        double alpha = 0.05;
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(result);
    }
}