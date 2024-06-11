package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

import java.util.Arrays;

public class ChiSquareTestImplTest {

    @org.junit.Before
    public void setUp() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        chiSquareTest.setDistribution(distribution);
    }

    @org.junit.Test
    public void testChiSquare() throws IllegalArgumentException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(6.25, result, 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidLength() throws IllegalArgumentException {
        long[] observed = new long[] {1, 2, 3};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        chiSquareTest.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidLength2() throws IllegalArgumentException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5};
        chiSquareTest.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareNegativeExpected() throws IllegalArgumentException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {-2.5, 2.5, 2.5, 2.5};
        chiSquareTest.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareNegativeObserved() throws IllegalArgumentException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        chiSquareTest.chiSquare(expected, new long[] {-1, 2, 3, 4});
    }

    @org.junit.Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(0.045, pValue, 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidLength() throws IllegalArgumentException, MathException {
        long[] observed = new long[] {1, 2, 3};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidLength2() throws IllegalArgumentException, MathException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5};
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNegativeExpected() throws IllegalArgumentException, MathException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {-2.5, 2.5, 2.5, 2.5};
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestNegativeObserved() throws IllegalArgumentException, MathException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        chiSquareTestImpl.chiSquareTest(expected, new long[] {-1, 2, 3, 4});
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestAlphaLessThanZero() throws IllegalArgumentException, MathException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        chiSquareTestImpl.chiSquareTest(expected, observed, -0.1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestAlphaGreaterThanOne() throws IllegalArgumentException, MathException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        chiSquareTestImpl.chiSquareTest(expected, observed, 1.1);
    }

    @org.junit.Test
    public void testChiSquareTestAlphaZero() throws IllegalArgumentException, MathException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, 0.0);
        assertFalse(result);
    }

    @org.junit.Test(expected = MathException.class)
    public void testChiSquareTestMathException() throws IllegalArgumentException, MathException {
        long[] observed = new long[] {1, 2, 3, 4};
        double[] expected = new double[] {2.5, 2.5, 2.5, 2.5};
        chiSquareTestImpl.chiSquareTest(expected, observed, 0.05);
    }

    @org.junit.Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[] {1, 2, 3};
        long[] observed2 = new long[] {2, 3, 4};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.127, pValue, 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidLength() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[] {1, 2};
        long[] observed2 = new long[] {2, 3, 4};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidLength2() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[] {1, 2, 3};
        long[] observed2 = new long[] {2, 3};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test
    public void testChiSquareTestDataSetsComparisonAlphaZero() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[] {1, 2, 3};
        long[] observed2 = new long[] {2, 3, 4};
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
        assertFalse(result);
    }

    @org.junit.Test(expected = MathException.class)
    public void testChiSquareTestDataSetsComparisonMathException() throws IllegalArgumentException, MathException {
        long[] observed1 = new long[] {1, 2, 3};
        long[] observed2 = new long[] {2, 3, 4};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }

    @org.junit.Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        long[] observed1 = new long[] {1, 2, 3};
        long[] observed2 = new long[] {2, 3, 4};
        double result = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(2.286, result, 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidLength() throws IllegalArgumentException {
        long[] observed1 = new long[] {1, 2};
        long[] observed2 = new long[] {2, 3, 4};
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidLength2() throws IllegalArgumentException {
        long[] observed1 = new long[] {1, 2, 3};
        long[] observed2 = new long[] {2, 3};
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }
}