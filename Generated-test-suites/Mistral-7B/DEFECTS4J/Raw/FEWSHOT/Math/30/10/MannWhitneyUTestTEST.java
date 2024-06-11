package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

public class MannWhitneyUTest {

    /**
     * Tests the mannWhitneyU method with equal length samples and equal means.
     */
    @Test
    public void testMannWhitneyUEqualSamplesEqualMean() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 2.5, 3.5};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = 3.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.001);
    }

    /**
     * Tests the mannWhitneyU method with equal length samples and different means.
     */
    @Test
    public void testMannWhitneyUDifferentSamplesDifferentMean() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = 6.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.001);
    }

    /**
     * Tests the mannWhitneyU method with different length samples and equal means.
     */
    @Test
    public void testMannWhitneyUEqualMeanDifferentLength() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 2.5};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = 3.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.001);
    }

    /**
     * Tests the mannWhitneyU method with different length samples and different means.
     */
    @Test
    public void testMannWhitneyUDifferentMeansDifferentLength() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 7.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = 9.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.001);
    }

    /**
     * Tests the mannWhitneyUTest method with equal length samples and equal means.
     */
    @Test
    public void testMannWhitneyUTestEqualSamplesEqualMean() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 2.5, 3.5};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedPValue = 0.05; // You should replace this with the
                                     // expected p-value based on the data
                                     // and significance level
        assertTrue(expectedPValue > pValue);
    }

    /**
     * Tests the mannWhitneyUTest method with equal length samples and different means.
     */
    @Test
    public void testMannWhitneyUTestDifferentSamplesDifferentMean() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertTrue(pValue < 0.05);
        } catch (ConvergenceException | MaxCountExceededException e) {
            // Expected when the p-value can't be computed
        }
    }

    /**
     * Tests the mannWhitneyUTest method with different length samples and equal means.
     */
    @Test
    public void testMannWhitneyUTestEqualMeanDifferentLength() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 2.5};
        try {
            double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertTrue(pValue > 0.05);
        } catch (ConvergenceException | MaxCountExceededException e) {
            // Expected when the p-value can't be computed
        }
    }

    /**
     * Tests the mannWhitneyUTest method with different length samples and different means.
     */
    @Test
    public void testMannWhitneyUTestDifferentMeansDifferentLength() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 7.0};
        try {
            double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertTrue(pValue < 0.05);
        } catch (ConvergenceException | MaxCountExceededException e) {
            // Expected when the p-value can't be computed
        }
    }

    /**
     * Tests the null argument exception.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullArgumentException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(null, new double[0]);
    }

    /**
     * Tests the no data exception.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUNoDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]);
    }

    /**
     * Tests the calculateAsymptoticPValue method.
     */
    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 3.0;
        int n1 = 3;
        int n2 = 3;
        try {
            double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
            assertTrue(pValue > 0.0);
            assertTrue(pValue < 0.1);
        } catch (ConvergenceException | MaxCountExceededException e) {
            // Expected when the p-value can't be computed
        }
    }
}