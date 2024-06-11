package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest().
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest.naturalRanking);
    }

    /**
     * Test constructor MannWhitneyUTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy).
     */
    @Test
    public void testConstructorWithStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaturalRanking.MISSING, TiesStrategy.MIN);
        assertNotNull(mannWhitneyUTest.naturalRanking);
    }

    /**
     * Test method ensureDataConformance.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
    }

    /**
     * Test method ensureDataConformance.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method mannWhitneyU.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{5.0, 5.0, 6.0, 7.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(8.0, mannWhitneyU, 1e-10);
    }

    /**
     * Test method mannWhitneyUTest.
     */
    @Test
    public void testMannWhitneyUTest() throws NoDataException,
            ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};
        double mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expected = 0.0556; // From Wikipedia example
        assertEquals(expected, mannWhitneyUTestResult, 1e-8);
    }

    /**
     * Test method mannWhitneyUTest with different strategies.
     */
    @Test
    public void testMannWhitneyUTestWithStrategies()
            throws NoDataException, ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaturalRanking.MIN, TiesStrategy.LOWER);
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};
        double mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expected = 0.0667; // From Wikipedia example
        assertEquals(expected, mannWhitneyUTestResult, 1e-8);
    }

    /**
     * Test method mannWhitneyUTest with identical samples.
     */
    @Test
    public void testMannWhitneyUTestWithIdenticalSamples()
            throws NoDataException, ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 1.0, 1.0};
        double[] y = new double[]{1.0, 1.0, 1.0};
        double mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expected = 1.0;
        assertEquals(expected, mannWhitneyUTestResult, 1e-8);
    }

    /**
     * Test method mannWhitneyUTest with empty samples.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptySamples()
            throws NoDataException, ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Test method mannWhitneyUTest with null samples.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullSamples()
            throws NoDataException, ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, new double[0]);
    }
}