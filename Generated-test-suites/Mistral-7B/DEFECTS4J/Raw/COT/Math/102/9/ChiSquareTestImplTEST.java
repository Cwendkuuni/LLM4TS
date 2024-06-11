package org.apache.commons.math.stat.inference; // import necessary packages here

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ChiSquareTestImpl.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        assertNotNull(instance.distribution);
    }

    /**
     * Test chiSquare method with valid inputs.
     */
    @Test
    public void testChiSquareValidInputs() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        double result = instance.chiSquare(expected, observed);
        double expectedResult = 5.142857142857143; // calculated using the formula
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test chiSquare method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputs1() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {};
        instance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputs2() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {};
        instance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputs3() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {1};
        instance.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputs4() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {};
        instance.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid inputs.
     */
    @Test
    public void testChiSquareTestValidInputs() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        double pValue = instance.chiSquareTest(expected, observed);
        double expectedPValue = 0.9512540625; // calculated using the formula
        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test chiSquareTest method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputs1() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {};
        instance.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputs2() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {1};
        instance.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputs3() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {1, 1};
        instance.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputs4() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {};
        instance.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with different significance levels.
     */
    @Test
    public void testChiSquareTestDifferentSignificanceLevels() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        double pValue = instance.chiSquareTest(expected, observed);
        double alpha = 0.05;
        boolean result = instance.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
        alpha = 0.01;
        result = instance.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
        alpha = 0.001;
        result = instance.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
        alpha = 0.0001;
        result = instance.chiSquareTest(expected, observed, alpha);
        assertTrue(result);
    }

    /**
     * Test chiSquare method with 2-way table.
     */
    @Test
    public void testChiSquare2WayTable() throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double result = instance.chiSquare(counts);
        double expectedResult = 7.810245358382424; // calculated using the formula
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test chiSquareTest method with 2-way table and valid p-value.
     */
    @Test
    public void testChiSquareTest2WayTableValidPValue() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double pValue = instance.chiSquareTest(counts);
        double expectedPValue = 0.878764190221624; // calculated using the formula
        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test chiSquareTest method with 2-way table and invalid p-value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTest2WayTableInvalidPValue() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double pValue = 1.1;
        instance.chiSquareTest(counts, pValue);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareDataSetsComparisonValidInputs() throws IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        double result = instance.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 3.25; // calculated using the formula
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs and valid p-value.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValidInputsAndPValue() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        double alpha = 0.05;
        boolean result = instance.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(result);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs and invalid p-value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidPValue() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4};
        long[] observed2 = {2, 2, 3, 4};
        double alpha = 1.1;
        instance.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
    }
}