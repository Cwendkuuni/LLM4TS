package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.MathException;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double expectedResult = 6.602488547969688;
        double result = chiSquareTest.chiSquare(counts);
        assertEquals(expectedResult, result, 0.0001);
    }

    @Test
    public void testChiSquareTest() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double alpha = 0.05;
        boolean expectedResult = false;
        boolean result = chiSquareTest.chiSquareTest(counts, alpha);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        double alpha = 0.05;
        boolean expectedResult = false;
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        double expectedResult = 5.991464968542212;
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(expectedResult, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidArgument1() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        chiSquareTest.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidArgument2() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3}};
        chiSquareTest.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidArgument3() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidArgument4() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}, {3, 4, 5}};
        chiSquareTest.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidArgument5() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 3, 3};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestInvalidArgument() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}, {3, 4, 5}, {3, 4, 5}};
        double alpha = 1.5;
        chiSquareTest.chiSquareTest(counts, alpha);
    }
}