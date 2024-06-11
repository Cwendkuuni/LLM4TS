package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    @Test
    public void testConstruction() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.getDistribution());
    }

    @Test
    public void testConstructionWithDistribution() {
        ChiSquaredDistributionImpl distribution = new ChiSquaredDistributionImpl(2);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, test.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArrayLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[1];
        long[] observed = new long[2];
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidObservedAndExpectedLengths() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[2];
        long[] observed = new long[1];
        test.chiSquare(expected, observed);
    }

    @Test
    public void testValidChiSquareCalculation() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{ 1.0, 2.0, 3.0 };
        long[] observed = new long[]{ 2, 3, 3 };
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(3.0, chiSquare, 1e-10);
    }

    @Test
    public void testValidPValueCalculation() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{ 1.0, 2.0, 3.0 };
        long[] observed = new long[]{ 2, 3, 3 };
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.95, pValue, 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{ 1.0, 2.0, 3.0 };
        long[] observed = new long[]{ 2, 3, 3 };
        test.chiSquareTest(expected, observed, -0.1);
    }

    @Test
    public void testValidRejectNullHypothesis() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{ 1.0, 2.0, 3.0 };
        long[] observed = new long[]{ 2, 3, 3 };
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @Test
    public void testValidFailToRejectNullHypothesis() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{ 1.0, 2.0, 3.0 };
        long[] observed = new long[]{ 2, 3, 3 };
        boolean result = test.chiSquareTest(expected, observed, 0.9);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalid2WayTableLengths() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[3][2];
        test.chiSquareTest(counts);
    }

    @Test
    public void testValidChiSquareDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{ 1, 2, 2 };
        long[] observed2 = new long[]{ 1, 3, 1 };
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(2.5, chiSquare, 1e-10);
    }

    @Test
    public void testValidPValueDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{ 1, 2, 2 };
        long[] observed2 = new long[]{ 1, 3, 1 };
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.11111111111111111, pValue, 1e-10);
    }

    @Test
    public void testValidRejectNullHypothesisDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{ 1, 2, 2 };
        long[] observed2 = new long[]{ 1, 3, 1 };
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    @Test
    public void testValidFailToRejectNullHypothesisDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{ 1, 2, 2 };
        long[] observed2 = new long[]{ 1, 3, 1 };
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.9);
        assertFalse(result);
    }
}