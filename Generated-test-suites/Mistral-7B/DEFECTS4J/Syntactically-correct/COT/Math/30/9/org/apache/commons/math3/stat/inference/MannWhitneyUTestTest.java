package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

/**
 * Test cases for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /** Ranking algorithm. */
    private NaturalRanking naturalRanking;

    /**
     * Initialize test environment.
     */
    public MannWhitneyUTestTest() {
        naturalRanking = new NaturalRanking(NaNStrategy.FIXED,
                TiesStrategy.AVERAGE);
    }

    /**
     * Test method mannWhitneyU().
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyU() throws ConvergenceException,
            MaxCountExceededException, NoDataException, NullArgumentException {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {3.0, 2.0, 1.0, 4.0};
        double expectedResult = 6.0;
        double result = new MannWhitneyUTest().mannWhitneyU(x, y);
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test method mannWhitneyUTest() with null arguments.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArguments()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException {
        new MannWhitneyUTest().mannWhitneyUTest(null, null);
    }

    /**
     * Test method mannWhitneyUTest() with zero-length samples.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroLengthSamples()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        new MannWhitneyUTest().mannWhitneyUTest(new double[0], new double[0]);
    }

    /**
     * Test method mannWhitneyUTest() with identical samples.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyUTestIdenticalSamples()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = {1.0, 2.0, 2.0};
        double[] y = {2.0, 2.0, 2.0};
        double expectedResult = 3.0;
        double result = new MannWhitneyUTest().mannWhitneyU(x, y);
        assertEquals(expectedResult, result, 0.001);

        double pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 0.01);
    }

    /**
     * Test method mannWhitneyUTest() with different lengths.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyUTestDifferentLengths()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expectedResult = 9.0;
        double result = new MannWhitneyUTest().mannWhitneyU(x, y);
        assertEquals(expectedResult, result, 0.001);

        double pValue = new MannWhitneyUTest().mannWhitneyUTest(x, y);
        double normalDistribution = new NormalDistribution(0, 1)
                .cumulativeProbability(1.361); // 95% confidence level
        assertEquals(pValue, normalDistribution, 0.01);
    }
}