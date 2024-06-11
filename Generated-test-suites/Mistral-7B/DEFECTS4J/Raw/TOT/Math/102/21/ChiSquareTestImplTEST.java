package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Tests for ChiSquareTestImpl
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor with default distribution
     */
    @Test
    public void testDefaultConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        ChiSquaredDistribution chiSquaredDistribution = chiSquareTest.getDistribution();
        assertEquals(1.0, chiSquaredDistribution.getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with custom distribution
     */
    @Test
    public void testCustomConstructor() {
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(3.0);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(chiSquaredDistribution);
        assertEquals(3.0, chiSquareTest.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test chiSquare method with valid inputs
     */
    @Test
    public void testChiSquareWithValidInputs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};

        double chiSquareValue = chiSquareTest.chiSquare(expected, observed);
        assertEquals(7.778746133361173, chiSquareValue, 0.00001);

        double pValue = chiSquareTest.chiSquareTest(expected, observed) * 100.0;
        assertEquals(45.0, pValue, 0.1);
    }

    /**
     * Test chiSquare method with invalid inputs: wrong size of vectors
     */
    @Test
    public void testChiSquareWithInvalidInputsWrongSize() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed = {1, 2};
        long[] expected = {2, 2, 2, 2, 2};

        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
    }

    /**
     * Test chiSquare method with invalid inputs: negative expected values
     */
    @Test
    public void testChiSquareWithInvalidInputsNegativeExpectedValues() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {-1.5, 2.5, 2.5, 2.5, 2.5};

        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
    }

    /**
     * Test chiSquare method with invalid inputs: negative observed values
     */
    @Test
    public void testChiSquareWithInvalidInputsNegativeObservedValues() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed = {-1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};

        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
    }

    /**
     * Test chiSquareTest method with valid inputs
     */
    @Test
    public void testChiSquareTestWithValidInputs() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double alpha = 0.05;

        boolean rejectNullHypothesis = chiSquareTest.chiSquareTest(expected, observed, alpha);
        assertTrue(rejectNullHypothesis);
    }

    /**
     * Test chiSquareTest method with invalid inputs: wrong alpha value
     */
    @Test
    public void testChiSquareTestWithInvalidInputsWrongAlphaValue() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double alpha = -0.05;

        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed, alpha));
    }

    /**
     * Test chiSquareTest method with valid inputs: reject null hypothesis
     */
    @Test
    public void testChiSquareTestRejectNullHypothesis() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        double alpha = 0.05;

        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.0128; // approximately

        assertEquals(expectedPValue, pValue, 0.001);
        assertTrue(chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha));
    }

    /**
     * Test chiSquareTest method with valid inputs: do not reject null hypothesis
     */
    @Test
    public void testChiSquareTestDoNotRejectNullHypothesis() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed1 = {1, 1, 1, 1, 1};
        long[] observed2 = {1, 1, 1, 1, 1};
        double alpha = 0.05;

        assertFalse(chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha));
    }

    /**
     * Test chiSquareTest method with invalid inputs: wrong number of elements in arrays
     */
    @Test
    public void testChiSquareTestWithInvalidInputsWrongNumberOfElements() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();

        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2};
        double alpha = 0.05;

        assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha));
    }
}