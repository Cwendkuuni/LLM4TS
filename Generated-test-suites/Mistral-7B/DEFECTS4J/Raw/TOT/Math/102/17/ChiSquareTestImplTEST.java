package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstruction() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        assertEquals(1.0, chiSquareTestImpl.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testChiSquareTestImplConstructionWithDistribution() {
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(3.0);
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(chiSquaredDistribution);
        assertEquals(3.0, chiSquareTestImpl.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidExpectedArrayLength() {
        long[] observed = new long[1];
        double[] expected = new double[0];
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidExpectedAndObservedLengths() {
        long[] observed = new long[1];
        double[] expected = new double[2];
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidObservedArray() {
        long[] observed = new long[1];
        double[] expected = new double[1];
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.chiSquare(expected, observed);
        observed[0] = -1;
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidExpectedArray() {
        long[] observed = new long[1];
        double[] expected = new double[1];
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.chiSquare(expected, observed);
        expected[0] = -1.0;
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestImplWithValidParameters() {
        long[] observed = new long[2];
        double[] expected = new double[2];
        observed[0] = 10;
        observed[1] = 15;
        expected[0] = 12.0;
        expected[1] = 8.0;
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double chiSquareStatistic = chiSquareTestImpl.chiSquare(expected, observed);
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(5.46222786067842, chiSquareStatistic, 0.01);
        assertEquals(0.0249947163627423, pValue, 0.01);
    }

    @Test
    public void testChiSquareTestImplWithValidParametersAndAlpha() {
        long[] observed = new long[2];
        double[] expected = new double[2];
        observed[0] = 10;
        observed[1] = 15;
        expected[0] = 12.0;
        expected[1] = 8.0;
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTest2wayTableComparisonValidParameters() {
        long[] observed1 = new long[] {5, 2, 3, 7};
        long[] observed2 = new long[] {6, 3, 2, 6};
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double chiSquareStatistic = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(4.53988208336315, chiSquareStatistic, 0.01);
        assertEquals(0.0292471324151056, pValue, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalid2wayTableDimensions() {
        long[][] counts = new long[1][1];
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalid2wayTable() {
        long[][] counts = new long[][]{{1, 2, -1}, {2, 1, 1}};
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestImplWithInvalid2wayTableAndAlpha() {
        long[][] counts = new long[][]{{1, 2, -1}, {2, 1, 1}};
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        chiSquareTestImpl.chiSquareTest(counts, 0.05);
    }

    @Test
    public void testChiSquareTestImplWithValid2wayTable() {
        long[][] counts = new long[][]{{5, 2, 3, 7}, {6, 3, 2, 6}};
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double chiSquareStatistic = chiSquareTestImpl.chiSquare(counts);
        double pValue = chiSquareTestImpl.chiSquareTest(counts);
        assertEquals(4.53988208336315, chiSquareStatistic, 0.01);
        assertEquals(0.0292471324151056, pValue, 0.01);
    }

    @Test
    public void testChiSquareTestImplWithValid2wayTableAndAlpha() {
        long[][] counts = new long[][]{{5, 2, 3, 7}, {6, 3, 2, 6}};
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        boolean result = chiSquareTestImpl.chiSquareTest(counts, 0.05);
        assertTrue(result);
    }
}