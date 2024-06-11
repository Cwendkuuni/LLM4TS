package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = chiSquareTest.getDistribution();
        assertEquals(1.0, distribution.getDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testChiSquareWithExpectedAndObservedArrays() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        double[] expected = new double[] {1.0, 2.0, 1.0, 2.0};
        long[] observed = new long[] {2, 3, 1, 3};

        try {
            double chiSquare = chiSquareTest.chiSquare(expected, observed);
            assertEquals(6.602, chiSquare, 0.001);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }

        try {
            double pValue = chiSquareTest.chiSquareTest(expected, observed);
            assertEquals(0.0789, pValue, 0.0001);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertTrue(chiSquareTest.chiSquareTest(expected, observed, 0.05));
    }

    @Test
    public void testChiSquareWith2WayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[][] counts = new long[][] {{1, 2, 3}, {2, 3, 1}, {3, 1, 2}};

        try {
            double chiSquare = chiSquareTest.chiSquare(counts);
            assertEquals(7.8, chiSquare, 0.1);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }

        try {
            double pValue = chiSquareTest.chiSquareTest(counts);
            assertEquals(0.0261, pValue, 0.0001);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertTrue(chiSquareTest.chiSquareTest(counts, 0.05));
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed1 = new long[] {1, 2, 1, 2, 1, 2};
        long[] observed2 = new long[] {2, 1, 2, 1, 2, 1};

        double chiSquare = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(5.991, chiSquare, 0.001);

        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(5.0);
        double pValue = 1.0 - chiSquaredDistribution.cumulativeProbability(chiSquare);
        assertEquals(0.0188, pValue, 0.0001);
        assertTrue(chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedAndObservedArrays() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        double[] expected = new double[] {1.0};
        long[] observed = new long[] {1};

        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalid2WayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[][] counts = new long[][] {{1, 2}, {2, 1}};
        chiSquareTest.chiSquare(counts);
    }
}