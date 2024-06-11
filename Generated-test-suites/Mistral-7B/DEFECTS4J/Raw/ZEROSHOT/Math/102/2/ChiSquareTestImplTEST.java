package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

/**
 * Test class for ChiSquareTestImpl.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.getDistribution());
    }

    /**
     * Test constructor with provided distribution.
     */
    @Test
    public void testConstructorWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(2.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, test.getDistribution());
    }

    /**
     * Test chiSquare method with valid inputs.
     */
    @Test
    public void testChiSquareValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(8.0 / 3.0, chiSquare, 1e-10);
    }

    /**
     * Test chiSquare method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2.0, 3.0, 1.0};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid inputs.
     */
    @Test
    public void testChiSquareTestValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.0435326629065393, pValue, 1e-10);
    }

    /**
     * Test chiSquareTest method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2.0, 3.0, 1.0};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with alpha.
     */
    @Test
    public void testChiSquareTestWithAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 1.0};
        boolean rejectNullHypothesis = test.chiSquareTest(expected, observed, 0.05);
        assertFalse(rejectNullHypothesis);
    }

    /**
     * Test chiSquare method with 2-way table.
     */
    @Test
    public void testChiSquare2WayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double chiSquare = test.chiSquare(counts);
        assertEquals(14.361436143614362, chiSquare, 1e-10);
    }

    /**
     * Test chiSquareTest method with 2-way table and alpha.
     */
    @Test
    public void testChiSquareTest2WayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        boolean rejectNullHypothesis = test.chiSquareTest(counts, 0.05);
        assertFalse(rejectNullHypothesis);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareDataSetsComparisonValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(4.5, chiSquare, 1e-10);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValidInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.1578110251518551, pValue, 1e-10);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with alpha.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(rejectNullHypothesis);
    }
}