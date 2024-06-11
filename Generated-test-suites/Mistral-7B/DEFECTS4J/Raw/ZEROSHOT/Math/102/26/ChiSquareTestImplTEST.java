package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Before;
import org.junit.Test;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTestImpl;

    @Before
    public void setUp() {
        ChiSquaredDistributionImpl chiSquaredDistribution = new ChiSquaredDistributionImpl(1.0);
        chiSquareTestImpl = new ChiSquareTestImpl(chiSquaredDistribution);
    }

    @Test
    public void testChiSquare() throws IllegalArgumentException {
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 2, 4, 2};
        double chiSquare = chiSquareTestImpl.chiSquare(expected, observed);
        assertEquals(7.0, chiSquare, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidArgument() throws IllegalArgumentException {
        long[] expected = {1};
        long[] observed = {2};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 2, 4, 2};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(0.013533533973357562, pValue, 0.0000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidArgument() throws IllegalArgumentException, MathException {
        long[] expected = {1};
        long[] observed = {2};
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestAlpha() throws IllegalArgumentException, MathException {
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 2, 4, 2};
        boolean rejectNullHypothesis = chiSquareTestImpl.chiSquareTest(expected, observed, 0.05);
        assertFalse(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareDataSetComparison() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 1, 3};
        double chiSquare = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(3.0, chiSquare, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetComparisonInvalidLength() throws IllegalArgumentException {
        long[] observed1 = {1};
        long[] observed2 = {2};
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetComparison() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 1, 3};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.04566193611281877, pValue, 0.0000001);
    }

    @Test
    public void testChiSquareTestDataSetComparisonAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 1, 3};
        boolean rejectNullHypothesis = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(rejectNullHypothesis);
    }
}