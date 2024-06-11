package org.apache.commons.math.stat.inference.test;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestWithTwoArrays() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5, 5, 5, 5};
        long[] observed2 = {2, 2, 3, 4, 4, 4, 4, 4};
        double expectedPValue = 0.01; // arbitrary value for testing

        double pValue = test.chiSquareTest(observed1, observed2, expectedPValue);

        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidArraysSize() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {3, 4, 5};
        test.chiSquareTest(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidArraysValues() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {-1, 2};
        long[] observed2 = {3, 4, 5};
        test.chiSquareTest(observed1, observed2);
    }

    @Test
    public void testChiSquareTestWithOneArray() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 5, 5, 5};
        double expectedPValue = 0.05; // arbitrary value for testing

        double pValue = test.chiSquareTest(observed);

        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidArrayValues() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {-1, 2, 3, 4, 5, 5, 5, 5};
        test.chiSquareTest(observed);
    }

    @Test
    public void testChiSquare() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 5, 5, 5};
        long[] expected = {2, 2, 3, 4, 4, 4, 4, 4};
        double expectedChiSquare = 3.841554; // chi-square distribution value with df=7, pValue=0.05

        double chiSquare = test.chiSquare(expected, observed);

        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidArraysSizes() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2};
        long[] expected = {3, 4, 5};
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidArraysValues() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {-1, 2};
        long[] expected = {3, 4, 5};
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareDataSetsComparison() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5, 5, 5, 5};
        long[] observed2 = {2, 2, 3, 4, 4, 4, 4, 4};
        double expectedPValue = 0.01; // arbitrary value for testing

        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2, expectedPValue);

        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidArraysSizes()
            throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {3, 4, 5};
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithCustomDistribution()
            throws MathIllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5, 5, 5, 5};
        long[] observed2 = {2, 2, 3, 4, 4, 4, 4, 4};
        ChiSquaredDistribution chiSquareDist = new ChiSquaredDistributionImpl(7);
        double expectedPValue = 0.01; // arbitrary value for testing

        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, expectedPValue, chiSquareDist);

        assertTrue(result);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidDistribution()
            throws MathIllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5, 5, 5, 5};
        long[] observed2 = {2, 2, 3, 4, 4, 4, 4, 4};
        ChiSquaredDistribution chiSquareDist = new ChiSquaredDistributionImpl(-1);
        double expectedPValue = 0.01; // arbitrary value for testing

        test.chiSquareTestDataSetsComparison(observed1, observed2, expectedPValue, chiSquareDist);
    }
}