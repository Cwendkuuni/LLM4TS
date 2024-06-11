package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Before;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTest;
    private ChiSquaredDistribution distribution;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
        distribution = new ChiSquaredDistributionImpl(1.0);
        chiSquareTest.setDistribution(distribution);
    }

    @Test
    public void testChiSquare() throws IllegalArgumentException {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 3.5, 2.5, 3.5};
        double actual = chiSquareTest.chiSquare(expected, observed);
        double expectedValue = 3.0;
        assertEquals(expectedValue, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidArguments() throws IllegalArgumentException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 3.5, 2.5};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 3.5, 2.5, 3.5};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        double expectedValue = 0.435297126; // obtained from stats.stackexchange.com
        assertEquals(expectedValue, pValue, 0.0001);
    }

    @Test
    public void testChiSquareTestWithInvalidArguments() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 3.5, 2.5};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestWithAlpha() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 3.5, 2.5, 3.5};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestWithInvalidAlpha() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 3.5, 2.5, 3.5};
        chiSquareTest.chiSquareTest(expected, observed, 0.5);
    }

    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double actual = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        double expectedValue = 2.916666667; // obtained from stats.stackexchange.com
        assertEquals(expectedValue, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidArguments()
            throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparison()
            throws IllegalArgumentException, MathException, InterruptedException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedValue = 0.113097335; // obtained from stats.stackexchange.com
        assertEquals(expectedValue, pValue, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithInvalidArguments()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha()
            throws IllegalArgumentException, MathException, InterruptedException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithInvalidAlpha()
            throws IllegalArgumentException, MathException, InterruptedException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.5);
    }
}