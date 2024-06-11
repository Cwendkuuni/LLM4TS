package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Test;

/**
 * Test cases for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor with default distribution.
     */
    @Test
    public void testDefaultConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assert(test instanceof ChiSquareTestImpl);
        assert(test instanceof UnknownDistributionChiSquareTest);
        assertNull(test.getDistribution());
    }

    /**
     * Test constructor with provided distribution.
     */
    @Test
    public void testConstructWithDistribution() {
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(5.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assert(test instanceof ChiSquareTestImpl);
        assert(test instanceof UnknownDistributionChiSquareTest);
        assertEquals(dist, test.getDistribution());
    }

    /**
     * Test chiSquare method with given expected and observed arrays.
     */
    @Test
    public void testChiSquareWithExpectedAndObservedArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double result = test.chiSquare(expected, observed);
        assertEquals(5.1428571428571425, result, 0.00001);
    }

    /**
     * Test chiSquareTest method with given expected and observed arrays.
     */
    @Test
    public void testChiSquareTestWithExpectedAndObservedArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.1326990678316612, pValue, 0.00001);
    }

    /**
     * Test chiSquareTest method with given expected and observed arrays and alpha
     */
    @Test
    public void testChiSquareTestWithExpectedAndObservedArraysAndAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double alpha = 0.05;
        boolean rejectHypothesis = test.chiSquareTest(expected, observed, alpha);
        assertFalse(rejectHypothesis);
    }

    /**
     * Test chiSquare method with 2-way table counts.
     */
    @Test
    public void testChiSquareWith2WayTableCounts() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {
            {1, 2, 3},
            {2, 3, 1},
            {3, 1, 2}
        };
        double result = test.chiSquare(counts);
        assertEquals(7.810249972348873, result, 0.00001);
    }

    /**
     * Test chiSquareTest method with 2-way table counts.
     */
    @Test
    public void testChiSquareTestWith2WayTableCounts() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {
            {1, 2, 3},
            {2, 3, 1},
            {3, 1, 2}
        };
        double pValue = test.chiSquareTest(counts);
        assertEquals(0.06452173645463465, pValue, 0.00001);
    }

    /**
     * Test chiSquareTest method with 2-way table counts and alpha.
     */
    @Test
    public void testChiSquareTestWith2WayTableCountsAndAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {
            {1, 2, 3},
            {2, 3, 1},
            {3, 1, 2}
        };
        double alpha = 0.05;
        boolean rejectHypothesis = test.chiSquareTest(counts, alpha);
        assertFalse(rejectHypothesis);
    }

    /**
     * Test chiSquareDataSetsComparison method with observed1 and observed2 arrays.
     */
    @Test
    public void testChiSquareDataSetsComparisonWithObservedArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2, 3, 2};
        long[] observed2 = {2, 1, 2, 1, 1, 3};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(3.841459476096331, result, 0.00001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with observed1 and observed2 arrays.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithObservedArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2, 3, 2};
        long[] observed2 = {2, 1, 2, 1, 1, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.05162537155115334, pValue, 0.00001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with observed1 and observed2 arrays and alpha.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithObservedArraysAndAlpha() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2, 3, 2};
        long[] observed2 = {2, 1, 2, 1, 1, 3};
        double alpha = 0.05;
        boolean rejectHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(rejectHypothesis);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2.0, 3.0, 1.0}; // wrong length
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs2() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0}; // wrong length
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs3() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-1, 2.0, 3.0}; // negative observed value
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs4() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 2, 3}; // wrong length
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs5() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2, 2}; // duplicate observed value
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs6() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 2, 2}; // duplicate observed value
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputs7() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3, 4}; // extra observed value
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2.0, 3.0, 1.0}; // wrong length
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputs2() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0}; // wrong length
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputs3() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-1, 2.0, 3.0}; // negative observed value
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputs4() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 2, 3}; // wrong length
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputs5() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2, 2}; // duplicate observed value
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputs6() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 2, 2}; // duplicate observed value
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputs7() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3, 4}; // extra observed value
        test.chiSquareTest(expected, observed);
    }
}