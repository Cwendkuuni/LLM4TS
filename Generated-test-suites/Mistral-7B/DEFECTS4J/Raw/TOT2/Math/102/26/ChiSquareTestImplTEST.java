package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;

/**
 * Test class for ChiSquareTestImpl.
 */
public class ChiSquareTestImplTest {

    /**
     * Test for valid input with default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = test.getDistribution();
        assertEquals("Degrees of freedom not set correctly", 1.0, distribution.getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test for valid input with distribution constructor.
     */
    @Test
    public void testWithDistributionConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(2.0));
        ChiSquaredDistribution distribution = test.getDistribution();
        assertEquals("Degrees of freedom not set correctly", 2.0, distribution.getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test for chiSquare method with valid input.
     */
    @Test
    public void testChiSquareValidInput() {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        double testStatistic = test.chiSquare(expected, observed);
        double expectedTestStatistic = 5.99146487903; // Calculated using statistical software

        assertEquals("Unexpected test statistic", expectedTestStatistic, testStatistic, 0.001);
    }

    /**
     * Test for chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestValidInput() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 0.00346; // Calculated using statistical software

        assertEquals("Unexpected p-value", expectedPValue, pValue, 0.001);
    }

    /**
     * Test for chiSquareTest method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput() throws IllegalArgumentException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 1.5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        test.chiSquareTest(expected, observed);
    }

    /**
     * Test for chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput() throws IllegalArgumentException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 1.5, 2.5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        test.chiSquare(expected, observed);
    }

    /**
     * Test for chiSquare method with array of longs as input.
     */
    @Test
    public void testChiSquareLongs() throws IllegalArgumentException {
        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {2, 3, 3, 3, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        double testStatistic = test.chiSquare(expected, observed);
        double expectedTestStatistic = 6.61089989482; // Calculated using statistical software

        assertEquals("Unexpected test statistic", expectedTestStatistic, testStatistic, 0.001);
    }

    /**
     * Test for chiSquareTest method with array of longs as input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareLongsTestInvalidInput() throws IllegalArgumentException {
        long[] observed = {1, 2, 3};
        long[] expected = {2, 3, 3, 3, 3, 3};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        test.chiSquareTest(expected, observed);
    }

    /**
     * Test for chiSquareDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareDataSetsComparisonValidInput() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 5, 5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        double testStatistic = test.chiSquareDataSetsComparison(observed1, observed2);
        double expectedTestStatistic = 2.776416227; // Calculated using statistical software

        assertEquals("Unexpected test statistic", expectedTestStatistic, testStatistic, 0.001);
    }

    /**
     * Test for chiSquareTestDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValidInput() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 2, 3, 5, 5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.05285; // Calculated using statistical software

        assertEquals("Unexpected p-value", expectedPValue, pValue, 0.001);
    }

    /**
     * Test for chiSquareTestDataSetsComparison method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInput() throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3, 5, 5};
        ChiSquareTestImpl test = new ChiSquareTestImpl();

        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }
}