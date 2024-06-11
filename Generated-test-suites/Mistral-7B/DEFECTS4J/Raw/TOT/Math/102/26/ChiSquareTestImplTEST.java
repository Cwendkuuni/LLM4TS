package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructors.
     */
    @Test
    public void testConstructors() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        Assert.assertNotNull(test.getDistribution());
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test2 = new ChiSquareTestImpl(dist);
        Assert.assertEquals(dist, test2.getDistribution());
    }

    /**
     * Test chiSquare method with valid inputs.
     */
    @Test
    public void testChiSquareValidInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 3, 1, 3};
        double chiSquare = test.chiSquare(expected, observed);
        Assert.assertEquals(6.602540258611255, chiSquare, 0.001);
    }

    /**
     * Test chiSquare method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 1, 1};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid inputs.
     */
    @Test
    public void testChiSquareTestValidInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 3, 1, 3};
        double pValue = test.chiSquareTest(expected, observed);
        Assert.assertEquals(0.02266982603372478, pValue, 0.0001);
    }

    /**
     * Test chiSquareTest method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {1, 1, 1};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with alpha = 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestAlphaZero() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 3, 1, 3};
        test.chiSquareTest(expected, observed, 0.0);
    }

    /**
     * Test chiSquareTest method with alpha > 0.5.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestAlphaGreaterThan05() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 3, 1, 3};
        test.chiSquareTest(expected, observed, 0.7);
    }

    /**
     * Test chiSquare method with 2-way table.
     */
    @Test
    public void testChiSquare2WayTable() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        double chiSquare = test.chiSquare(counts);
        Assert.assertEquals(2.776117209129368, chiSquare, 0.001);
    }

    /**
     * Test chiSquareTest method with 2-way table.
     */
    @Test
    public void testChiSquareTest2WayTable() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        double pValue = test.chiSquareTest(counts);
        Assert.assertEquals(0.3469522737173863, pValue, 0.0001);
    }

    /**
     * Test chiSquareTest method with alpha = 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest2WayTableAlphaZero() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        test.chiSquareTest(counts, 0.0);
    }

    /**
     * Test chiSquareTest method with alpha > 0.5.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest2WayTableAlphaGreaterThan05() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3, 4}};
        test.chiSquareTest(counts, 0.7);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareDataSetsComparisonValidInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2, 1, 2};
        long[] observed2 = {2, 1, 2, 1, 2, 1};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        Assert.assertEquals(1.666666666666667, chiSquare, 0.001);
    }

    /**
     * Test chiSquareDataSetsComparison method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2};
        long[] observed2 = {2, 1, 2, 1};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValidInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2, 1, 2};
        long[] observed2 = {2, 1, 2, 1, 2, 1};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertEquals(0.713546135363873, pValue, 0.0001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2};
        long[] observed2 = {2, 1, 2, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with alpha = 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonAlphaZero() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2, 1, 2};
        long[] observed2 = {2, 1, 2, 1, 2, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with alpha > 0.5.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonAlphaGreaterThan05() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2, 1, 2};
        long[] observed2 = {2, 1, 2, 1, 2, 1};
        test.chiSquareTestDataSetsComparison(observed1, observed2, 0.7);
    }
}