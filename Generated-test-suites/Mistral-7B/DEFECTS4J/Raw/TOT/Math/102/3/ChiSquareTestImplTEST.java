package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTest;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquareWithSimpleExpectedAndObserved() {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 1.5, 2.5, 2.5};
        double statistic = chiSquareTest.chiSquare(expected, observed);
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        Assert.assertEquals(5.991464898665585, statistic, 0.001);
        Assert.assertEquals(0.019459093193606176, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithUnexpectedArrayLengths() {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 1.5, 2.5, 3.5};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeExpectedOrObserved() {
        long[] observed = {1, -2, 3, 4};
        double[] expected = {2.5, -1.5, 2.5, 2.5};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareWithLarge2WayTable() {
        long[][] observed = {
                {1, 2, 3, 4, 5},
                {2, 4, 5, 4, 3},
                {3, 3, 1, 2, 4},
                {4, 3, 5, 6, 3},
                {5, 3, 4, 3, 5}
        };
        double pValue = chiSquareTest.chiSquareTest(observed);
        Assert.assertEquals(0.12469907793797533, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithEmptyTable() {
        long[][] observed = {};
        chiSquareTest.chiSquareTest(observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithNegativeEntryInTable() {
        long[][] observed = {
                {1, 2, 3, 4, 5},
                {2, 4, 5, -4, 3},
                {3, 3, 1, 2, 4},
                {4, 3, 5, 6, 3},
                {5, 3, 4, 3, 5}
        };
        chiSquareTest.chiSquareTest(observed);
    }
}