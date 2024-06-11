package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Test class for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor with default distribution.
     */
    @Test
    public void testDefaultConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.getDistribution());
        assertEquals(1.0, test.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with given distribution.
     */
    @Test
    public void testCustomConstructor() {
        ChiSquaredDistributionImpl dist = new ChiSquaredDistributionImpl(3.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertEquals(dist, test.getDistribution());
        assertEquals(3.0, test.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquare(double[], long[])}.
     * <p>
     * Test with valid input.
     */
    @Test
    public void testChiSquareValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double result = test.chiSquare(expected, observed);
        double expectedResult = 2.0;
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquare(double[], long[])}.
     * <p>
     * Test with invalid input length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {};
        test.chiSquare(expected, observed);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquare(double[], long[])}.
     * <p>
     * Test with invalid input non-positive expected values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidExpectedValues() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0};
        long[] observed = {1.0, 1.0};
        test.chiSquare(expected, observed);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquare(long[][])}.
     * <p>
     * Test with valid input.
     */
    @Test
    public void testChiSquareArrayValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        double result = test.chiSquare(counts);
        double expectedResult = 2.0;
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquare(long[][])}.
     * <p>
     * Test with invalid input non-rectangular array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3}};
        test.chiSquare(counts);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquareTest(double[], long[])}.
     * <p>
     * Test with valid input.
     */
    @Test
    public void testChiSquareTestValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 0.95;
        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquareTest(long[][])}.
     * <p>
     * Test with valid input.
     */
    @Test
    public void testChiSquareTestArrayValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        double pValue = test.chiSquareTest(counts);
        double expectedPValue = 0.95;
        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquareTest(double[], long[], double)}.
     * <p>
     * Test with valid input.
     */
    @Test
    public void testChiSquareTestSignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double significanceLevel = 0.05;
        boolean rejectedHypothesis = test.chiSquareTest(expected, observed, significanceLevel);
        boolean expectedResult = true;
        assertEquals(expectedResult, rejectedHypothesis);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquareTest(double[], long[], double)}.
     * <p>
     * Test with insufficient degrees of freedom.
     */
    @Test(expected = MathException.class)
    public void testChiSquareTestInsufficientDegreesOfFreedom() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 1.0};
        long[] observed = {2.0, 2.0};
        double significanceLevel = 0.05;
        test.chiSquareTest(expected, observed, significanceLevel);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[])}.
     * <p>
     * Test with valid input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.975;
        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[])}.
     * <p>
     * Test with invalid input length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInputLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {};
        long[] observed2 = {1, 2, 3};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[])}.
     * <p>
     * Test with invalid input non-identical length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInputNonIdenticalLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }
}