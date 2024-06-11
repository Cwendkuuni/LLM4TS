package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.distribution);
        assertEquals(1.0, test.distribution.getDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testChiSquareTestWithArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 3, 3, 5};
        double pValue = test.chiSquareTest(expected, observed);
        assertTrue(pValue > 0.0);
        assertTrue(pValue < 1.0);
    }

    @Test
    public void testChiSquareWithInvalidExpectedAndObservedLengths() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(new double[0], new long[1]));
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(new double[]{1.0}, new long[0]));
    }

    @Test
    public void testChiSquareWithInvalidExpectedAndObservedValues() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4};
        long[] observed = {-1, 2, 3, 4};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expected, observed));
        expected = new long[]{-1, 2, 3, 4};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expected, observed));
        double[] expectedD = {1.0, 2.0, 3.0, 4.0};
        long[] observedL = {1, 2, 3, 4};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expectedD, observedL));
    }

    @Test
    public void testChiSquareWithValidInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 3, 3, 5};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(10.0, chiSquare, 0.0);
    }

    @Test
    public void testChiSquareTestWithArrays_InvalidDistribution() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 3, 3, 5};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTest(expected, observed, new ChiSquaredDistributionImpl(0.5)));
    }

    @Test
    public void testChiSquareTestWithArrays_ValidInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 3, 3, 5};
        double significanceLevel = 0.05;
        boolean rejectNullHypothesis = test.chiSquareTest(expected, observed, significanceLevel);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestWithArrays_ValidInputs_SmallSignificanceLevel() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 3, 3, 5};
        double significanceLevel = 0.01;
        boolean rejectNullHypothesis = test.chiSquareTest(expected, observed, significanceLevel);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestWithArrays_ValidInputs_LargeSignificanceLevel() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3, 4};
        long[] observed = {2, 3, 3, 5};
        double significanceLevel = 0.5;
        boolean rejectNullHypothesis = test.chiSquareTest(expected, observed, significanceLevel);
        assertFalse(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithInvalidInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1};
        long[] observed2 = {2};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareDataSetsComparisonWithValidInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 5};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(10.0, chiSquare, 0.0);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithArrays_InvalidDistribution() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 5};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTestDataSetsComparison(observed1, observed2, new ChiSquaredDistributionImpl(0.5)));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithArrays_ValidInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 5};
        double significanceLevel = 0.05;
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithArrays_ValidInputs_SmallSignificanceLevel() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 5};
        double significanceLevel = 0.01;
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithArrays_ValidInputs_LargeSignificanceLevel() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 3, 3, 5};
        double significanceLevel = 0.5;
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertFalse(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithArrays_EqualDataSets() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 1, 1, 1};
        long[] observed2 = {1, 1, 1, 1};
        double significanceLevel = 0.05;
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertFalse(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestWithLongArray() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double expected = 15.0;
        double chiSquare = test.chiSquare(data);
        assertEquals(expected, chiSquare, 0.0);
    }

    @Test
    public void testChiSquareTestWithLongArray_InvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] data = {-1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(data));
    }

    @Test
    public void testChiSquareTestWithLongArray_SmallSampleSize() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] data = {1, 2, 3};
        double expected = 2.0;
        double chiSquare = test.chiSquare(data);
        assertEquals(expected, chiSquare, 0.0);
    }

    @Test
    public void testChiSquareTestWithLongArray_LargeSampleSize() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] data = new long[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = (long) (Math.random() * 100);
        }
        double expected = data.length * 100.0;
        double chiSquare = test.chiSquare(data);
        assertEquals(expected, chiSquare, 0.0);
    }
}