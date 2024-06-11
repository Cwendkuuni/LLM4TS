package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        assertNotNull(chiSquareTest);
    }

    @Test
    public void testChiSquareTestImplConstructorWithDistribution() {
        ChiSquaredDistributionImpl chiSquaredDistribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(chiSquaredDistribution);
        assertSame(chiSquaredDistribution, chiSquareTest.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedArrayLength() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[1];
        long[] observed = new long[2];
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidObservedArrayLength() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0};
        long[] observed = new long[1];
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedAndObservedLengthsMismatch() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0};
        long[] observed = new long[]{1, 2};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidExpectedArray() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, -2.0};
        long[] observed = new long[]{1, 2};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidObservedArray() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0};
        long[] observed = new long[]{-1, 2};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareWithValidArguments() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(2.0, result, 1e-10);
    }

    @Test
    public void testChiSquareTestWithValidArguments() throws MathIllegalArgumentException, MathRuntimeException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.95, pValue, 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidArgumentsLengthMismatch() throws MathIllegalArgumentException, MathRuntimeException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0};
        long[] observed = new long[]{1, 2};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidArguments() throws MathIllegalArgumentException, MathRuntimeException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0};
        long[] observed = new long[]{-1, 2};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestWithValidArgumentsAndAlphaGreaterThanZeroAndLessThanOne() throws MathIllegalArgumentException, MathRuntimeException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.1);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestWithInvalidArgumentsAndAlphaLessThanZeroOrGreaterThanOne() throws MathIllegalArgumentException, MathRuntimeException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, -0.1);
        assertFalse(result);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithValidArguments() throws MathIllegalArgumentException, MathRuntimeException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2};
        long[] observed2 = new long[]{2, 1};
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.5, result, 1e-10);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValidArgumentsAndAlphaGreaterThanZeroAndLessThanOne() throws MathIllegalArgumentException, MathRuntimeException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2};
        long[] observed2 = new long[]{2, 1};
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.1);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithInvalidArgumentsAndAlphaLessThanZeroOrGreaterThanOne() throws MathIllegalArgumentException, MathRuntimeException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2};
        long[] observed2 = new long[]{2, 1};
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, -0.1);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidArgumentsLengthMismatch() throws MathIllegalArgumentException, MathRuntimeException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1};
        long[] observed2 = new long[]{1, 2};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.1);
    }

    @Test
    public void testCheckArray() {
        long[][] input = new long[][]{{1, 2, 3}, {2, 3, 4}, {-2, 3, 4}};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        try {
            chiSquareTest.checkArray(input);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        long[][] inputNegative = new long[][]{{-1, 2, 3}, {2, 3, 4}, {-2, 3, 4}};
        try {
            chiSquareTest.checkArray(inputNegative);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        long[][] inputDifferentLengths = new long[][]{{1, 2}, {1, 2, 3}};
        try {
            chiSquareTest.checkArray(inputDifferentLengths);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}