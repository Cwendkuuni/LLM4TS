package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.distribution.DistributionFactory;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testConstructors() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        assertEquals(1.0, instance.getDistribution().getDegreesOfFreedom(), 0.0);

        ChiSquareDistributionImpl distribution = new ChiSquaredDistributionImpl(2.0);
        ChiSquareTestImpl instanceWithDistribution = new ChiSquareTestImpl(distribution);
        assertEquals(2.0, instanceWithDistribution.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testChiSquareWithValidArguments() throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double chiSquareStatistic = instance.chiSquare(expected, observed);
        assertEquals(7.778481523255814, chiSquareStatistic, 0.00001);
    }

    @Test
    public void testChiSquareWithInvalidLengthObservedOrExpected() throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquare(expected, observed));
        observed = new long[5];
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquare(expected, observed));
        expected = new double[3];
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquare(expected, observed));
        expected = new double[5];
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareWithInvalidPositiveExpectedOrNonNegativeObserved() throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {-1.5, 2.5, 2.5, 2.5, 2.5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquare(expected, observed));
        double[] expectedInvalid = {2.5, -1.5, 2.5, 2.5, 2.5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquare(expectedInvalid, observed));
    }

    @Test
    public void testChiSquareTestWithValidArguments() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double pValue = instance.chiSquareTest(expected, observed);
        assertEquals(0.0281834822193625, pValue, 0.00001);
    }

    @Test
    public void testChiSquareTestWithInvalidLengthExpectedOrObserved() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareTest(expected, observed));
        double[] expectedInvalid = {2.5, 2.5, 2.5, 2.5, 2.5, 2.5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareTest(expectedInvalid, observed));
        observed = new long[5];
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareTest(expected, observed));
        observed = new long[3];
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTestWithInvalidPositiveExpectedOrNonNegativeObserved() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {-1.5, 2.5, 2.5, 2.5, 2.5};
        assertThrows(MathException.class, () -> instance.chiSquareTest(expected, observed));
        double[] expectedInvalid = {2.5, -1.5, 2.5, 2.5, 2.5};
        assertThrows(MathException.class, () -> instance.chiSquareTest(expectedInvalid, observed));
    }

    @Test
    public void testChiSquareTestWithInvalidSignificanceLevel() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareTest(expected, observed, -0.1));
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareTest(expected, observed, 1.1));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValidArguments() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        double chiSquareStatistic = instance.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(3.688557560471223, chiSquareStatistic, 0.00001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithInvalidLengthObserved1OrObserved2() throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 2, 3, 4, 5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareDataSetsComparison(observed1, observed2));
        long[] observed1Invalid = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareDataSetsComparison(observed1Invalid, observed2));
        observed2 = new long[5];
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareDataSetsComparison(observed1, observed2));
        observed1 = new long[3];
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithInvalidNonNegativeObserved1OrObserved2() throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {-1, 2, 3, 4, 5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareDataSetsComparison(observed1, observed2));
        long[] observed1Invalid = {1, 2, 3, 4, 5, 6};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareDataSetsComparison(observed1Invalid, observed2));
        observed2 = new long[]{-1, 2, 3, 4, 5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithInvalidSignificanceLevel() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareTestDataSetsComparison(observed1, observed2, -0.1));
        assertThrows(IllegalArgumentException.class, () -> instance.chiSquareTestDataSetsComparison(observed1, observed2, 1.1));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithIdenticalCounts() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 1, 1, 1, 1};
        long[] observed2 = {1, 1, 1, 1, 1};
        double pValue = instance.chiSquareTestDataSetsComparison(observed1, observed2);
        assertNull(pValue);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithEqualCountsButDifferentData() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 1, 1, 1, 1};
        long[] observed2 = {2, 2, 2, 2, 2};
        double pValue = instance.chiSquareTestDataSetsComparison(observed1, observed2);
        assertFalse(pValue > 0.05);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithDifferentCounts() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 1, 1, 1, 1};
        long[] observed2 = {2, 3, 3, 4, 5};
        double pValue = instance.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(pValue > 0.05);
    }
}