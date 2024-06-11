package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplInstance() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertTrue(test instanceof ChiSquareTestImpl);
        assertTrue(test instanceof UnknownDistributionChiSquareTest);
        assert null != test.distribution;
    }

    @Test
    public void testChiSquareTestImplWithDistribution() {
        ChiSquareDistribution dist = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertEquals(dist, test.distribution);
    }

    @Test
    public void testChiSquareMethod() throws IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double result = test.chiSquare(expected, observed);
        assertEquals(5.5, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodInvalidPreconditions() throws IllegalArgumentException {
        double[] expected = {1.0};
        long[] observed = {2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestMethod() throws IllegalArgumentException, MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.0966, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestMethodInvalidPreconditions() throws IllegalArgumentException, MathException {
        double[] expected = {1.0};
        long[] observed = {2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestMethodWithAlpha() throws IllegalArgumentException, MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithAlphaInvalidPreconditions() throws IllegalArgumentException, MathException {
        double[] expected = {1.0};
        long[] observed = {2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTest(expected, observed, 0.05);
    }

    @Test
    public void testChiSquare2WayTableMethod() throws IllegalArgumentException {
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double result = test.chiSquare(counts);
        assertEquals(7.68, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare2WayTableMethodInvalidPreconditions() throws IllegalArgumentException {
        long[][] counts = {{1, 2}, {2, 3}};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquare(counts);
    }

    @Test
    public void testChiSquareTest2WayTableMethod() throws IllegalArgumentException, MathException {
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double pValue = test.chiSquareTest(counts);
        assertEquals(0.119, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest2WayTableMethodInvalidPreconditions() throws IllegalArgumentException, MathException {
        long[][] counts = {{1, 2}, {2, 3}};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTest(counts);
    }

    @Test
    public void testChiSquareTest2WayTableMethodWithAlpha() throws IllegalArgumentException, MathException {
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        boolean result = test.chiSquareTest(counts, 0.05);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest2WayTableMethodWithAlphaInvalidPreconditions() throws IllegalArgumentException, MathException {
        long[][] counts = {{1, 2}, {2, 3}};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTest(counts, 0.05);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethod() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(1.52, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonMethodInvalidPreconditions() throws IllegalArgumentException {
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethod() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.391, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodInvalidPreconditions() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithAlphaInvalidPreconditions() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }
}