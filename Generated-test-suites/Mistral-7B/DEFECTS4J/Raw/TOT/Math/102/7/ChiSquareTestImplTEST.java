package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.stat.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.stat.distribution.ChiSquaredDistributionImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTest;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquareWithSingleObservedAndExpectedValue() {
        long observed = 10;
        double[] expected = new double[] {1.0};
        double chiSquareStatistic = chiSquareTest.chiSquare(expected, new long[] {observed});
        double pValue = chiSquareTest.chiSquareTest(expected, new long[] {observed});
        assertEquals(expected[0], chiSquareTest.expectedArrayRescaling(expected), 0.001);
        assertEquals(expected[0], expected[0] + observed, 0.001);
        assertEquals(chiSquareStatistic, 0.0, 0.001);
        assertEquals(pValue, 1.0, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidObservedArrayLength() {
        double[] expected = new double[] {1.0, 2.0};
        long[] observed = new long[] {1};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedArrayLength() {
        double[] expected = new double[] {1.0};
        long[] observed = new long[] {2, 2};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidObservedArrayValues() {
        double[] expected = new double[] {1.0, 2.0};
        long[] observed = new long[] {-1, 2};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedArrayValues() {
        double[] expected = new double[] {1.0, 2.0};
        long[] observed = new long[] {2, -1};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedAndObservedLengthMismatch() {
        double[] expected = new double[] {1.0};
        long[] observed = new long[] {2, 2};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareWithValidObservedAndExpectedValues() {
        double[] expected = new double[] {3.0, 7.0, 5.0};
        long[] observed = new long[] {5, 6, 4};
        double chiSquareStatistic = chiSquareTest.chiSquare(expected, observed);
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(expected[0], chiSquareTest.expectedArrayRescaling(expected), 0.001);
        assertEquals(expected[0], expected[0] + observed[0], 0.001);
        assertEquals(expected[1], chiSquareTest.expectedArrayRescaling(expected), 0.001);
        assertEquals(expected[1], expected[1] + observed[1], 0.001);
        assertEquals(expected[2], chiSquareTest.expectedArrayRescaling(expected), 0.001);
        assertEquals(expected[2], expected[2] + observed[2], 0.001);
        assertEquals(chiSquareStatistic, 2.755992368225734E7D, 0.001);
        assertEquals(pValue, 0.00001663952711431838, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidObserved1Length() {
        long[] observed1 = new long[] {1};
        long[] observed2 = new long[] {2, 2};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidObserved2Length() {
        long[] observed1 = new long[] {2, 2};
        long[] observed2 = new long[] {1};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidObservedValues() {
        long[] observed1 = new long[] {1, 2};
        long[] observed2 = new long[] {-1, 2};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidExpectedValues() {
        long[] observed1 = new long[] {1, 2};
        long[] observed2 = new long[] {2, -1};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithValidObservedAndExpectedValues() {
        long[] observed1 = new long[] {3, 2, 4, 1};
        long[] observed2 = new long[] {4, 3, 3, 2};
        double chiSquareStatistic = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(chiSquareStatistic, 5.991464934231533E1D, 0.001);
        assertEquals(pValue, 0.01856849041623336, 0.001);
    }
}