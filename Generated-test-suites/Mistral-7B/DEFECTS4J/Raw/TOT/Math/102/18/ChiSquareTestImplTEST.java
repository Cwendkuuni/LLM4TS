package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    /**
     * Test for {@link ChiSquareTestImpl# ChiSquareTestImpl()}.
     */
    @Test
    public void testDefaultConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = test.getDistribution();
        assertEquals("Degrees of freedom should be 1", 1, distribution.getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test for {@link ChiSquareTestImpl# ChiSquareTestImpl(ChiSquaredDistribution)}.
     */
    @Test
    public void testCustomDistributionConstructor() {
        ChiSquaredDistributionImpl distribution = new ChiSquaredDistributionImpl(3.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertEquals("Degrees of freedom should be 3", 3, test.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test for {@link ChiSquareTestImpl# chiSquare(double[], long[])}.
     */
    @Test
    public void testChiSquareWithexpectedAndObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0, 3.0 };
        long[] observed = new long[] { 2, 3, 1 };
        try {
            double chiSquareStatistic = test.chiSquare(expected, observed);
            assertEquals("Chi-square statistic should not be negative", 6.0, chiSquareStatistic, 0.0);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for {@link ChiSquareTestImpl# chiSquareTest(double[], long[])}.
     */
    @Test
    public void testChiSquareTestWithExpectedAndObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0, 3.0 };
        long[] observed = new long[] { 2, 3, 1 };
        try {
            double pValue = test.chiSquareTest(expected, observed);
            assertTrue("pValue should be less than 1.0", pValue < 1.0);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for {@link ChiSquareTestImpl# chiSquare(long[][])}.
     */
    @Test
    public void testChiSquareWith2WayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        try {
            double chiSquareStatistic = test.chiSquare(counts);
            assertEquals("Chi-square statistic should not be negative", 14.0, chiSquareStatistic, 0.0);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for {@link ChiSquareTestImpl# chiSquareTest(long[][])}.
     */
    @Test
    public void testChiSquareTestWith2WayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        try {
            double pValue = test.chiSquareTest(counts);
            assertTrue("pValue should be less than 1.0", pValue < 1.0);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for {@link ChiSquareTestImpl# chiSquareTest(long[], long[])}.
     */
    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] { 1, 2, 3 };
        long[] observed2 = new long[] { 2, 1, 3 };
        try {
            double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
            assertTrue("pValue should be less than 1.0", pValue < 1.0);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for invalid arguments in {@link ChiSquareTestImpl# chiSquare(double[], long[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0 };
        long[] observed = new long[] { 2, 3, 1 };
        test.chiSquare(expected, observed);
    }

    /**
     * Test for invalid arguments in {@link ChiSquareTestImpl# chiSquare(double[], long[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidObservedArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0, 3.0 };
        long[] observed = new long[] { 2, 3 };
        test.chiSquare(expected, observed);
    }

    /**
     * Test for invalid arguments in {@link ChiSquareTestImpl# chiSquareTest(double[], long[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidExpectedArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0 };
        long[] observed = new long[] { 2, 3, 1 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test for invalid arguments in {@link ChiSquareTestImpl# chiSquareTest(double[], long[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidObservedArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] { 1.0, 2.0, 3.0 };
        long[] observed = new long[] { 2, 3 };
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test for invalid arguments in {@link ChiSquareTestImpl# chiSquare(long[][])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalid2WayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][] {
                {1, 2},
                {4, 5}
        };
        test.chiSquare(counts);
    }

    /**
     * Test for invalid arguments in {@link ChiSquareTestImpl# chiSquareTest(long[][])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalid2WayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[][] {
                {1, 2},
                {4, 5}
        };
        test.chiSquareTest(counts);
    }
}