package org.apache.commons.math.stat.inference;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareWithExpectedAndObservedArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};

        double chiSquare = test.chiSquare(expected, observed);
        double pValue = test.chiSquareTest(expected, observed);

        assertEquals(11.0 / 5.0, chiSquare, 0.01); // check if the sum of squared deviations is correct
        assertTrue(pValue > 0.05); // check if the p-value is greater than the significance level
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedAndObservedArrays() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 2.5};

        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareWithLong2DArray() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[][] counts = {{1, 2, 3, 4}, {2, 3, 4, 5}, {3, 4, 5, 6}, {4, 5, 6, 7}};

        double chiSquare = test.chiSquare(counts);
        double pValue = test.chiSquareTest(counts);

        assertEquals(15.0, chiSquare, 0.01); // check if the chi-square value is correct
        assertTrue(pValue > 0.05); // check if the p-value is greater than the significance level
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidLong2DArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[][] counts = {{1, 2}, {3, 4, 5}};

        test.chiSquare(counts);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValidArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 4, 5};

        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);

        assertTrue(pValue > 0.05); // check if the p-value is greater than the significance level
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidArrays() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};

        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }
}