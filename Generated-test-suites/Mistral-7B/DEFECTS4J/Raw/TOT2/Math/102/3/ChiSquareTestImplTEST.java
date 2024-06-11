package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

/**
 * Tests for ChiSquareTestImpl class
 */
public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareMethod() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {2, 3, 2, 3, 3};
        double chiSquareStatistic = chiSquareTestImpl.chiSquare(expected, observed);
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(4.0, chiSquareStatistic, 0.001);
        assertEquals(0.05, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidParameters() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = {1, 2};
        long[] expected = {2, 2};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethod() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 2, 3, 3};
        double chiSquareStatistic = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(2.958562827310386, chiSquareStatistic, 0.001);
        assertEquals(0.05, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonMethodWithInvalidParameters() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2};
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }
}