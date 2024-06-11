package org.apache.commons.math.stat.inference;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test file for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstruction() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        ChiSquareTestImpl chiSquareTestImplWithDistribution = new ChiSquareTestImpl(
                new ChiSquaredDistributionImpl(1.0));
        Assert.assertNotNull(chiSquareTestImpl);
        Assert.assertNotNull(chiSquareTestImplWithDistribution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = new long[0];
        long[] expected = new long[1];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareMethod() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3};
        long[] observed = {2, 3, 1};
        double chiSquareResult = chiSquareTestImpl.chiSquare(expected, observed);
        double expectedChiSquare = 2.0d;
        Assert.assertEquals(expectedChiSquare, chiSquareResult, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] expected = new long[1];
        long[] observed = new long[0];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestMethod() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3};
        long[] observed = {2, 3, 1};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        double expectedPValue = 0.15865525; // Approximate value using statistical tables
        Assert.assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] expected = new long[0];
        long[] observed = new long[1];
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestMethodWithAlpha() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3};
        long[] observed = {2, 3, 1};
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, 0.05);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidAlpha() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3};
        long[] observed = {2, 3, 1};
        chiSquareTestImpl.chiSquareTest(expected, observed, 1.0);
    }

    @Test
    public void testChiSquareLongArrayMethod() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 1}, {1, 1, 1}};
        double pValue = chiSquareTestImpl.chiSquareTest(counts);
        double expectedPValue = 0.15865525; // Approximate value using statistical tables
        Assert.assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareLongArrayMethodWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2}};
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test
    public void testChiSquareLongArrayMethodWithAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 1}, {1, 1, 1}};
        boolean result = chiSquareTestImpl.chiSquareTest(counts, 0.05);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareLongArrayMethodWithInvalidAlpha() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 1}, {1, 1, 1}};
        chiSquareTestImpl.chiSquareTest(counts, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[1];
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethod() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};
        double chiSquareResult = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        double expectedChiSquare = 2.0d;
        Assert.assertEquals(expectedChiSquare, chiSquareResult, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethod() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.15865525; // Approximate value using statistical tables
        Assert.assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidAlpha() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 1.0);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        Assert.assertFalse(result);
    }
}