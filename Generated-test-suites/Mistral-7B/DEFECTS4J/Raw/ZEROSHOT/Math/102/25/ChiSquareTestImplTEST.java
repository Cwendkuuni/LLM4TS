package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

import java.util.Arrays;

public class ChiSquareTestImplTest {

    @org.junit.Before
    public void setUp() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.setDistribution(new ChiSquaredDistributionImpl(1.0));
    }

    @org.junit.Test
    public void testChiSquare() throws IllegalArgementException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(7.778763642862863, chiSquare, 0.001);
    }

    @org.junit.Test
    public void testChiSquareTest() throws IllegalArgementException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.01832242679872466, pValue, 0.001);
    }

    @org.junit.Test
    public void testChiSquareTestWithAlpha() throws IllegalArgementException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertFalse(result);
    }

    @org.junit.Test
    public void testChiSquareDataSetComparison() throws IllegalArgementException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(2.0, chiSquare, 0.001);
    }

    @org.junit.Test
    public void testChiSquareDataSetComparisonTest() throws IllegalArgementException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.5, pValue, 0.001);
    }

    @org.junit.Test
    public void testChiSquareDataSetComparisonTestWithAlpha() throws IllegalArgementException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidChiSquareData() throws IllegalArgementException {
        long[] observed = {1, -1};
        double[] expected = {2.5, 2.5};
        test.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidChiSquareData2() throws IllegalArgementException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 4.5};
        test.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetComparisonInvalidData() throws IllegalArgementException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = new long[] {1, 2};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetComparisonInvalidData2() throws IllegalArgementException {
        long[] observed1 = new long[] {1, 2};
        long[] observed2 = {1, 2, 3};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidExpectedData() throws IllegalArgementException {
        long[] observed = {1, 2, 3};
        double[] expected = {-2.5, 2.5, 2.5};
        test.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidExpectedData2() throws IllegalArgementException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, -2.5, 2.5};
        test.chiSquare(expected, observed);
    }
}