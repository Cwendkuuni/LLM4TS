package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.MathException;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double result = test.chiSquare(counts);
        double expected = 7.0;
        assertEquals(expected, result, 0.001);

        long[][] counts2 = {{1, 1}, {1, 1}};
        double result2 = test.chiSquare(counts2);
        double expected2 = 0.0;
        assertEquals(expected2, result, 0.001);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double pValue = test.chiSquareTest(counts);
        double expected = 0.0421;
        assertEquals(expected, pValue, 0.001);

        long[][] counts2 = {{1, 1}, {1, 1}};
        double pValue2 = test.chiSquareTest(counts2);
        double expected2 = 1.0;
        assertEquals(expected2, pValue2, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expected = 0.0421;
        assertEquals(expected, pValue, 0.001);

        long[] observed1_2 = {1, 1, 1, 1};
        long[] observed2_2 = {1, 1, 1, 1};
        double pValue2 = test.chiSquareTestDataSetsComparison(observed1_2, observed2_2);
        double expected2 = 1.0;
        assertEquals(expected2, pValue2, 0.001);
    }

    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        double expected = 7.0;
        assertEquals(expected, result, 0.001);

        long[] observed1_2 = {1, 1, 1, 1};
        long[] observed2_2 = {1, 1, 1, 1};
        double result2 = test.chiSquareDataSetsComparison(observed1_2, observed2_2);
        double expected2 = 0.0;
        assertEquals(expected2, result2, 0.001);
    }
}