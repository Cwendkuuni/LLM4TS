package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChiSquareTestImpl.
 */
public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTestImpl;

    @Before
    public void setUp() {
        chiSquareTestImpl = new ChiSquareTestImpl();
    }

    /**
     * Test of chiSquare method, of class ChiSquareTestImpl.
     */
    @Test
    public void testChiSquare() throws IllegalArgumentException {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        assertEquals(7.778746355353553, result, 0.000001);
    }

    /**
     * Test of chiSquareTest method, of class ChiSquareTestImpl.
     */
    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertTrue(pValue > 0.04 && pValue < 0.06);
    }

    /**
     * Test of chiSquareTest method, of class ChiSquareTestImpl, with different significance level.
     */
    @Test
    public void testChiSquareTestWithDifferentSignificanceLevel()
            throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
        double alpha = 0.01;
        boolean nullHypothesisCanBeRejected = chiSquareTestImpl.chiSquareTest(
                expected, observed, alpha);
        assertTrue(nullHypothesisCanBeRejected);
    }

    /**
     * Test of chiSquare method for 2-way table.
     */
    @Test
    public void testChiSquare2WayTable() throws IllegalArgumentException {
        long[][] counts = {
            {1, 2},
            {2, 3} };
        double result = chiSquareTestImpl.chiSquare(counts);
        assertEquals(2.0, result, 0.000001);
    }

    /**
     * Test of chiSquareTest method for 2-way table.
     */
    @Test
    public void testChiSquareTest2WayTable()
            throws IllegalArgumentException, MathException {
        long[][] counts = {
            {1, 2},
            {2, 3} };
        double pValue = chiSquareTestImpl.chiSquareTest(counts);
        assertTrue(pValue > 0.95 && pValue < 0.97);
    }

    /**
     * Test of chiSquareTest method for 2-way table, with different significance level.
     */
    @Test
    public void testChiSquareTest2WayTableWithDifferentSignificanceLevel()
            throws IllegalArgumentException, MathException {
        long[][] counts = {
            {1, 2},
            {2, 3} };
        double alpha = 0.01;
        boolean nullHypothesisCanBeRejected = chiSquareTestImpl.chiSquareTest(
                counts, alpha);
        assertTrue(nullHypothesisCanBeRejected);
    }

    /**
     * Test of chiSquareDataSetsComparison method.
     */
    @Test
    public void testChiSquareDataSetsComparison()
            throws IllegalArgumentException {
        long[] observed1 = {1, 2, 3, 2, 1};
        long[] observed2 = {2, 2, 2, 3, 3};
        double result = chiSquareTestImpl.chiSquareDataSetsComparison(
                observed1, observed2);
        assertEquals(3.75, result, 0.000001);
    }

    /**
     * Test of chiSquareTestDataSetsComparison method.
     */
    @Test
    public void testChiSquareTestDataSetsComparison()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 2, 1};
        long[] observed2 = {2, 2, 2, 3, 3};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(
                observed1, observed2);
        assertTrue(pValue > 0.98 && pValue < 0.995);
    }

    /**
     * Test of chiSquareTestDataSetsComparison method, with different significance level.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithDifferentSignificanceLevel()
            throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 2, 1};
        long[] observed2 = {2, 2, 2, 3, 3};
        double alpha = 0.01;
        boolean nullHypothesisCanBeRejected = chiSquareTestImpl.chiSquareTestDataSetsComparison(
                observed1, observed2, alpha);
        assertTrue(nullHypothesisCanBeRejected);
    }
}